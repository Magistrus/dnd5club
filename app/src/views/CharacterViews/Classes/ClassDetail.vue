<template>
    <div
        v-if="!loading && currentClass"
        class="class-detail"
    >
        <section-header
            :title="currentClass.name.rus"
            :subtitle="currentClass.name.eng"
            :copy="urlForCopy"
            :close="closeClass"
            fullscreen
        />

        <swiper
            v-if="tabs.list.length"
            class="class-detail__tabs"
            :scrollbar="{ draggable: true, hide: true, snapOnRelease: true }"
            :slides-per-view="'auto'"
            :mousewheel="false"
            :free-mode="true"
            :modules="tabs.modules"
        >
            <swiper-slide
                v-for="(tab, tabKey) in tabs.list"
                :key="tabKey"
                class="class-detail__tab"
                :class="{ 'is-active': currentTab?.name === tab.name }"
                @click.left.exact.prevent="clickTabHandler({ index: tabKey, callback: tab.callback })"
            >
                <div class="class-detail__tab_icon">
                    <svg-icon :icon-name="tab.icon"/>
                </div>

                <div class="class-detail__tab_name">
                    {{ tab.name }}
                </div>
            </swiper-slide>
        </swiper>

        <div
            v-if="currentTab?.content"
            ref="classBody"
            class="class-detail__body"
        >
            <div
                v-if="currentArchetypes.length && currentTab.name === 'Навыки'"
                class="class-detail__select"
            >
                <field-select
                    :model-value="currentSelectArchetype"
                    :options="currentArchetypes"
                    group-values="list"
                    group-label="group"
                    :group-select="false"
                    track-by="url"
                    label="name"
                >
                    <template #option="{ option }">
                        <div
                            v-if="option?.group"
                            class="class-detail__select_option"
                        >
                            <span class="class-detail__select_option--name">{{ option.group }}</span>
                        </div>

                        <router-link
                            v-else-if="option?.name"
                            :to="option.url"
                            class="class-detail__select_option"
                        >
                            <span class="class-detail__select_option--name">{{ option.name }}</span>

                            <span
                                v-if="option?.source"
                                class="class-detail__select_option--source"
                            >[{{ option.source }}]</span>
                        </router-link>
                    </template>

                    <template #placeholder>
                        --- Архетипы ---
                    </template>
                </field-select>
            </div>

            <div class="class-detail__body--inner">
                <!-- eslint-disable vue/no-v-html -->
                <div
                    v-if="currentTab.raw"
                    class="class-detail__raw"
                    v-html="currentTab.content"
                />
                <!-- eslint-enable vue/no-v-html -->

                <div
                    v-else-if="currentTab.name === 'Изображения'"
                    class="class-detail__images"
                >
                    <img
                        v-for="(img, imgKey) in currentTab.content"
                        :key="imgKey"
                        :src="img"
                        :alt="currentClass.name.rus"
                        class="class-detail__images_item"
                    >
                </div>
            </div>
        </div>
    </div>

    <vue-easy-lightbox
        v-if="currentClass?.images"
        scroll-disabled
        move-disabled
        loop
        :visible="images.show"
        :imgs="currentClass.images"
        :index="images.index"
        @hide="images.show = false"
    >
        <template #toolbar/>
    </vue-easy-lightbox>

    <div
        v-show="loading"
        class="class-detail"
    >
        <div class="class-detail__loader">
            <div class="class-detail__loader_img">
                <img
                    src="/public/img/loader.png"
                    alt=""
                >
            </div>
        </div>
    </div>
</template>

