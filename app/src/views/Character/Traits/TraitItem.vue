<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: traitItem.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="traitItem"
            :class="getClassList(isActive)"
            :href="href"
            class="trait-item"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="trait-item__body">
                <div class="trait-item__row">
                    <div class="trait-item__name">
                        <div class="trait-item__name--rus">
                            {{ traitItem.name.rus }}
                        </div>

                        <div class="trait-item__name--eng">
                            [{{ traitItem.name.eng }}]
                        </div>
                    </div>
                </div>

                <div class="trait-item__row">
                    <div class="trait-item__requirements">
                        {{ traitItem.requirements }}
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { useTraitsStore } from "@/store/CharacterStore/TraitsStore";

    export default {
        name: 'TraitItem',
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            traitItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            traitsStore: useTraitsStore(),
            trait: {
                show: false,
                data: undefined
            }
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-trait-selected': this.$route.name === 'traitDetail',
                    'is-green': this.traitItem?.homebrew
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.traitsStore.traitInfoQuery(this.traitItem.url)
                    .then(spell => {
                        this.trait = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .trait-item {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .trait-item {
                &__body {
                    background-color: var(--bg-homebrew-gradient-left);
                }
            }
        }

        &__body {
            display: flex;
            flex-direction: column;
            padding: 8px 10px;
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

        &__requirements {
            color: var(--text-g-color);
            font-size: calc(var(--main-font-size) - 1px);
            line-height: normal;
        }

        &:hover {
            .trait-item {
                &__content {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            width: 100%;

            .trait-item {
                &__body {
                    background-color: var(--primary-active);
                }

                &__school {
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
