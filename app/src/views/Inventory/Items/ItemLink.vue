<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: itemItem.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="itemItem"
            :class="getClassList(isActive)"
            :href="href"
            class="item-link"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="item-link__body">
                <div class="item-link__name">
                    <div class="item-link__name--rus">
                        {{ itemItem.name.rus }}
                    </div>

                    <div class="item-link__name--eng">
                        [{{ itemItem.name.eng }}]
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useItemsStore } from "@/store/Inventory/ItemsStore";

    export default {
        name: 'ItemLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            itemItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            itemsStore: useItemsStore(),
            item: {
                show: false,
                data: undefined
            }
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-item-selected': this.$route.name === 'itemDetail',
                    'is-green': this.itemItem?.homebrew
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.itemsStore.itemInfoQuery(this.itemItem.url)
                    .then(spell => {
                        this.item = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .item-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .item-link {
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
            .item-link {
                &__body {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            width: 100%;

            .item-link {
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
