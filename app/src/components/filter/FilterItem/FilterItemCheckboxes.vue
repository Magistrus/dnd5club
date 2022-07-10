<template>
    <div
        v-if="modelValue?.length"
        ref="filterItem"
        :class="{ 'is-active': opened }"
        class="filter-item"
    >
        <div class="filter-item__header">
            <div
                class="filter-item__trigger"
                @click.left.exact.prevent="toggleBlock"
            >
                <div class="filter-item__name">
                    {{ name }}
                </div>

                <button
                    class="filter-item__button filter-item__button--toggle"
                    type="button"
                    @click.self.left.exact.prevent.stop="toggleBlock"
                >
                    <svg-icon icon-name="arrow-stroke"/>
                </button>
            </div>

            <button
                v-if="isFilterCustomized"
                v-tippy="{ content: 'Сбросить блок «' + name + '»' }"
                class="filter-item__button filter-item__button--reset"
                type="button"
                @click.left.exact.prevent="resetValues"
            >
                <svg-icon icon-name="close"/>
            </button>
        </div>

        <div
            v-if="opened"
            class="filter-item__body"
        >
            <field-checkbox
                v-for="(checkbox, checkboxKey) in modelValue"
                :key="checkboxKey"
                :model-value="checkbox.value"
                :tooltip="checkbox.tooltip"
                @update:model-value="setValue($event, checkboxKey)"
            >
                {{ checkbox.label }}
            </field-checkbox>
        </div>
    </div>
</template>

<script>
    import SvgIcon from '@/components/UI/SvgIcon';
    import FieldCheckbox from '@/components/form/FieldType/FieldCheckbox';
    import cloneDeep from 'lodash/cloneDeep';

    export default {
        name: 'FilterItemCheckboxes',
        components: {
            FieldCheckbox,
            SvgIcon
        },
        props: {
            name: {
                type: String,
                default: '',
                required: true
            },
            expand: {
                type: Boolean,
                default: false,
            },
            modelValue: {
                type: Array,
                default: undefined
            }
        },
        emits: ['update:model-value'],
        data: () => ({
            opened: false
        }),
        computed: {
            isFilterCustomized() {
                if (!this.modelValue) {
                    return false;
                }

                for (const value of this.modelValue) {
                    if (value.value !== value.default) {
                        return true
                    }
                }

                return false
            },
        },
        beforeMount() {
            this.opened = this.isFilterCustomized || this.expand;
        },
        methods: {
            resetValues() {
                const values = cloneDeep(this.modelValue)
                    .map(value => ({
                        ...value,
                        value: value.default
                    }));

                this.emitValues(values);
            },

            setValue(newValue, index) {
                const values = cloneDeep(this.modelValue);

                values[index].value = newValue;

                this.emitValues(values);
            },

            emitValues(values) {
                this.$emit('update:model-value', values);
            },

            toggleBlock() {
                this.opened = !this.opened;

                this.$refs.filterItem.scrollIntoView({
                    behavior: "smooth",
                    block: "nearest"
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "FilterItem.module";
</style>
