<template>
    <div
        class="filter"
        :class="{ 'in-tab': inTab }"
    >
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
                    v-tooltip.bottom="{ content: 'Стереть строку поиска' }"
                    class="filter__search_clear"
                    type="button"
                    @click.left.exact.prevent="search = ''"
                >
                    <svg-icon icon-name="close"/>
                </button>
            </div>

            <button
                v-if="filter"
                v-tooltip.bottom="{ content: showed ? 'Скрыть фильтры' : 'Показать фильтры' }"
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
                v-tooltip.bottom-end="'Сбросить все фильтры'"
                class="filter__button"
                type="button"
                @click.left.exact.prevent="resetFilter"
            >
                <svg-icon icon-name="clear-filter"/>
            </button>
        </div>

        <teleport
            v-if="!!filter"
            :to="inTab ? '[data-tab-filter]' : '[data-content-filter]'"
        >
            <div
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
        </teleport>
    </div>
</template>

<script>
    import _ from 'lodash';
    import SvgIcon from '@/components/UI/SvgIcon';
    import FilterItemSources from '@/components/filter/FilterItem/FilterItemSources';
    import FilterItemCheckboxes from '@/components/filter/FilterItem/FilterItemCheckboxes';
    import FilterService from "@/services/FilterService";

    export default {
        name: 'ListFilter',
        components: { FilterItemCheckboxes, FilterItemSources, SvgIcon },
        props: {
            filterInstance: {
                type: FilterService,
                default: undefined,
                required: true
            },
            inTab: {
                type: Boolean,
                default: false
            },
        },
        emits: ['clear-filter', 'search', 'update'],
        data: () => ({
            search: '',
            showed: false,
        }),
        computed: {
            filter: {
                get() {
                    return this.filterInstance?.getState || undefined;
                },

                async set(value) {
                    await this.filterInstance.save(value);

                    this.$emit('update', this.filterInstance.getQueryParams);
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
                return this.filterInstance.isCustomized;
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
                await this.filterInstance.reset();
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
            overflow: hidden;
            background-color: var(--bg-secondary);
            border-bottom: 1px solid var(--border);
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
            background-color: var(--bg-sub-menu);
            width: 100%;
            max-height: 100%;
            overflow: hidden auto;
            border-radius: 12px;

            &_body {
                width: 100%;
                padding: 16px;
            }
        }

        &:not(.in-tab) {
            .filter {
                &__body {
                    border: 1px solid var(--border);
                    border-radius: 12px;
                }

                &__dropdown {
                    border-radius: 0;
                }
            }
        }
    }
</style>
