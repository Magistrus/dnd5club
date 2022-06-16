<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: rule.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="ruleItem"
            :class="getClassList(isActive)"
            :href="href"
            class="rule-link"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="rule-link__body">
                <div class="rule-link__name">
                    <div class="rule-link__name--rus">
                        {{ rule.name.rus }}
                    </div>

                    <div class="rule-link__name--eng">
                        [{{ rule.name.eng }}]
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useRulesStore } from "@/store/Wiki/RulesStore";

    export default {
        name: 'RuleLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            rule: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            rulesStore: useRulesStore(),
            modal: {
                show: false,
                data: undefined
            }
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-rule-selected': this.$route.name === 'ruleDetail',
                    'is-green': this.rule?.source?.homebrew
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.rulesStore.ruleInfoQuery(this.rule.url)
                    .then(spell => {
                        this.modal = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .rule-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .rule-link {
                &__body {
                    background-color: var(--bg-homebrew-gradient-left);
                }
            }
        }

        &__body {
            display: flex;
            flex-direction: row;
            align-rules: center;
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
            .rule-link {
                &__body {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            width: 100%;

            .rule-link {
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
