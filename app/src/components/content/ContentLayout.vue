<template>
    <div
        :class="layoutClasses"
        class="content-layout"
    >
        <div class="content-layout__body">
            <div
                ref="list"
                class="content-layout__list"
            >
                <div
                    ref="filter"
                    class="content-layout__filter"
                >
                    <div
                        v-if="$slots.filter"
                        class="content-layout__filter_body"
                    >
                        <slot name="filter"/>
                    </div>

                    <div
                        data-content-filter
                        class="content-layout__filter_dropdown"
                        :style="{ height: `${dropdownHeight}px` }"
                    />
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
            filterUpdated: false,
            dropdownHeight: 0
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
        updated() {
            if (!this.filterUpdated && this.$refs.list && this.$refs.filter && this.$slots.filter) {
                this.calcDropdownHeight();

                this.filterUpdated = true;
            }
        },
        mounted() {
            this.calcDropdownHeight();

            window.addEventListener('resize', this.calcDropdownHeight);
        },
        beforeUnmount() {
            window.removeEventListener('resize', this.calcDropdownHeight);
        },
        methods: {
            calcDropdownHeight() {
                if (this.$refs.filter && this.$refs.list) {
                    const { filter, list } = this.$refs;
                    const filterRect = filter.getBoundingClientRect();
                    const listRect = list.getBoundingClientRect();

                    this.dropdownHeight = listRect.height - filterRect.height;
                }
            }
        }
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
        }

        &__list {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }

        &__filter {
            flex-shrink: 0;
            position: relative;

            &_body {
                padding-bottom: 24px;
            }

            &_dropdown {
                position: absolute;
                top: 100%;
                left: 0;
                width: 100%;
                height: 100%;
                pointer-events: none;
                z-index: 10;

                ::v-deep(*) {
                    pointer-events: auto;
                }
            }
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
