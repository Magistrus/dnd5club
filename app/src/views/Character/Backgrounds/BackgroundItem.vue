<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: backgroundItem.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="backgroundItem"
            v-masonry-tile
            :class="getClassList(isActive)"
            :href="href"
            class="background-item"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="background-item__body">
                <div class="background-item__name">
                    <div class="background-item__name--rus">
                        {{ backgroundItem.name.rus }}
                    </div>

                    <div class="background-item__name--eng">
                        [{{ backgroundItem.name.eng }}]
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { useResizeObserver } from '@vueuse/core/index';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';

    export default {
        name: 'BackgroundItem',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            backgroundItem: {
                type: Object,
                default: () => ({})
            }
        },
        mounted() {
            this.$nextTick(() => {
                useResizeObserver(this.$refs.backgroundItem, this.updateGrid);
            });
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-background-selected': this.$route.name === 'backgroundDetail',
                    'is-green': this.backgroundItem?.homebrew
                }
            },

            updateGrid() {
                this.$nextTick(() => this.$redrawVueMasonry('background-items'))
            },
        }
    }
</script>

<style lang="scss" scoped>
    .background-item {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;

        @include media-min($md) {
            width: calc(50% - 6px);
        }

        @include media-min($xxl) {
            width: calc(100% / 3 - 12px * 2 / 3);
        }

        &.is-background-selected {
            width: 100%;
        }

        &.is-green {
            .background-item {
                &__body {
                    background-color: var(--bg-homebrew-gradient-left);
                }
            }
        }

        &__body {
            display: flex;
            flex-direction: row;
            align-items: center;
            padding: 8px 10px;
        }

        &__name {
            display: block;
            font-size: var(--main-font-size);
            font-weight: 500;

            &--rus,
            &--eng {
                display: inline;
                line-height: normal;
            }

            &--rus {
                color: var(--text-color-title);
            }

            &--eng {
                color: var(--text-g-color);
            }
        }

        &:hover {
            .background-item {
                &__body {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            width: 100%;

            .background-item {
                &__body {
                    background-color: var(--primary-active);
                }

                &__name {
                    &--rus,
                    &--eng {
                        color: var(--text-btn-color);
                    }
                }
            }
        }
    }
</style>
