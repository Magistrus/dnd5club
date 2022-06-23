<template>
    <div
        ref="layout"
        class="tab-layout"
    >
        <div
            v-if="filterInstance"
            ref="filter"
            class="tab-layout__filter"
        >
            <div class="tab-layout__filter_body">
                <list-filter
                    :in-tab="true"
                    :filter-instance="filterInstance"
                    @search="$emit('search', $event)"
                    @update="$emit('update', $event)"
                />
            </div>

            <div
                data-tab-filter
                class="tab-layout__filter_dropdown"
                :style="{ height: `${dropdownHeight}px` }"
            />
        </div>

        <div
            ref="items"
            class="tab-layout__items"
        >
            <div class="tab-layout__items--inner">
                <slot name="default"/>
            </div>
        </div>
    </div>
</template>

<script>

    import { useElementBounding, useInfiniteScroll, useResizeObserver } from "@vueuse/core/index";
    import FilterService from "@/common/services/FilterService";
    import ListFilter from "@/components/filter/ListFilter";

    export default {
        name: 'TabLayout',
        components: { ListFilter },
        props: {
            filterInstance: {
                type: FilterService,
                default: undefined
            }
        },
        emits: ['list-end', 'search', 'update'],
        data: () => ({
            dropdownHeight: 0,
            filterInstalled: false,
        }),
        updated() {
            if (!this.filterInstalled && this.$refs.filter) {
                this.filterInstalled = true;

                useResizeObserver(this.$refs.layout, this.calcDropdownHeight);
            }
        },
        mounted() {
            useInfiniteScroll(
                this.$refs.items,
                () => {
                    this.$emit('list-end');
                },
                { distance: 1080 }
            );
        },
        methods: {
            calcDropdownHeight() {
                if (this.$refs.filter && this.$refs.layout) {
                    const filterRect = useElementBounding(this.$refs.filter);
                    const layoutRect = useElementBounding(this.$refs.layout);

                    this.dropdownHeight = layoutRect.height.value - filterRect.height.value || 0;

                    return;
                }

                this.dropdownHeight = 0;
            }
        }
    }
</script>

<style lang="scss" scoped>
    .tab-layout {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        overflow: hidden;

        &__filter {
            flex-shrink: 0;
            position: relative;

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

            &--inner {
                padding: 24px;
            }
        }
    }
</style>
