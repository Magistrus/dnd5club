<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: backgroundItem.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="backgroundItem"
            :class="getClassList(isActive)"
            :href="href"
            class="background-link"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="background-link__body">
                <div class="background-link__name">
                    <div class="background-link__name--rus">
                        {{ backgroundItem.name.rus }}
                    </div>

                    <div class="background-link__name--eng">
                        [{{ backgroundItem.name.eng }}]
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useBackgroundsStore } from "@/store/Character/BackgroundsStore";

    export default {
        name: 'BackgroundLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            backgroundItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            backgroundsStore: useBackgroundsStore(),
            background: {
                show: false,
                data: undefined
            }
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-background-selected': this.$route.name === 'backgroundDetail',
                    'is-green': this.backgroundItem?.homebrew
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.backgroundsStore.backgroundInfoQuery(this.backgroundItem.url)
                    .then(spell => {
                        this.background = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .background-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .background-link {
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
            .background-link {
                &__body {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            width: 100%;

            .background-link {
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
