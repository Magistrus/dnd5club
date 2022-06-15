<template>
    <div
        :class="layoutClasses"
        class="content-layout"
    >
        <div class="content-layout__body">
            <div
                id="left_block"
                ref="list"
                class="content-layout__list"
            >
                <div
                    v-if="filterInstance"
                    ref="filter"
                    class="content-layout__filter"
                >
                    <div class="content-layout__filter_body">
                        <list-filter
                            :filter-instance="filterInstance"
                            @search="$emit('search', $event)"
                            @update="$emit('update', $event)"
                        />
                    </div>

                    <div
                        data-content-filter
                        class="content-layout__filter_dropdown"
                        :style="{ height: `${dropdownHeight}px` }"
                    />
                </div>

                <div
                    ref="items"
                    class="content-layout__items"
                >
                    <slot name="default"/>
                </div>
            </div>

            <div
                v-if="showRightSide"
                id="right_block"
                class="content-layout__selected"
            >
                <router-view/>
            </div>
        </div>
    </div>
</template>

<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { useElementBounding, useInfiniteScroll, useResizeObserver } from "@vueuse/core/index";
    import ListFilter from "@/components/filter/ListFilter";
    import FilterService from "@/services/FilterService";

    export default {
        name: 'ContentLayout',
        components: { ListFilter },
        props: {
            showRightSide: {
                type: Boolean,
                default: false
            },
            filterInstance: {
                type: FilterService,
                default: undefined
            }
        },
        emits: ['list-end', 'update', 'search'],
        data: () => ({
            uiStore: useUIStore(),
            dropdownHeight: 0,
            filterInstalled: false,
        }),
        computed: {
            layoutClasses() {
                return {
                    'is-showed-right-side': this.showRightSide,
                    'is-fullscreen': this.uiStore.getContentConfig.fullscreen,
                    'is-small': !this.uiStore.getContentConfig.fullscreen,
                }
            },
        },
        updated() {
            if (!this.filterInstalled && this.$refs.filter) {
                this.filterInstalled = true;

                useResizeObserver(this.$refs.list, this.calcDropdownHeight);
            }
        },
        mounted() {
            useInfiniteScroll(
                this.$refs.items,
                () => {
                    this.$emit('list-end');
                }
            );
        },
        methods: {
            calcDropdownHeight() {
                if (this.$refs.filter && this.$refs.list) {
                    const filterRect = useElementBounding(this.$refs.filter);
                    const listRect = useElementBounding(this.$refs.list);

                    this.dropdownHeight = listRect.height.value - filterRect.height.value || 0;

                    return;
                }

                this.dropdownHeight = 0;
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

        &__list#left_block {
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

        &__selected#right_block {
            display: block;
            top: 0;
            right: 0;
            width: calc(60% - 48px);
            height: calc(100% - 48px);
            margin: 24px;
            overflow: hidden;
            border-radius: 12px;
            background-color: var(--bg-secondary);
        }

        &.is-small {
            &.is-showed-right-side {
                .content-layout {
                    &__list#left_block {
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

                &__selected#right_block {
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