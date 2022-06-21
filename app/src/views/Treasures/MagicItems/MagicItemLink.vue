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
            class="link-item"
            @click.left.exact.prevent="clickHandler(navigate)"
        >
            <div class="link-item__content">
                <div
                    class="link-item__rarity"
                    :class="getRarityClass"
                >
                    <span>{{ getRarityAbbreviation }}</span>
                </div>

                <div class="link-item__body">
                    <div class="link-item__row">
                        <div class="link-item__name">
                            <div class="link-item__name--rus">
                                {{ magicItem.name.rus }}
                            </div>

                            <div class="link-item__name--eng">
                                [{{ magicItem.name.eng }}]
                            </div>
                        </div>
                    </div>

                    <div class="link-item__row">
                        <div
                            v-capitalize-first
                            class="link-item__type"
                        >
                            {{ magicItem.type.name }}
                        </div>

                        <div
                            v-if="magicItem.custom?.count"
                            class="link-item__count"
                        >
                            {{ `x${ magicItem.custom.count }` }}
                        </div>

                        <div class="link-item__price">
                            {{ `${ magicItem.custom?.price || magicItem.price || 0 } зм` }}
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
            },
            inTools: {
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

            clickHandler(callback) {
                if (this.inTools) {
                    this.$emit('select-item');

                    return;
                }

                callback();
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/styles/link-item";

    .link-item {
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

        &__type {
            color: var(--text-g-color);
            font-size: calc(var(--main-font-size) - 1px);
            line-height: normal;
        }

        &__count,
        &__price {
            margin-left: auto;
            display: block;
        }

        &.router-link-active {
            .link-item {
                &__rating,
                &__type,
                &__count,
                &__price {
                    color: var(--text-btn-color);
                }
            }
        }
    }
</style>
