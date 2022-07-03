<template>
    <content-detail class="class-detail">
        <template #fixed>
            <section-header
                :copy="!error && !loading"
                :fullscreen="!getIsMobile"
                :subtitle="currentClass?.name?.eng || ''"
                :title="currentClass?.name?.rus || ''"
                close-on-desktop
                @close="close"
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
                        <svg-icon :icon-name="`tab-${tab.type}`"/>
                    </div>

                    <div
                        v-if="tab.name"
                        class="class-detail__tab_name"
                    >
                        {{ tab.name }}
                    </div>
                </swiper-slide>
            </swiper>
        </template>

        <template #default>
            <div
                v-if="loading"
                class="class-detail__content"
            >
                <div class="class-detail__loader">
                    <div class="class-detail__loader_img">
                        <img
                            v-lazy="'/img/loader.png'"
                            alt=""
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
                <div
                    v-if="currentTab && currentTab.type !== 'spells' && currentTab.type !== 'options'"
                    ref="classBody"
                    class="class-detail__body"
                >
                    <div
                        v-if="getIsMobile && currentArchetypes.length && currentTab.type === 'traits'"
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

                    <div class="class-detail__body--inner">
                        <raw-content
                            :url="currentTab.url"
                            @loaded="initScrollListeners"
                            @before-unmount="removeScrollListeners"
                        />
                    </div>
                </div>

                <spells-view
                    v-else-if="currentTab.type === 'spells'"
                    :store-key="getStoreKey"
                    in-tab
                />

                <options-view
                    v-else-if="currentTab.type === 'options'"
                    :store-key="getStoreKey"
                    in-tab
                />
            </div>

            <vue-easy-lightbox
                v-if="currentClass?.images?.length"
                :imgs="currentClass?.images"
                :index="gallery.index"
                :visible="gallery.show"
                loop
                move-disabled
                scroll-disabled
                @hide="gallery.show = false"
            >
                <template #toolbar/>
            </vue-easy-lightbox>
        </template>
    </content-detail>
</template>

<script>
    import { Swiper, SwiperSlide } from 'swiper/vue';
    import {
        A11y, FreeMode, Mousewheel, Scrollbar
    } from 'swiper';
    import SectionHeader from '@/components/UI/SectionHeader';
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useClassesStore } from '@/store/Character/ClassesStore';
    import FieldSelect from '@/components/form/FieldType/FieldSelect';
    import SpellsView from "@/views/Spells/SpellsView";
    import errorHandler from "@/common/helpers/errorHandler";
    import OptionsView from "@/views/Character/Options/OptionsView";
    import RawContent from "@/components/content/RawContent";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'ClassDetail',
        components: {
            ContentDetail,
            RawContent,
            OptionsView,
            SpellsView,
            FieldSelect,
            SvgIcon,
            SectionHeader,
            Swiper,
            SwiperSlide,
        },
        async beforeRouteUpdate(to, from, next) {
            this.removeScrollListeners();

            await this.loadNewClass(to.path);

            next();
        },
        data: () => ({
            classesStore: useClassesStore(),
            loading: true,
            error: false,
            currentClass: undefined,
            currentTab: undefined,
            tabs: {
                list: [],
                modules: [FreeMode, Scrollbar, Mousewheel, A11y]
            },
            gallery: {
                show: false,
                index: 0,
            },
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile']),

            classes() {
                return this.classesStore.getClasses || []
            },

            getStoreKey() {
                return `${ this.currentClass.name.eng + this.currentTab.type + this.currentTab.order }`
                    .replaceAll(' ', '')
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
        beforeUnmount() {
            this.removeScrollListeners();
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
                this.tabs.list = loadedClass.tabs;

                if (loadedClass.images) {
                    this.tabs.list.push({
                        type: 'images',
                        order: this.tabs.length,
                        callback: () => this.showGallery
                    });
                }

                await this.setTab(0);
            },

            async setTab(index) {
                try {
                    this.loading = true;

                    this.currentTab = this.tabs.list[index];
                    this.loading = false;

                    this.$nextTick(() => {
                        if (this.$refs.classBody) {
                            this.$refs.classBody.scrollIntoView({
                                block: 'start',
                            });
                        }
                    })
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err)
                }
            },

            async clickTabHandler({ index, callback }) {
                if (typeof callback === 'function') {
                    callback();

                    return;
                }

                await this.setTab(index);
            },

            goToArchetype(path) {
                this.$router.push({ path })
            },

            initScrollListeners() {
                if (this.$route.hash) {
                    const section = this.$refs.classBody.querySelector(this.$route.hash);

                    if (!section) {
                        return;
                    }

                    this.$refs.classBody.scroll({
                        top: section.getBoundingClientRect().top - 119 - 56,
                        behavior: "smooth"
                    });
                }

                const links = this.$refs.classBody.querySelectorAll('[href^="#"]');

                for (const link of links) {
                    link.addEventListener('click', this.scrollToSection);
                }
            },

            removeScrollListeners() {
                if (!this.$refs.classBody) {
                    return;
                }

                const links = this.$refs.classBody.querySelectorAll('[href^="#"]');

                for (const link of links) {
                    link.removeEventListener('click', this.scrollToSection);
                }
            },

            scrollToSection(e) {
                if (e.button) {
                    return;
                }

                e.preventDefault();

                const { target } = e;
                const hash = target.getAttribute('href');

                if (!hash) {
                    return;
                }

                const section = this.$refs.classBody.querySelector(hash);

                if (!section) {
                    return;
                }

                window.history.pushState(null, null, hash);

                this.$refs.classBody.scroll({
                    top: section.getBoundingClientRect().top - 119 - 56,
                    behavior: "smooth"
                });
            },

            showGallery() {
                console.log(this.currentClass.images)
                if (!this.currentClass?.images?.length) {
                    return;
                }

                this.gallery.show = true;
                this.gallery.index = 0;
            },

            close() {
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
                width: 0 0 1px;
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

                @include media-max(800px) {
                    display: none;
                }
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

                    &_name {
                        display: block;
                    }
                }
            }

            @include media-max(1300px) {
                padding: 0 16px;
            }

            @include media-max(360px) {
                padding: 0 8px;
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
            max-height: 100%;
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
