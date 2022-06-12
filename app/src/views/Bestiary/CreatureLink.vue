<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: creature.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            ref="creature"
            :class="getClassList(isActive)"
            :href="href"
            class="creature-link"
            @click.left.exact.prevent="navigate()"
        >
            <div class="creature-link__content">
                <div class="creature-link__rating">
                    <span>{{ 'challengeRating' in creature ? creature.challengeRating : '-' }}</span>
                </div>

                <div class="creature-link__body">
                    <div class="creature-link__row">
                        <div class="creature-link__name">
                            <div class="creature-link__name--rus">
                                {{ creature.name.rus }}
                            </div>

                            <div class="creature-link__name--eng">
                                [{{ creature.name.eng }}]
                            </div>
                        </div>
                    </div>

                    <div class="creature-link__row">
                        <div
                            v-capitalize-first
                            class="creature-link__type"
                        >
                            {{ creature.type }}
                        </div>
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useBestiaryStore } from "@/store/Bestiary/BestiaryStore";

    export default {
        name: 'CreatureLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            creature: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            bestiaryStore: useBestiaryStore(),
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-green': this.creature?.source?.homebrew,
                    'in-tab': this.inTab
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .creature-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .creature-link {
                &__content {
                    background-color: var(--bg-homebrew-gradient-left);
                }
            }
        }

        &__content {
            display: flex;
            flex-direction: row;
            padding: 8px 10px;
            width: 100%;
        }

        &__rating {
            width: 42px;
            height: 42px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            font-size: 17px;
            color: var(--text-color);
        }

        &__body {
            flex: 1 1 100%;
            padding-left: 16px;
            border-left: 1px solid var(--border);
        }

        &__row {
            display: flex;
            align-items: center;

            & + & {
                margin-top: 4px;
            }
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

        &__type {
            color: var(--text-g-color);
            font-size: calc(var(--main-font-size) - 1px);
            line-height: normal;
        }

        &:hover {
            .creature-link {
                &__content {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            .creature-link {
                &__content {
                    background-color: var(--primary-active);
                }

                &__rating,
                &__type {
                    color: var(--text-btn-color);
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
