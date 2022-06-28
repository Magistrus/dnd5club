<template>
    <div
        ref="container"
        :class="{'is-showed-right-side': showRightSide}"
        class="content-layout"
    >
        <div
            class="content-layout__body"
            :class="{ 'is-fullscreen': getFullscreen }"
        >
            <div
                class="content-layout__list"
                :class="{ 'is-fullscreen': getFullscreen, 'is-showed-right-side': showRightSide }"
            >
                <div
                    v-if="filterInstance"
                    class="content-layout__filter"
                >
                    <div class="content-layout__filter_body">
                        <list-filter
                            :filter-instance="filterInstance"
                            @search="$emit('search', $event)"
                            @update="$emit('update', $event)"
                        />
                    </div>

                    <div
                        data-content-filter
                        class="content-layout__filter_dropdown"
                        :style="{ height: `${dropdownHeight}px` }"
                    />
                </div>

                <div
                    v-if="$slots.fixed"
                    class="content-layout__fixed"
                >
                    <slot name="fixed"/>
                </div>

                <div
                    ref="items"
                    class="content-layout__items"
                >
                    <slot name="default"/>
                </div>
            </div>

            <div
                v-if="showRightSide"
                ref="detail"
                v-scroll-lock="showRightSide && (getIsMobile || getFullscreen)"
                class="content-layout__selected"
                :class="{ 'is-fullscreen': getFullscreen }"
            >
                <router-view v-if="!$slots['right-side']"/>

                <slot name="right-side"/>
            </div>
        </div>
    </div>
</template>

<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { useElementBounding, useInfiniteScroll, useResizeObserver } from "@vueuse/core/index";
    import ListFilter from "@/components/filter/ListFilter";
    import FilterService from "@/common/services/FilterService";
    import { mapState } from "pinia/dist/pinia";
    import { ref, unref } from "vue";

    export default {
        name: 'ContentLayout',
        components: { ListFilter },
        props: {
            showRightSide: {
                type: Boolean,
                default: false
            },
            filterInstance: {
                type: FilterService,
                default: undefined
            }
        },
        emits: ['list-end', 'update', 'search'],
        data: () => ({
            dropdownHeight: 0,
            filterInstalled: false,
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile', 'getFullscreen']),
        },
        watch: {
            getFullscreen() {
                this.setMaxWidth();
            }
        },
        mounted() {
            useInfiniteScroll(
                ref(window),
                () => {
                    this.$emit('list-end');
                },
                { distance: 1080 }
            );

            useResizeObserver(this.$refs.items, this.calcDropdownHeight);
            useResizeObserver(this.$refs.container, this.setMaxWidth);
        },
        methods: {
            calcDropdownHeight(entries) {
                const entry = entries[0]
                const { height } = entry.contentRect;

                this.dropdownHeight = height || 0;
            },

            setMaxWidth() {
                if (this.$refs.detail && this.$refs.container) {
                    this.$nextTick(() => {
                        if (!this.getFullscreen) {
                            this.$refs.detail.style.maxWidth = '';
                            this.$refs.detail.style.right = '';

                            return;
                        }

                        const conRect = useElementBounding(this.$refs.container);
                        const docRect = useElementBounding(ref(document.getElementById('dnd5club')));

                        this.$refs.detail.style.maxWidth = this.getIsMobile ? '100%' : `${ unref(conRect.width) }px`;
                        this.$refs.detail.style.left = !this.getIsMobile
                            ? `${ (unref(docRect.width) - unref(conRect.width)) / 2 }px`
                            : 0;
                        this.$refs.detail.style.right = 'initial';
                    })
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .content-layout {
        width: 100%;

        &__body {
            width: 100%;
            display: flex;
            justify-content: flex-start;
            position: relative;

            &.is-fullscreen {
                border-radius: 12px;
            }
        }

        &__list {
            display: flex;
            flex-direction: column;
            position: relative;
            width: 100%;
            flex-shrink: 0;

            ::-webkit-scrollbar-track {
                background-color: transparent;

                &:hover {
                    background-color: transparent;
                }
            }

            &.is-showed-right-side {
                width: 40%;

                @media (max-width: 1200px) {
                    width: 100%;
                }

                &.is-fullscreen {
                    width: 100%;
                }
            }
        }

        &__filter {
            flex-shrink: 0;
            position: sticky;
            margin-top: -56px;
            top: 0;
            padding-top: 56px;
            z-index: 12;
            pointer-events: none;
            background: linear-gradient(
                    180deg,
                    var(--bg-main) 0,
                    var(--bg-main) 88px,
                    var(--bg-main) 88px,
                    transparent 124px
            );

            &_body {
                padding-bottom: 24px;

                ::v-deep(*) {
                    pointer-events: auto;
                }
            }

            &_dropdown {
                position: absolute;
                top: 100%;
                left: 0;
                width: 100%;
                height: 100%;
                pointer-events: none;
                z-index: 12;

                ::v-deep(*) {
                    pointer-events: auto;
                }
            }
        }

        &__selected {
            display: block;
            top: 56px;
            right: 0;
            width: calc(60% - 10px);
            height: calc(var(--max-vh) - 56px - 24px);
            overflow: hidden;
            border-radius: 12px;
            background-color: var(--bg-secondary);
            position: sticky;
            z-index: 12;
            margin-left: auto;

            @media (max-width: 1200px) {
                width: 100%;
                height: calc(var(--max-vh) - 56px);
                position: fixed;
                top: 56px;
                left: 0;
                border-radius: 0;
            }

            &.is-fullscreen {
                width: 100%;
                height: calc(var(--max-vh) - 56px - 24px);
                position: fixed;
                top: 56px;
                left: 0;
                max-width: 1320px;
                margin: auto;
                border: {
                    bottom-left-radius: 0;
                    bottom-right-radius: 0;
                };

                    @media (max-width: 1400px) {
                        max-width: calc(1320px - 24px - 24px);
                    }
            }
        }
    }
</style>
