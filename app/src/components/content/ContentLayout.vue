<template>
    <div
        :class="{'is-showed-right-side': showRightSide}"
        class="content-layout"
    >
        <div
            class="content-layout__body"
            :class="{ 'is-fullscreen': getFullscreen }"
        >
            <div
                ref="list"
                class="content-layout__list"
                :class="{ 'is-fullscreen': getFullscreen, 'is-showed-right-side': showRightSide }"
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
                    v-if="$slots.fixed"
                    class="content-layout__fixed"
                >
                    <slot name="fixed"/>
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
                class="content-layout__selected"
                :class="{ 'is-fullscreen': getFullscreen }"
            >
                <router-view v-if="!$slots['right-side']"/>

                <slot name="right-side"/>
            </div>
        </div>
    </div>
</template>

<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { useInfiniteScroll, useResizeObserver } from "@vueuse/core/index";
    import ListFilter from "@/components/filter/ListFilter";
    import FilterService from "@/common/services/FilterService";
    import { mapState } from "pinia/dist/pinia";
    import { ref } from "vue";

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
            dropdownHeight: 0,
            filterInstalled: false,
            scrollTop: 0
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile', 'getFullscreen']),
        },
        mounted() {
            useInfiniteScroll(
                ref(window),
                () => {
                    this.$emit('list-end');
                },
                { distance: 1080 }
            );

            useResizeObserver(this.$refs.items, this.calcDropdownHeight);
        },
        methods: {
            calcDropdownHeight(entries) {
                const entry = entries[0]
                const { height } = entry.contentRect;

                this.dropdownHeight = height || 0;
            },
        }
    }
</script>

<style lang="scss" scoped>
    .content-layout {
        width: 100%;

        &__body {
            width: 100%;
            display: flex;
            justify-content: flex-start;
            position: relative;

            &.is-fullscreen {
                border-radius: 12px;
            }
        }

        &__list {
            display: flex;
            flex-direction: column;
            overflow: hidden;
            position: relative;
            width: 100%;
            flex-shrink: 0;

            ::-webkit-scrollbar-track {
                background-color: transparent;

                &:hover {
                    background-color: transparent;
                }
            }

            &.is-showed-right-side:not(.is-fullscreen) {
                width: 40%;

                @media (max-width: 1200px) {
                    width: 100%;
                    height: calc(var(--max-vh) - 56px - 24px);
                    border-radius: 12px;
                }
            }

            &.is-showed-right-side.is-fullscreen {
                height: calc(var(--max-vh) - 56px - 24px);
                border-radius: 12px;
            }
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
                z-index: 12;

                ::v-deep(*) {
                    pointer-events: auto;
                }
            }
        }

        &__selected {
            display: block;
            top: 56px;
            right: 0;
            width: calc(60% - 10px);
            height: calc(var(--max-vh) - 56px - 24px);
            overflow: hidden;
            border-radius: 12px;
            background-color: var(--bg-secondary);
            position: sticky;
            z-index: 12;
            margin-left: auto;

            @media (max-width: 1200px) {
                width: 100%;
                position: absolute;
                top: 0;
            }

            &.is-fullscreen {
                top: 0;
                width: 100%;
                position: absolute;
            }
        }
    }
</style>
