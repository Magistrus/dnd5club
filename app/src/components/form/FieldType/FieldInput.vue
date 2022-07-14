<template>
    <label class="field-input">
        <span
            v-if="label"
            class="field-input__label"
        >
            {{ label }}
        </span>

        <input
            v-model="value"
            v-bind="attrs"
            :autocomplete="autocomplete ? 'on' : 'off'"
            :placeholder="placeholder"
            :type="type"
            class="field-input__input"
        >
    </label>
</template>

<script>
    export default {
        name: "FieldInput",
        props: {
            modelValue: {
                type: [String, Number],
                default: ''
            },
            label: {
                type: String,
                default: ''
            },
            placeholder: {
                type: String,
                default: ''
            },
            autocomplete: {
                type: Boolean,
                default: false
            },
            isNumber: {
                type: Boolean,
                default: false
            },
            isPassword: {
                type: Boolean,
                default: false
            },
            isEmail: {
                type: Boolean,
                default: false
            },
            isError: {
                type: Boolean,
                default: false
            },
            min: {
                type: Number,
                default: undefined
            }
        },
        emits: ['update:modelValue'],
        computed: {
            value: {
                get() {
                    return this.modelValue;
                },

                set(e) {
                    this.$emit('update:modelValue', e);
                }
            },

            type() {
                if (this.isNumber) {
                    return 'number';
                }

                if (this.isPassword) {
                    return 'password';
                }

                if (this.isEmail) {
                    return 'email';
                }

                return 'text';
            },

            attrs() {
                const attrs = {};

                if (this.isNumber) {
                    if (this.min !== undefined) {
                        attrs.min = this.min;
                    }
                }

                return attrs;
            }
        }
    };
</script>

<style lang="scss" scoped>
    .field-input {
        @include css_anim();

        border: 1px solid var(--border);
        background: var(--bg-sub-menu);
        display: block;
        border-radius: 8px;
        overflow: hidden;
        width: 100%;

        &__input {
            background-color: transparent;
            color: var(--text-color);
            font-size: var(--main-font-size);
            height: 38px;
            font-family: 'Open Sans', serif;
            width: 100%;
            padding: 4px 12px;
            margin: 0;
            overflow: hidden;
            text-overflow: ellipsis;
            border: 0;
        }

        &:focus-within {
            @include css_anim();

            border-color: var(--primary-active);

            .field-input {
                &__input {
                    background-color: transparent;
                }
            }
        }

        &:hover {
            border-color: var(--primary-hover);

            .field-input {
                &__input {
                    background-color: transparent;
                }
            }
        }

        &.is-error {
            border-color: var(--error);
        }
    }
</style>
