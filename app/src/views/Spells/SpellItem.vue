<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: spellItem.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            ref="spellItem"
            :class="getClassList(isActive)"
            :href="href"
            class="spell-item"
            @click.left.exact.prevent="clickHandler(navigate)"
        >
            <div class="spell-item__content">
                <div
                    v-tooltip="{ content: spellItem.level ? `${spellItem.level} уровень заклинания` : 'Заговор' }"
                    class="spell-item__lvl"
                >
                    <span>{{ spellItem.level || '◐' }}</span>
                </div>

                <div class="spell-item__body">
                    <div class="spell-item__row">
                        <div class="spell-item__name">
                            <div class="spell-item__name--rus">
                                {{ spellItem.name.rus }}
                            </div>

                            <div class="spell-item__name--eng">
                                [{{ spellItem.name.eng }}]
                            </div>
                        </div>
                    </div>

                    <div class="spell-item__row">
                        <div
                            v-if="spellItem.concentration || spellItem.ritual"
                            class="spell-item__modifications"
                        >
                            <div
                                v-if="spellItem.concentration"
                                v-tooltip="{ content: 'Концентрация' }"
                                class="spell-item__modification"
                            >
                                К
                            </div>

                            <div
                                v-if="spellItem.ritual"
                                v-tooltip="{ content: 'Ритуал' }"
                                class="spell-item__modification"
                            >
                                Р
                            </div>
                        </div>

                        <div
                            v-capitalize-first
                            class="spell-item__school"
                        >
                            {{ spellItem.school }}
                        </div>

                        <div
                            v-if="hasComponents"
                            class="spell-item__components"
                        >
                            <div
                                v-if="spellItem.components.v"
                                v-tooltip="{ content: 'Вербальный' }"
                                class="spell-item__component"
                            >
                                В
                            </div>

                            <div
                                v-if="spellItem.components.s"
                                v-tooltip="{ content: 'Соматический' }"
                                class="spell-item__component"
                            >
                                С
                            </div>

                            <div
                                v-if="!!spellItem.components.m"
                                v-tooltip="{ content: 'Материальный' }"
                                class="spell-item__component"
                            >
                                М
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </a>
    </router-link>

    <base-modal
        v-if="spell.data"
        v-model="spell.show"
    >
        <template #title>
            {{ spell.data.name.rus }}
        </template>

        <template #default>
            <spell-body :spell="spell.data"/>
        </template>
    </base-modal>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useSpellsStore } from "@/store/SpellsStore/SpellsStore";
    import SpellBody from "@/views/Spells/SpellBody";
    import BaseModal from "@/components/UI/BaseModal";

    export default {
        name: 'SpellItem',
        components: { BaseModal, SpellBody },
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            spellItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            spellsStore: useSpellsStore(),
            spell: {
                show: false,
                data: undefined
            }
        }),
        computed: {
            hasComponents() {
                const { spellItem } = this;

                return spellItem?.components?.v || spellItem?.components?.s || !!spellItem?.components?.m
            },
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-green': this.spellItem?.source?.homebrew,
                    'in-tab': this.inTab
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.spellsStore.spellInfoQuery(this.spellItem.url)
                    .then(spell => {
                        this.spell = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .spell-item {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .spell-item {
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

        &__lvl {
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

        &__modifications {
            display: flex;
            margin-right: 8px;
        }

        &__modification {
            padding: 0 3px;
            border-radius: 4px;
            background-color: var(--primary);
            color: var(--text-btn-color);
            font-size: calc(var(--main-font-size) - 1px);
            line-height: normal;
        }

        &__school {
            color: var(--text-g-color);
            font-size: calc(var(--main-font-size) - 1px);
            line-height: normal;
        }

        &__components {
            display: flex;
            margin-left: auto;
        }

        &__component {
            font-size: calc(var(--main-font-size) - 1px);
            line-height: normal;
            color: var(--text-color);

            & + & {
                margin-left: 4px;
            }
        }

        &:hover {
            .spell-item {
                &__content {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            .spell-item {
                &__content {
                    background-color: var(--primary-active);
                }

                &__lvl,
                &__modification,
                &__school,
                &__component {
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
