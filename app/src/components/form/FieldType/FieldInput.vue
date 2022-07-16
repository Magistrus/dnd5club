<template>
    <label class="field-input">
        <span
            v-if="label"
            class="field-input__label"
        >
            {{ label }}
        </span>

        <span class="field-input__control">
            <input
                v-model="value"
                v-bind="attrs"
                :autocomplete="autocomplete ? 'on' : 'off'"
                :placeholder="placeholder"
                :type="type"
                class="field-input__input"
                :class="{ 'is-error': error.status }"
                @blur="checkField"
                @input="clearError"
            >
        </span>

        <span
            v-if="error.status && !!error.text"
            class="field-input__error"
        >
            {{ error.text }}
        </span>
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
            },
            required: {
                type: Boolean,
                default: false
            },
            errorText: {
                type: String,
                default: ''
            },
            validator: {
                type: Function,
                default: undefined
            }
        },
        emits: ['update:modelValue', 'validated'],
        data: () => ({
            error: {
                status: false,
                text: ''
            }
        }),
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
        },
        methods: {
            clearError() {
                this.error = {
                    status: false,
                    text: ''
                };
            },

            isEmailValid() {
                return (/^[a-zA-Z\d.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z\d-]+\.[a-zA-Z\d-]+$/).test(this.value);
            },

            async checkField() {
                const emitCheck = success => this.$emit('validated', success);

                let status = {
                    error: false,
                    text: ''
                };

                if (this.required && !this.value.length) {
                    status = {
                        error: true,
                        text: 'Поле не заполнено'
                    };
                }

                if (!status.error && this.isEmail && !this.isEmailValid()) {
                    status = {
                        error: true,
                        text: 'Неверный электронный адрес'
                    };
                }

                if (!status.error && this.validator) {
                    const result = await this.validator();

                    if (result) {
                        status = {
                            error: true,
                            text: result
                        };
                    }
                }

                if (!status.error && this.isError) {
                    status = {
                        error: true,
                        text: this.errorText || ''
                    };
                }

                if (!status.error) {
                    this.clearError();

                    emitCheck(!status.error);

                    return;
                }

                this.error = {
                    status: status.error,
                    text: status.text
                };

                emitCheck(!status.error);
            }
        }
    };
</script>

<style lang="scss" scoped>
    .field-input {
        display: block;
        width: 100%;

        &__control {
            @include css_anim();

            border: 1px solid var(--border);
            background: var(--bg-sub-menu);
            border-radius: 8px;
            display: block;
            overflow: hidden;
            width: 100%;

            &.is-error {
                border-color: var(--error);
            }
        }

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

        &__error {
            color: var(--text-color);
            font-size: 12px;
            padding: 8px 12px 0;
            display: block;
        }

        &:focus-within {
            .field-input {
                &__control {
                    @include css_anim();

                    border-color: var(--primary-active);
                }

                &__input {
                    background-color: transparent;
                }
            }
        }

        &:hover {
            .field-input {
                &__control {
                    border-color: var(--primary-hover);
                }

                &__input {
                    background-color: transparent;
                }
            }
        }
    }
</style>
