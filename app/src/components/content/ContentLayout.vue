<template>
    <div
        ref="container"
        v-scroll-lock="showRightSide && getFullscreen"
        :class="{'is-showed-right-side': showRightSide}"
        class="content-layout"
    >
        <div
            :class="{ 'is-fullscreen': getFullscreen }"
            class="content-layout__body"
        >
            <div
                :class="{ 'is-fullscreen': getFullscreen, 'is-showed-right-side': showRightSide }"
                class="content-layout__list"
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
                </div>

                <div
                    v-if="$slots.fixed"
                    class="content-layout__fixed"
                >
                    <slot name="fixed"/>
                </div>

                <div
                    ref="items"
                    :class="{ 'is-shadow': shadow || (showRightSide || getFullscreen) }"
                    class="content-layout__items"
                >
                    <slot name="default"/>
                </div>
            </div>

            <div
                v-if="showRightSide"
                ref="detail"
                :class="{ 'is-fullscreen': getFullscreen }"
                class="content-layout__selected"
            >
                <router-view v-if="!$slots['right-side']"/>

                <slot name="right-side"/>
            </div>
        </div>
    </div>
</template>

<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { useInfiniteScroll, useResizeObserver } from "@vueuse/core/index";
    import ListFilter from "@/components/filter/ListFilter";
    import FilterService from "@/common/services/FilterService";
    import { mapState } from "pinia";
    import { ref } from "vue";

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
            },
        },
        emits: ['list-end', 'update', 'search', 'resize'],
        data: () => ({
            shadow: false,
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile', 'getFullscreen']),
        },
        watch: {
            showRightSide: {
                flush: 'post',
                handler(value) {
                    if (value && this.getFullscreen) {
                        this.$refs.detail.style.top = `${ window.scrollY }px`;
                    }
                }
            },
            getFullscreen: {
                handler(value) {
                    if (value) {
                        this.$refs.detail.style.top = `${ window.scrollY }px`;
                    }

                    if (!value) {
                        this.$refs.detail.style.top = '';
                    }
                }
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

            window.addEventListener('scroll', this.scrollHandler);

            useResizeObserver(this.$refs.items, this.scrollHandler);
        },
        beforeUnmount() {
            window.removeEventListener('scroll', this.scrollHandler);
        },
        methods: {
            scrollHandler() {
                this.toggleShadow();
            },

            toggleShadow() {
                this.shadow = window.scrollY + window.innerHeight < document.body.offsetHeight - 24
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

            &.is-fullscreen {
                border-radius: 12px;
            }

            &.is-showed-right-side {
                width: 100%;

                @include media-min($sm) {
                    width: 40%;
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

        &__items {
            margin-bottom: -24px;

            &:after {
                @include css_anim();

                content: '';
                display: block;
                pointer-events: none;
                width: calc(100% + 16px);
                height: 24px;
                box-shadow: 0 0 32px 32px var(--bg-main);
                position: sticky;
                bottom: 0;
                margin: 0 -16px;
                z-index: 16;
                opacity: 0;
                background-color: var(--bg-main);
                border: {
                    top-left-radius: 20%;
                    top-right-radius: 20%;
                };
            }

            &.is-shadow {
                &:after {
                    opacity: 1;
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
            margin-left: auto;
            z-index: 16;

            @media (max-width: 1200px) {
                width: 100%;
                height: calc(var(--max-vh) - 56px);
                position: fixed;
                top: 56px;
                left: 0;
                border-bottom-left-radius: 0;
                border-bottom-right-radius: 0;
            }

            &.is-fullscreen {
                width: 100%;
                max-width: 100%;
                height: calc(var(--max-vh) - 56px - 24px);
                position: absolute;
                top: 0;
                left: 0;
                margin: auto;

                @media (max-width: 1200px) {
                    height: calc(var(--max-vh) - 56px);
                }
            }
        }

        &__fixed {
            position: sticky;
            top: 56px;
            z-index: 3;
        }
    }
</style>
