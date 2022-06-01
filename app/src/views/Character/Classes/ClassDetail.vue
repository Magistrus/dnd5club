<template>
    <div class="class-detail">
        <section-header
            :close="closeClass"
            :copy="urlForCopy"
            :subtitle="currentClass?.name?.eng || ''"
            :title="currentClass?.name?.rus || ''"
            fullscreen
        />

        <div
            v-if="loading"
            class="class-detail__content"
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

        <div
            v-else-if="error"
            class="class-detail__content"
        >
            <div class="class-detail__err">
                err
            </div>
        </div>

        <div
            v-else-if="currentClass"
            class="class-detail__content"
        >
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

                            <div
                                v-else-if="option?.name"
                                class="class-detail__select_option"
                                @click.left.prevent.exact="goToArchetype(option.url)"
                            >
                                <span class="class-detail__select_option--name">{{ option.name }}</span>

                                <span
                                    v-if="option?.source"
                                    class="class-detail__select_option--source"
                                >[{{ option.source }}]</span>
                            </div>
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
    import errorHandler from "@/helpers/errorHandler";

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
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewClass(to.path);

            next();
        },
        data: () => ({
            http: new HTTPService(),
            classesStore: useClassesStore(),
            loading: true,
            error: false,
            currentClass: undefined,
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
                return window.location.origin + this.$route.path
            },

            classes() {
                return this.classesStore.getClasses || []
            },

            currentSelectArchetype() {
                let selected;

                for (let i = 0; i < this.currentArchetypes.length && !selected; i++) {
                    for (let index = 0; index < this.currentArchetypes[i].list.length && !selected; index++) {
                        if (this.currentArchetypes[i].list[index].url === this.$route.path) {
                            selected = this.currentArchetypes[i].list[index];
                        }
                    }
                }

                return selected;
            },

            currentArchetypes() {
                const getArchetypes = list => {
                    const sorted = [];

                    for (let i = 0; i < list.length; i++) {
                        const el = list[i];

                        if (Array.isArray(el) && el.length) {
                            sorted.push(...el);
                        }
                    }

                    return sorted.map(el => ({
                        group: el.name,
                        list: el.list.map(arch => ({
                            name: arch.name.rus,
                            source: arch.source.shortName,
                            url: arch.url
                        }))
                    }));
                }

                const classLink = this.classes.find(classItem => this.$route.path.match(classItem.url));

                return classLink
                    ? getArchetypes(classLink.archetypes)
                    : [];
            },
        },
        async mounted() {
            await this.loadNewClass(this.$route.path);
        },
        methods: {
            async loadNewClass(url) {
                try {
                    this.error = false;
                    this.loading = true;
                    this.currentTab = undefined;
                    this.tabs.list = [];
                    this.images = {
                        show: false,
                        index: 0
                    };

                    const loadedClass = await this.classesStore.classInfoQuery(url);

                    await this.initTabs(loadedClass);

                    this.currentClass = loadedClass;

                    this.loading = false;
                } catch (err) {
                    this.error = true;
                }
            },

            async initTabs(loadedClass) {
                this.tabs.list = loadedClass.tabs.map(tab => ({
                    ...tab,
                    content: undefined
                }));

                if (loadedClass.images) {
                    this.tabs.list.push({
                        icon: 'tab-images',
                        active: false,
                        order: this.tabs.length,
                        callback: () => {
                            this.images.show = true
                        }
                    });
                }

                await this.setTab(0);
            },

            async setTab(index) {
                try {
                    const tab = this.tabs.list[index];

                    if (!tab.content && tab.name !== 'Заклинания') {
                        const { data } = await this.getTabContent(tab);

                        tab.content = data;
                    }

                    this.currentTab = tab;

                    this.$nextTick(() => {
                        if (this.$refs.classBody) {
                            this.$refs.classBody.scrollIntoView({
                                block: 'start',
                            });
                        }
                    })
                } catch (err) {
                    errorHandler(err)
                }
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

            goToArchetype(path) {
                this.$router.push({ path })
            },

            scrollToSection() {
                const section = this.$refs.classBody.querySelector(this.$route.hash);

                section.scrollIntoView({
                    block: "start",
                    behavior: "smooth"
                });
            },

            closeClass() {
                this.$router.push({ name: 'classes' });
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

        &__content {
            width: 100%;
            flex: 1 1 100%;
            overflow: hidden;
            display: flex;
            flex-direction: column;
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
