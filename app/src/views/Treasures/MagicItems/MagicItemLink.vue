<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: magicItem.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            ref="magicItem"
            :class="getClassList(isActive)"
            :href="href"
            class="magic-item-link"
            @click.left.exact.prevent="navigate()"
        >
            <div class="magic-item-link__content">
                <div
                    class="magic-item-link__rarity"
                    :class="getRarityClass"
                >
                    <span>{{ getRarityAbbreviation }}</span>
                </div>

                <div class="magic-item-link__body">
                    <div class="magic-item-link__row">
                        <div class="magic-item-link__name">
                            <div class="magic-item-link__name--rus">
                                {{ magicItem.name.rus }}
                            </div>

                            <div class="magic-item-link__name--eng">
                                [{{ magicItem.name.eng }}]
                            </div>
                        </div>
                    </div>

                    <div class="magic-item-link__row">
                        <div
                            v-capitalize-first
                            class="magic-item-link__type"
                        >
                            {{ magicItem.type.name }}
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
    import { useMagicItemsStore } from "@/store/Treasures/MagicItemsStore";

    export default {
        name: 'MagicItemLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            magicItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            magicItemsStore: useMagicItemsStore(),
        }),
        computed: {
            getRarityAbbreviation() {
                const words = this.magicItem.rarity.split(' ');

                let abbreviation = '';

                for (const word of words) {
                    if (!word) {
                        continue;
                    }

                    abbreviation += word[0].toUpperCase();
                }

                return abbreviation;
            },

            getRarityClass() {
                switch (this.magicItem.rarity) {
                    case 'артефакт':
                        return 'is-artifact'
                    case 'легендарный':
                        return 'is-legendary'
                    case 'очень редкий':
                        return 'is-very-rare'
                    case 'редкий':
                        return 'is-rare'
                    case 'необычный':
                        return 'is-uncommon'
                    case 'обычный':
                        return 'is-common'
                    default:
                        return 'is-unknown'
                }
            }
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-green': this.magicItem?.source?.homebrew,
                    'in-tab': this.inTab
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .magic-item-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .magic-item-link {
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

        &__rarity {
            width: 42px;
            height: 42px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            font-size: 17px;
            color: var(--text-color);
            border-right: 1px solid var(--border);
            position: relative;

            &:after {
                content: '';
                position: absolute;
                background: var(--border);
                border: 1px solid var(--border);
                border-radius: 50%;
                width: 11px;
                height: 11px;
                display: block;
                top: 50%;
                right: 0;
                z-index: 1;
                box-shadow: 0 0 1px 1px #0006;
                transform: translateY(-50%) translateX(50%);
            }

            &.is-common {
                &:after {
                    background-color: var(--common);
                }
            }

            &.is-uncommon {
                &:after {
                    background-color: var(--uncommon);
                }
            }

            &.is-rare {
                &:after {
                    background-color: var(--rare);
                }
            }

            &.is-very-rare {
                &:after {
                    background-color: var(--very_rare);
                }
            }

            &.is-legendary {
                &:after {
                    background-color: var(--legendary);
                }
            }

            &.is-artifact {
                &:after {
                    background-color: var(--artifact);
                }
            }
        }

        &__body {
            flex: 1 1 100%;
            padding-left: 16px;
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
            .magic-item-link {
                &__content {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            .magic-item-link {
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
