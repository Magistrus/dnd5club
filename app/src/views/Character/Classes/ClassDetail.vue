<template>
    <div
        v-if="!loading && currentClass"
        class="class-detail"
    >
        <section-header
            :close="closeClass"
            :copy="urlForCopy"
            :subtitle="currentClass.name.eng"
            :title="currentClass.name.rus"
            fullscreen
        />

        <swiper
            v-if="tabs.list.length"
            :free-mode="true"
            :modules="tabs.modules"
            :mousewheel="false"
            :scrollbar="{ draggable: true, hide: true, snapOnRelease: true }"
            :slides-per-view="'auto'"
            class="class-detail__tabs"
        >
            <swiper-slide
                v-for="(tab, tabKey) in tabs.list"
                :key="tabKey"
                :class="{ 'is-active': currentTab?.name === tab.name, 'is-only-icon': !tab.name }"
                class="class-detail__tab"
                @click.left.exact.prevent="clickTabHandler({ index: tabKey, callback: tab.callback })"
            >
                <div class="class-detail__tab_icon">
                    <svg-icon :icon-name="tab.icon"/>
                </div>

                <div
                    v-if="tab.name"
                    class="class-detail__tab_name"
                >
                    {{ tab.name }}
                </div>
            </swiper-slide>
        </swiper>

        <div
            ref="classBody"
            class="class-detail__body"
        >
            <div
                v-if="currentArchetypes.length && currentTab.name === 'Навыки'"
                class="class-detail__select"
            >
                <field-select
                    :group-select="false"
                    :model-value="currentSelectArchetype"
                    :options="currentArchetypes"
                    :searchable="false"
                    group-label="group"
                    group-values="list"
                    label="name"
                    track-by="url"
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

            <spells-view
                v-if="currentTab?.name === 'Заклинания'"
                :store-key="currentClass.name.eng.replaceAll(' ', '')"
                :in-tab="true"
            />

            <div
                v-else
                class="class-detail__body--inner"
            >
                <!-- eslint-disable vue/no-v-html -->
                <div
                    class="class-detail__raw"
                    v-html="currentTab.content"
                />
                <!-- eslint-enable vue/no-v-html -->
            </div>
        </div>
    </div>

    <vue-easy-lightbox
        v-if="currentClass?.images"
        :imgs="currentClass.images"
        :index="images.index"
        :visible="images.show"
        loop
        move-disabled
        scroll-disabled
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
                    alt=""
                    src="/app/img/loader.png"
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
    import SectionHeader from '@/components/UI/SectionHeader';
    import SvgIcon from '@/components/UI/SvgIcon';
    import HTTPService from '@/services/HTTPService';
    import { useClassesStore } from '@/store/CharacterStore/ClassesStore';
    import FieldSelect from '@/components/form/FieldType/FieldSelect';
    import SpellsView from "@/views/Spells/SpellsView";

    export default {
        name: 'ClassDetail',
        components: {
            SpellsView,
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
            store.classInfoQuery(to.url)
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

                    if (!tab.content && tab.name !== 'Заклинания') {
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

                        if (this.$refs.rawDetail) {
                            this.$refs.rawDetail.innerHTML = tab.content;
                        }
                    })
                } catch (err) {
                    console.error(err)
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .class-detail {
        overflow: hidden;
        width: 100%;
        height: 100%;
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

            &.is-only-icon {
                flex: 1 0 fit-content;
            }

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