<script>
    import { Swiper, SwiperSlide } from 'swiper/vue';
    import {
        A11y, FreeMode, Mousewheel, Scrollbar
    } from 'swiper';
    import SectionHeader from '@/components/SectionHeader';
    import SvgIcon from '@/components/UI/SvgIcon';
    import HTTPService from '@/utils/HTTPService';
    import { useClassesStore } from '@/store/CharacterStore/ClassesStore';
    import FieldSelect from '@/components/UI/FieldType/FieldSelect';

    export default {
        name: 'ClassDetail',
        components: {
            FieldSelect,
            SvgIcon,
            SectionHeader,
            Swiper,
            SwiperSlide,
        },
        beforeRouteUpdate(to, from, next) {
            this.loading = true;

            const store = useClassesStore();

            store.deselectClass();
            store.classInfoQuery(to.params.className, to.params?.classArchetype)
                .then(async () => {
                    this.loading = false;

                    await this.initTabs();
                    await this.setTab(0);

                    next();
                })
                .catch(err => {
                    console.error(err)
                });
        },
        data: () => ({
            http: new HTTPService(),
            classesStore: useClassesStore(),
            loading: true,
            currentTab: undefined,
            tabs: {
                list: [],
                modules: [FreeMode, Scrollbar, Mousewheel, A11y]
            },
            images: {
                show: false,
                index: 0,
            }
        }),
        computed: {
            urlForCopy() {
                return window.location.origin + this.$route.fullPath
            },

            currentClass() {
                return this.classesStore.getCurrentClass || undefined
            },

            currentSelectArchetype() {
                let selected;

                for (let i = 0; i < this.currentArchetypes.length && !selected; i++) {
                    for (let index = 0; index < this.currentArchetypes[i].list.length && !selected; index++) {
                        if (this.currentArchetypes[i].list[index].url === this.$route.fullPath) {
                            selected = this.currentArchetypes[i].list[index];
                        }
                    }
                }

                return selected;
            },

            currentArchetypes() {
                return this.classesStore.getCurrentArchetypes || undefined
            },
        },
        async beforeMount() {
            await this.initTabs();

            this.loading = false;
        },
        methods: {
            async initTabs() {
                this.tabs.list = this.currentClass.tabs.map(tab => ({
                    ...tab,
                    content: undefined
                }));

                if (this.currentClass?.images) {
                    this.tabs.list.push({
                        name: 'Изображения',
                        icon: 'tab-images',
                        active: false,
                        order: this.tabs.length,
                        callback: () => {
                            this.images.show = true
                        }
                    })
                }

                await this.setTab(0);
            },

            closeClass() {
                this.$router.push({ name: 'classes' });
            },

            async clickTabHandler({
                index,
                callback
            }) {
                if (typeof callback === 'function') {
                    callback();

                    return;
                }

                await this.setTab(index);
            },

            async getTabContent(tab) {
                let res;

                if (tab.raw) {
                    res = await this.http.rawGet(tab.url);

                    return res;
                }

                res = await this.http.get(tab.url);

                return res;
            },

            async setTab(index) {
                try {
                    const tab = this.tabs.list[index];

                    if (!tab.content) {
                        const res = await this.getTabContent(tab);

                        if (res.status !== 200) {
                            console.error(res.statusText);

                            return;
                        }

                        tab.content = res.data;
                    }

                    this.currentTab = tab;

                    this.$nextTick(() => {
                        if (this.$refs.classBody) {
                            this.$refs.classBody.scrollIntoView({
                                block: 'start'
                            });
                        }
                    })
                } catch (err) {
                    console.error(err)
                }
            },

            updateGrid() {
                this.$nextTick(() => this.$redrawVueMasonry('classes-detail-images'))
            },
        }
    }
</script>

<style lang="scss" scoped>
    .class-detail {
        overflow: hidden;
        width: 100%;
        height: 100%;
        background-color: var(--bg-secondary);
        display: flex;
        flex-direction: column;

        &__loader {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;

            &_img {
                width: 70%;
                position: relative;
                display: flex;
                align-items: center;
                justify-content: center;

                &:before {
                    content: '';
                    display: block;
                    width: 100%;
                    padding-bottom: 100%;
                }

                img {
                    width: 100%;
                    height: 100%;
                    object-fit: contain;
                    position: absolute;
                    filter: drop-shadow(0 0 12px var(--bg-main));
                }
            }
        }

        &__tabs {
            display: flex;
            width: 100%;
            flex-shrink: 0;
            border: {
                width: 1px 0;
                style: solid;
                color: var(--border);
            };
        }

        &__tab {
            @include css_anim();

            display: flex;
            align-items: center;
            justify-content: center;
            padding: 0 24px;
            cursor: pointer;
            height: 46px;
            flex: 1 1 100%;
            min-width: fit-content;

            & + & {
                border-left: 1px solid var(--border);
            }

            &_icon {
                width: 24px;
                height: 24px;
                display: flex;
                align-items: center;
                justify-content: center;
                flex-shrink: 0;
                color: var(--primary);
            }

            &_name {
                color: var(--text-color);
                margin-left: 16px;
                white-space: nowrap;
                font-size: var(--main-font-size);
            }

            @include media-min($md) {
                &:hover {
                    background-color: var(--bg-sub-menu);
                }
            }

            &.is-active {
                background-color: var(--primary-active);

                .class-detail__tab {
                    &_icon,
                    &_name {
                        color: var(--text-btn-color);
                    }
                }
            }
        }

        &__select {
            ::v-deep(.dnd5club-select) {
                .multiselect {
                    &__tags {
                        border-top: 0;
                    }
                }
            }

            &_option {
                background-color: transparent;
                color: inherit;
                padding: 12px;
                width: 100%;
                display: block;
                min-height: 40px;
                text-decoration: none;
                text-transform: none;
                vertical-align: middle;
                position: absolute;
                left: 0;
                top: 0;

                &--source {
                    margin-left: 4px;
                    color: var(--text-g-color);
                }
            }
        }

        &__body {
            width: 100%;
            flex: 1 1 100%;
            overflow: auto;

            &--inner {
                padding: 24px;
            }
        }

        &__images {
            width: 100%;

            &_item {
                width: calc(100% / 3);
            }
        }
    }
</style>
