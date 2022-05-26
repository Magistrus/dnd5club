<template>
    <div
        v-if="modelValue?.length"
        class="filter-item"
        :class="{ 'is-active': opened }"
    >
        <div class="filter-item__header">
            <div
                class="filter-item__trigger"
                @click.left.exact.prevent="opened = !opened"
            >
                <div class="filter-item__name">
                    {{ name }}
                </div>

                <button
                    type="button"
                    class="filter-item__button filter-item__button--toggle"
                    @click.self.left.exact.prevent="opened = !opened"
                >
                    <svg-icon icon-name="arrow-stroke"/>
                </button>
            </div>

            <button
                v-if="isFilterCustomized"
                type="button"
                class="filter-item__button filter-item__button--reset"
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
    import _ from 'lodash';
    import SvgIcon from '@/components/UI/SvgIcon';
    import FieldCheckbox from '@/components/UI/FieldType/FieldCheckbox';

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
            this.opened = this.isFilterCustomized;
        },
        methods: {
            resetValues() {
                const values = _.cloneDeep(this.modelValue)
                    .map(value => ({
                        ...value,
                        value: value.default
                    }));

                this.emitValues(values);
            },

            setValue(newValue, index) {
                const values = _.cloneDeep(this.modelValue);

                values[index].value = newValue;

                this.emitValues(values);
            },

            emitValues(values) {
                this.$emit('update:model-value', values);
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "FilterItem.module";
</style>
