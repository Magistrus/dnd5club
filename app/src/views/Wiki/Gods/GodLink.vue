<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: god.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            ref="god"
            :class="getClassList(isActive)"
            :href="href"
            class="god-link"
            @click.left.exact.prevent="navigate()"
        >
            <div class="god-link__content">
                <div class="god-link__alignment">
                    <span>{{ god.alignment }}</span>
                </div>

                <div class="god-link__body">
                    {{ god.name.rus }} [{{ god.name.eng }}]
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useGodsStore } from "@/store/Wiki/GodsStore";

    export default {
        name: 'GodLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            god: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            godsStore: useGodsStore(),
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-green': this.god?.source?.homebrew,
                    'in-tab': this.inTab
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .god-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;
        color: var(--text-color-title);

        &.is-green {
            .god-link {
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

        &__alignment {
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
            font-size: var(--main-font-size);
            font-weight: 500;
            display: inline;
            line-height: normal;
        }

        &:hover {
            .god-link {
                &__content {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            .god-link {
                color: var(--text-btn-color);

                &__content {
                    background-color: var(--primary-active);
                }
            }
        }
    }
</style>
