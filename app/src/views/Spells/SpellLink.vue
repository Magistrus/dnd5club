<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: spell.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            ref="spell"
            :class="getClassList(isActive)"
            :href="href"
            class="spell-link"
            @click.left.exact.prevent="clickHandler(navigate)"
        >
            <div class="spell-link__content">
                <div
                    v-tooltip="{ content: spell.level ? `${spell.level} уровень заклинания` : 'Заговор' }"
                    class="spell-link__lvl"
                >
                    <span>{{ spell.level || '◐' }}</span>
                </div>

                <div class="spell-link__body">
                    <div class="spell-link__row">
                        <div class="spell-link__name">
                            <div class="spell-link__name--rus">
                                {{ spell.name.rus }}
                            </div>

                            <div class="spell-link__name--eng">
                                [{{ spell.name.eng }}]
                            </div>
                        </div>
                    </div>

                    <div class="spell-link__row">
                        <div
                            v-if="spell.concentration || spell.ritual"
                            class="spell-link__modifications"
                        >
                            <div
                                v-if="spell.concentration"
                                v-tooltip="{ content: 'Концентрация' }"
                                class="spell-link__modification"
                            >
                                К
                            </div>

                            <div
                                v-if="spell.ritual"
                                v-tooltip="{ content: 'Ритуал' }"
                                class="spell-link__modification"
                            >
                                Р
                            </div>
                        </div>

                        <div
                            v-capitalize-first
                            class="spell-link__school"
                        >
                            {{ spell.school }}
                        </div>

                        <div
                            v-if="hasComponents"
                            class="spell-link__components"
                        >
                            <div
                                v-if="spell.components.v"
                                v-tooltip="{ content: 'Вербальный' }"
                                class="spell-link__component"
                            >
                                В
                            </div>

                            <div
                                v-if="spell.components.s"
                                v-tooltip="{ content: 'Соматический' }"
                                class="spell-link__component"
                            >
                                С
                            </div>

                            <div
                                v-if="!!spell.components.m"
                                v-tooltip="{ content: 'Материальный' }"
                                class="spell-link__component"
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
        v-if="spellModal.data"
        v-model="spellModal.show"
    >
        <template #title>
            {{ spellModal.data.name.rus }}
        </template>

        <template #default>
            <spell-body :spell="spellModal.data"/>
        </template>
    </base-modal>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useSpellsStore } from "@/store/Spells/SpellsStore";
    import SpellBody from "@/views/Spells/SpellBody";
    import BaseModal from "@/components/UI/BaseModal";

    export default {
        name: 'SpellLink',
        components: { BaseModal, SpellBody },
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            spell: {
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
            spellModal: {
                show: false,
                data: undefined
            }
        }),
        computed: {
            hasComponents() {
                const { spell } = this;

                return spell?.components?.v || spell?.components?.s || !!spell?.components?.m
            },
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-green': this.spell?.source?.homebrew,
                    'in-tab': this.inTab
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.spellsStore.spellInfoQuery(this.spell.url)
                    .then(spell => {
                        this.spellModal = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .spell-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .spell-link {
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
            border-right: 1px solid var(--border);
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
            .spell-link {
                &__content {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            .spell-link {
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
