<template>
    <div
        ref="layout"
        class="tab-layout"
    >
        <div
            ref="filter"
            class="tab-layout__filter"
        >
            <div
                v-if="$slots.filter"
                class="tab-layout__filter_body"
            >
                <slot name="filter"/>
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
                <slot name="items"/>
            </div>
        </div>
    </div>
</template>

<script>

    import { useInfiniteScroll } from "@vueuse/core/index";

    export default {
        name: 'TabLayout',
        emits: ['list-end'],
        data: () => ({
            filterUpdated: false,
            dropdownHeight: 0
        }),
        updated() {
            if (!this.filterUpdated && this.$refs.layout && this.$refs.filter && this.$slots.filter) {
                this.calcDropdownHeight();

                this.filterUpdated = true;
            }
        },
        mounted() {
            this.calcDropdownHeight();

            window.addEventListener('resize', this.calcDropdownHeight);

            useInfiniteScroll(
                this.$refs.items,
                () => {
                    this.$emit('list-end');
                },
                {
                    distance: 250
                }
            );
        },
        beforeUnmount() {
            window.removeEventListener('resize', this.calcDropdownHeight);
        },
        methods: {
            calcDropdownHeight() {
                if (this.$refs.filter && this.$refs.layout) {
                    const { filter, layout } = this.$refs;
                    const filterRect = filter.getBoundingClientRect();
                    const layoutRect = layout.getBoundingClientRect();

                    this.dropdownHeight = layoutRect.height - filterRect.height;
                }
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
