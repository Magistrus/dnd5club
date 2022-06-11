<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: optionItem.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="optionItem"
            :class="getClassList(isActive)"
            :href="href"
            class="option-item"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="option-item__body">
                <div class="option-item__name">
                    <div class="option-item__name--rus">
                        {{ optionItem.name.rus }}
                    </div>

                    <div class="option-item__name--eng">
                        [{{ optionItem.name.eng }}]
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useOptionsStore } from "@/store/Character/OptionsStore";

    export default {
        name: 'OptionItem',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            optionItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            optionsStore: useOptionsStore(),
            option: {
                show: false,
                data: undefined
            }
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-option-selected': this.$route.name === 'optionDetail',
                    'is-green': this.optionItem?.homebrew
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.optionsStore.optionInfoQuery(this.optionItem.url)
                    .then(spell => {
                        this.option = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .option-item {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .option-item {
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
            .option-item {
                &__body {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            width: 100%;

            .option-item {
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
