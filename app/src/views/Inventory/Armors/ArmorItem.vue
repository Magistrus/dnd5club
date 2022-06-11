<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: armor.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            class="armor-item"
            :class="getClassList(isActive)"
            :href="href"
            @click.left.exact.prevent="clickHandler(navigate)"
        >
            <div
                v-if="armor.name"
                class="armor-item__row"
            >
                <div class="armor-item__name">
                    <div class="armor-item__name--rus">
                        {{ armor.name.rus }}
                    </div>

                    <div
                        v-if="armor.name.eng"
                        class="armor-item__name--eng"
                    >
                        [{{ armor.name.eng }}]
                    </div>
                </div>
            </div>

            <div class="armor-item__row">
                <div
                    v-if="armor.type?.name"
                    class="armor-item__type"
                >
                    {{ armor.type.name }}
                </div>

                <div
                    v-if="armor.armorClass"
                    v-tooltip="{ content: 'Класс доспеха (АС)' }"
                    class="armor-item__armor-class"
                >
                    {{ armor.armorClass }}
                </div>

                <div
                    v-if="armor.price"
                    v-tooltip="{ content: 'Стоимость' }"
                    class="armor-item__price"
                >
                    {{ armor.price }}
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from "vue-router";

    export default {
        name: "ArmorItem",
        props: {
            armor: {
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
                    'is-green': this.armor.homebrew,
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
    .armor-item {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;
        padding: 8px 10px;

        &.is-green {
            .armor-item {
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
            .armor-item {
                background-color: var(--hover);
            }
        }

        &.router-link-active {
            background-color: var(--primary-active);

            .armor-item {
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
