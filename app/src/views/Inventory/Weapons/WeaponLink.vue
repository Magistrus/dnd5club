<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: weapon.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            class="weapon-link"
            :class="getClassList(isActive)"
            :href="href"
            @click.left.exact.prevent="clickHandler(navigate)"
        >
            <div
                v-if="weapon.name"
                class="weapon-link__row"
            >
                <div class="weapon-link__name">
                    <div class="weapon-link__name--rus">
                        {{ weapon.name.rus }}
                    </div>

                    <div
                        v-if="weapon.name.eng"
                        class="weapon-link__name--eng"
                    >
                        [{{ weapon.name.eng }}]
                    </div>
                </div>
            </div>

            <div class="weapon-link__row">
                <div
                    v-if="weapon.type?.name"
                    class="weapon-link__type"
                >
                    {{ weapon.type.name }}
                </div>

                <div
                    v-if="weapon.damage"
                    class="weapon-link__damage"
                >
                    <div
                        v-if="weapon.damage.dice"
                        v-tooltip="{ content: 'Урон' }"
                        class="weapon-link__damage_dice"
                    >
                        {{ weapon.damage.dice }}
                    </div>

                    <div
                        v-if="weapon.damage.type"
                        v-tooltip="{ content: 'Тип урона' }"
                        class="weapon-link__damage_type"
                    >
                        {{ weapon.damage.type }}
                    </div>
                </div>

                <div
                    v-if="weapon.price"
                    v-tooltip="{ content: 'Стоимость' }"
                    class="weapon-link__price"
                >
                    {{ weapon.price }}
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from "vue-router";

    export default {
        name: 'WeaponLink',
        props: {
            weapon: {
                type: Object,
                required: true,
                default: undefined
            },
            inTab: {
                type: Boolean,
                default: false
            },
            ...RouterLink.props,
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-green': this.weapon.homebrew,
                    'in-tab': this.inTab
                }
            },

            clickHandler(callback) {
                callback();
            }
        }
    }
</script>

<style lang="scss" scoped>
    .weapon-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;
        padding: 8px 10px;

        &.is-green {
            .weapon-link {
                background-color: var(--bg-homebrew-gradient-left);
            }
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

        &:hover {
            .weapon-link {
                background-color: var(--hover);
            }
        }

        &.router-link-active {
            background-color: var(--primary-active);

            .weapon-link {
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
