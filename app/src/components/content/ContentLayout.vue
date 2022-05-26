<template>
    <div
        class="content-layout"
        :class="layoutClasses"
    >
        <div class="content-layout__body">
            <div class="content-layout__list">
                <div
                    v-if="$slots.filter"
                    class="content-layout__filter"
                >
                    <slot name="filter"/>
                </div>

                <div class="content-layout__items">
                    <slot name="items"/>
                </div>
            </div>

            <div
                v-if="showRightSide"
                class="content-layout__selected"
            >
                <router-view/>
            </div>
        </div>
    </div>
</template>

<script>
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'ContentLayout',
        props: {
            showRightSide: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            uiStore: useUIStore(),
        }),
        computed: {
            layoutClasses() {
                return {
                    'is-showed-right-side': this.showRightSide,
                    'is-fullscreen': this.uiStore.getContentConfig.fullscreen,
                    'is-small': !this.uiStore.getContentConfig.fullscreen,
                }
            }
        },
    }
</script>

<style lang="scss" scoped>
    .content-layout {
        width: 100%;
        height: var(--max-vh);
        padding: 24px;

        &__body {
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: flex-start;
            position: relative;
        }

        &__list {
            width: 100%;
            display: flex;
            flex-direction: column;
            overflow: auto;
        }

        &__filter {
            flex-shrink: 0;
            padding-bottom: 24px;
        }

        &__items {
            flex: 1 1 100%;
            overflow: auto;
        }

        &__selected {
            width: calc(60% - 24px);
            height: 100%;
            margin-left: 24px;
            overflow: hidden;
            border-radius: 12px;
        }

        &.is-small {
            &.is-showed-right-side {
                .content-layout {
                    &__list {
                        width: 40%;
                    }
                }
            }
        }

        &.is-fullscreen {
            .content-layout {
                &__body {
                    border-radius: 12px;
                    overflow: hidden;
                }

                &__selected {
                    position: absolute;
                    width: 100%;
                    margin-left: 0;
                }
            }

            &.is-showed-right-side {
            }
        }
    }
</style>
