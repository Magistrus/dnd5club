<template>
    <div
        v-if="type === 'crumb'"
        v-tippy="tooltip"
        :class="{'is-active': value}"
        class="field-checkbox field-checkbox--crumb"
        @click.left.exact.prevent="value = !value"
    >
        <div class="field-checkbox__label">
            <slot/>
        </div>
    </div>

    <div
        v-else-if="type === 'toggle'"
        :class="{'is-active': value}"
        class="field-checkbox field-checkbox--toggle"
        @click.left.exact.prevent="value = !value"
    >
        <div class="field-checkbox__faker"/>

        <div
            v-if="$slots.default"
            class="field-checkbox__label"
        >
            <slot/>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'FieldCheckbox',
        props: {
            modelValue: {
                type: Boolean,
                default: false,
            },
            type: {
                type: String,
                default: 'crumb',
                validator: value => ['crumb', 'toggle'].includes(value)
            },
            tooltip: {
                type: String,
                default: ''
            }
        },
        emits: ['update:model-value'],
        computed: {
            value: {
                get() {
                    return this.modelValue
                },
                set(value) {
                    this.$emit('update:model-value', value)
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .field-checkbox {
        cursor: pointer;

        &--crumb {
            display: inline-block;
            width: fit-content;

            .field-checkbox {
                &__label {
                    @include css_anim();

                    display: inline-block;
                    padding: 4px 8px;
                    border-radius: 4px;
                    background-color: var(--hover);
                    color: var(--text-color);
                }
            }

            &.is-active {
                .field-checkbox {
                    &__label {
                        @include css_anim();

                        background-color: var(--primary-active);
                        color: var(--text-btn-color);
                    }
                }
            }

            @include media-min($md) {
                &:not(.is-active) {
                    .field-checkbox__label {
                        &:hover {
                            background-color: var(--primary-hover);
                            color: var(--text-btn-color);
                        }
                    }
                }
            }
        }

        &--toggle {
            display: inline-flex;

            .field-checkbox {
                &__faker {
                    @include css_anim();

                    display: flex;
                    align-items: center;
                    justify-content: flex-start;
                    width: 34px;
                    height: 20px;
                    border: 2px solid transparent;
                    background-color: var(--hover);
                    border-radius: 26px;
                    padding: 1px;
                    flex-shrink: 0;

                    &:after {
                        @include css_anim();

                        content: '';
                        display: block;
                        border-radius: 50%;
                        background-color: var(--text-btn-color);
                        width: 14px;
                        height: 14px;
                    }
                }
            }

            &.is-active {
                .field-checkbox {
                    &__faker {
                        background-color: var(--primary);

                        &:after {
                            transform: translateX(100%);
                        }
                    }
                }
            }

            @include media-min($md) {
                &:hover {
                    .field-checkbox {
                        &__faker {
                            border-color: #aef;
                        }
                    }
                }
            }
        }
    }
</style>
