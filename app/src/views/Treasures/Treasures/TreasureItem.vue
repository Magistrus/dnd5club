<template>
    <div
        ref="treasure"
        :class="{ 'is-green': treasure?.source?.homebrew, 'in-tab': inTab}"
        class="treasure-link"
    >
        <div class="treasure-link__body">
            <div class="treasure-link__row">
                <div class="treasure-link__name">
                    {{ treasure.name.rus }}
                </div>
            </div>

            <div class="treasure-link__row">
                <div
                    v-tooltip="{ content: treasure.source.name }"
                    class="treasure-link__source"
                >
                    {{ treasure.source.shortName }}
                </div>

                <div class="treasure-link__price">
                    {{ treasure.price }}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'TreasureItem',
        props: {
            treasure: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
    }
</script>

<style lang="scss" scoped>
    .treasure-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .treasure-link {
                &__body {
                    background-color: var(--bg-homebrew-gradient-left);
                }
            }
        }

        &__body {
            display: flex;
            flex-direction: column;
            padding: 8px 10px;
            width: 100%;
            color: var(--text-color-title);
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
            line-height: normal;
        }

        &__source {
            display: flex;
            margin-right: 8px;
        }

        &__price {
            display: flex;
            margin-left: auto;
        }

        &:hover {
            .treasure-link {
                &__body {
                    background-color: var(--hover);
                }
            }
        }
    }
</style>
