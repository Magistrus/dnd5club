<template>
    <div class="filter">
        <div class="filter__body">
            <div class="filter__search">
                <label class="filter__search_field">
                    <span class="filter__search_field_icon">
                        <svg-icon icon-name="search"/>
                    </span>

                    <input
                        v-model="search"
                        placeholder="Поиск..."
                        type="text"
                        @input.prevent="$emit('search', search)"
                    >
                </label>

                <button
                    v-if="!!search"
                    class="filter__search_clear"
                    type="button"
                    @click.left.exact.prevent="search = ''"
                >
                    <svg-icon icon-name="close"/>
                </button>
            </div>

            <button
                v-if="filter"
                :class="{ 'is-opened': showed }"
                class="filter__button"
                type="button"
                @click.left.exact.prevent="showed = !showed"
            >
                <svg-icon
                    :fill-enable="true"
                    :icon-name="isFilterCustomized ? 'filter-customized' : 'filter'"
                    :stroke-enable="false"
                />

                <span>Фильтр</span>
            </button>

            <button
                v-if="!!filter && isFilterCustomized"
                v-tooltip.bottom-end="'Стереть фильтр'"
                class="filter__button"
                type="button"
                @click.left.exact.prevent="resetFilter"
            >
                <svg-icon icon-name="clear-filter"/>
            </button>
        </div>

        <div
            v-if="!!filter"
            v-show="showed"
            class="filter__dropdown"
        >
            <div class="filter__dropdown_body">
                <filter-item-sources
                    v-if="filter?.sources"
                    :model-value="filter.sources"
                    @update:model-value="setSourcesValue($event)"
                />

                <filter-item-checkboxes
                    v-for="(block, blockKey) in otherFilters"
                    :key="blockKey"
                    :model-value="block.values"
                    :name="block.name"
                    @update:model-value="setOtherValue($event, blockKey)"
                />
            </div>
        </div>
    </div>
</template>

<script>
    import _ from 'lodash';
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useFilterStore } from '@/store/FilterStore/FilterStore';
    import FilterItemSources from '@/components/filter/FilterItem/FilterItemSources';
    import FilterItemCheckboxes from '@/components/filter/FilterItem/FilterItemCheckboxes';

    export default {
        name: 'ListFilter',
        components: { FilterItemCheckboxes, FilterItemSources, SvgIcon },
        emits: ['clear-filter', 'search', 'update'],
        data: () => ({
            search: '',
            showed: false,
            filterStore: useFilterStore(),
        }),
        computed: {
            filter: {
                get() {
                    return this.filterStore?.getFilter || undefined;
                },

                async set(value) {
                    await this.filterStore.saveFilter(value);

                    this.$emit('update', this.filterStore.getQueryParams());
                }
            },

            otherFilters: {
                get() {
                    return this.filter?.other || this.filter || [];
                },

                set(value) {
                    if (this.filter?.other) {
                        this.filter = {
                            ...this.filter,
                            other: value
                        };

                        return
                    }

                    this.filter = value;
                }
            },

            isFilterCustomized() {
                return this.filterStore.isFilterCustomized;
            },
        },
        methods: {
            setSourcesValue(value) {
                this.filter = {
                    ...this.filter,
                    sources: value
                };
            },

            setOtherValue(value, index) {
                const otherFilters = _.cloneDeep(this.otherFilters);

                otherFilters[index].values = value;

                this.otherFilters = otherFilters;
            },

            async resetFilter() {
                await this.filterStore.resetFilter();
            },
        }
    }
</script>

<style lang="scss" scoped>
    .filter {
        position: relative;

        &__body {
            width: 100%;
            display: flex;
            position: relative;
            border: 1px solid var(--border);
            border-radius: 12px;
            overflow: hidden;
            background-color: var(--bg-secondary);
        }

        &__search {
            flex: 1;
            display: flex;

            &_field {
                flex: 1;
                display: flex;
                align-items: center;
                cursor: text;

                &_icon {
                    width: 42px;
                    height: 42px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    flex-shrink: 0;

                    svg {
                        width: 24px;
                        height: 24px;
                        color: var(--primary);
                    }
                }

                input {
                    width: 100%;
                    height: 100%;
                    border: 0;
                    background-color: transparent;
                    color: var(--text-color);
                    padding: 0;
                }
            }

            &_clear {
                @include css_anim();

                display: flex;
                align-items: center;
                justify-content: center;
                width: 42px;
                height: 42px;
                flex-shrink: 0;
                color: var(--primary);

                @include media-min($md) {
                    &:hover {
                        color: var(--text-btn-color);
                        background-color: var(--primary-hover);
                    }
                }

                svg {
                    width: 24px;
                    height: 24px;
                }
            }
        }

        &__button {
            @include css_anim();

            display: flex;
            flex-shrink: 0;
            align-items: center;
            justify-content: center;
            padding: 8px;
            border-left: 1px solid var(--border);

            svg {
                @include css_anim();

                width: 24px;
                height: 24px;
                color: var(--primary);
            }

            span {
                @include css_anim();

                margin-left: 4px;
                color: var(--text-color);
            }

            &.is-opened {
                @include css_anim();

                background-color: var(--primary-active);

                svg {
                    color: var(--text-btn-color);
                }

                span {
                    color: var(--text-btn-color);
                }
            }

            @include media-min($md) {
                &:hover {
                    @include css_anim();

                    background-color: var(--primary-hover);

                    svg {
                        color: var(--text-btn-color);
                    }

                    span {
                        color: var(--text-btn-color);
                    }
                }
            }
        }

        &__dropdown {
            position: absolute;
            top: calc(100% + 16px);
            background-color: var(--bg-sub-menu);
            width: 100%;
            max-height: calc(var(--max-vh) - 72px - 42px - 73px - 16px); // head, filter, menu, padding
            overflow: hidden auto;
            z-index: 10;
            border-radius: 12px;

            &_body {
                width: 100%;
                padding: 16px;
            }
        }
    }
</style>
