<template>
    <div
        v-bind="$attrs"
        class="dnd5club-select"
    >
        <multiselect
            v-bind="$props"
            @select="onSelect"
            @remove="onRemove"
            @search-change="onSearch"
            @tag="onTag"
            @open="onOpen"
            @close="onClose"
        >
            <template #option="{option, search}">
                <slot
                    name="option"
                    :option="option"
                    :search="search"
                />
            </template>
            <template #maxElements>
                <slot name="maxElements">
                    Ты выбрал максимальное количество
                </slot>
            </template>
            <template #noResult>
                <slot name="noResult">
                    Боги не знают ответа на твой запрос
                </slot>
            </template>
            <template #noOptions>
                <slot name="noOptions">
                    Не всегда есть выбор
                </slot>
            </template>
            <template #beforeList>
                <slot name="beforeList"/>
            </template>
            <template #afterList>
                <slot name="afterList"/>
            </template>
            <template #caret="{ toggle }">
                <slot
                    name="caret"
                    :toggle="toggle"
                >
                    <div class="multiselect__select">
                        <svg-icon icon-name="arrow-stroke"/>
                    </div>
                </slot>
            </template>
            <template #singleLabel>
                <slot name="singleLabel"/>
            </template>
            <template #placeholder>
                <slot name="placeholder">
                    Выбери что-нибудь
                </slot>
            </template>
            <template #limit>
                <slot name="limit">
                    Ты выбрал слишком много
                </slot>
            </template>
            <template #clear="{ search }">
                <slot
                    name="clear"
                    :search="search"
                />
            </template>
            <template #tag="{ option, search, remove }">
                <slot
                    name="tag"
                    :option="option"
                    :search="search"
                    :remove="remove"
                />
            </template>
        </multiselect>
    </div>
</template>

<script>
    import Multiselect from 'vue-multiselect';
    import SvgIcon from '@/components/UI/SvgIcon';

    export default {
        name: 'FieldSelect',
        components: {
            Multiselect,
            SvgIcon
        },
        inheritAttrs: false,
        props: {
            modelValue: {
                type: [Number, String, Object, Array],
                default: ''
            },
            name: {
                type: String,
                default: 'select'
            },
            options: {
                type: Array,
                required: true
            },
            multiple: {
                type: Boolean,
                default: false
            },
            label: {
                type: String,
                default: 'label'
            },
            inputLabel: {
                type: String,
                default: ''
            },
            placeholder: {
                type: String,
                default: ''
            },
            tagPlaceholder: {
                type: String,
                default: ''
            },
            searchable: {
                type: Boolean,
                default: true
            },
            taggable: {
                type: Boolean,
                default: false
            },
            preserveSearch: {
                type: Boolean,
                default: false
            },
            internalSearch: {
                type: Boolean,
                default: true
            },
            clearOnSelect: {
                type: Boolean,
                default: true
            },
            required: {
                type: Boolean,
                default: false
            },
            trackBy: {
                type: String,
                default: ''
            },
            optionsLimit: {
                type: Number,
                default: 1000
            },
            iconBlock: {
                type: String,
                default: ''
            },
            allowEmpty: {
                type: Boolean,
                default: true
            },
            groupValues: {
                type: String,
                default: ''
            },
            groupLabel: {
                type: String,
                default: ''
            },
            selectGroupLabel: {
                type: String,
                default: ''
            },
            selectLabel: {
                type: String,
                default: ''
            },
            selectedLabel: {
                type: String,
                default: ''
            },
            deselectLabel: {
                type: String,
                default: ''
            },
            deselectGroupLabel: {
                type: String,
                default: ''
            },
            groupSelect: {
                type: Boolean,
                default: false
            },
            preselectFirst: {
                type: Boolean,
                default: false
            }
        },
        emits: ['remove', 'open', 'close', 'tag', 'search-change', 'select', 'update:modelValue'],
        computed: {
            value: {
                get() {
                    return this.modelValue
                },
                set(value) {
                    this.$emit('update:modelValue', value)
                }
            }
        },
        methods: {
            onSelect(event) {
                this.$emit('select', event);
            },
            onRemove(event) {
                this.$emit('remove', event);
            },
            onSearch(event) {
                this.$emit('search-change', event);
            },
            onTag(event) {
                this.$emit('tag', event);
            },
            onOpen(event) {
                this.$emit('open', event);
            },
            onClose(event) {
                this.$emit('close', event);
            },
        }
    }
</script>

<style lang="scss" scoped>
.dnd5club-select {
    ::v-deep(.multiselect) {
        .multiselect {
            box-sizing: border-box;
            outline: none;
            appearance: none;
            -webkit-overflow-scrolling: touch;

            &:after,
            &:before {
                box-sizing: border-box;
                outline: none;
                appearance: none;
                -webkit-overflow-scrolling: touch;
            }

            *,
            *:before,
            *:after {
                box-sizing: border-box;
                outline: none;
                appearance: none;
                -webkit-overflow-scrolling: touch;
            }

            &__tags {
                background: var(--bg-secondary);
                color: var(--text-color);
                font-size: var(--main-font-size);
                line-height: var(--main-line-height);
                min-height: 48px;
                padding: 12px 48px 0 12px;
                border: {
                    width: 1px 0;
                    style: solid;
                    color: var(--border);
                    radius: 0;
                };
            }

            &__select {
                width: 48px;
                height: calc(100% - 2px);
                padding: 0;
                right: 0;

                &:before {
                    display: none;
                }

                svg {
                    color: var(--primary);
                }
            }

            &__spinner {
                width: 48px;
                height: calc(100% - 2px);
                background: var(--bg-secondary);
                right: 0;

                &:before,
                &:after {
                    border-color: var(--primary) transparent transparent;
                }
            }

            &__input {
                background-color: transparent;
                border-radius: 0;
                color: var(--text-color);
                padding: 0;

                &::placeholder {
                    color: var(--text-color);
                    font-size: var(--main-font-size);
                    line-height: var(--main-line-height);
                }
            }

            &__single {
                font-size: var(--main-font-size);
                line-height: var(--main-line-height);
                border-radius: 0;
                background: transparent;
                padding: 0;
                margin-bottom: 0;
            }

            &__placeholder {
                color: var(--text-color);
                font-size: var(--main-font-size);
                line-height: var(--main-line-height);
                padding: 0;
            }

            &__content {
                &-wrapper {
                    background: var(--bg-secondary);
                    color: var(--text-color);
                    font-size: var(--main-font-size);
                    line-height: var(--main-line-height);
                    border: {
                        width: 0 0 1px 0;
                        style: solid;
                        color: var(--border);
                        radius: 0;
                    };
                }
            }

            &__option {
                background: var(--bg-secondary);
                color: var(--text-color);

                &--group {
                    background: var(--bg-sub-menu);
                    color: var(--text-color);
                }

                &--disabled {
                    background: var(--bg-sub-menu) !important;
                    color: var(--text-color) !important;
                }

                &--highlight {
                    background-color: var(--primary-hover);
                    color: var(--text-btn-color);

                    &:after {
                        background-color: transparent;
                    }
                }
            }
        }
    }
}
</style>
