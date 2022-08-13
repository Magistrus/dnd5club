<template>
    <label class="field-input">
        <span
            v-if="label"
            class="field-input__label"
        >
            {{ label }}
        </span>

        <span
            class="field-input__control"
            :class="{ 'is-error': errorText }"
        >
            <input
                v-bind="attrs"
                ref="input"
                v-model="value"
                :autocomplete="inputAutocomplete"
                :placeholder="placeholder"
                :type="inputType"
                :spellcheck="false"
                class="field-input__input"
                @blur="$emit('blur')"
            >

            <span
                v-if="isPassword"
                class="field-input__control_icon"
                @click.left.exact.prevent="togglePass"
            >
                <svg-icon
                    :icon-name="showedPass ? 'hide-pass' : 'show-pass'"
                    :stroke-enable="false"
                    fill-enable
                />
            </span>
        </span>

        <span
            v-if="!!errorText"
            class="field-input__error"
        >
            {{ errorText }}
        </span>
    </label>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";

    export default {
        name: "FieldInput",
        components: {
            SvgIcon
        },
        inheritAttrs: false,
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
                type: [Boolean, String],
                default: false
            },
            type: {
                type: String,
                default: 'text'
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
            }
        },
        emits: ['update:modelValue', 'blur'],
        data: () => ({
            showedPass: false,
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

            inputType() {
                if (this.isNumber) {
                    return 'number';
                }

                if (this.isPassword) {
                    return this.showedPass ? 'text' : 'password';
                }

                if (this.isEmail) {
                    return 'email';
                }

                return this.type;
            },

            inputAutocomplete() {
                switch (typeof this.autocomplete) {
                    case 'boolean':
                        return this.autocomplete ? 'on' : 'off';
                    case "string":
                        return this.autocomplete;

                    default:
                        return 'off';
                }
            },

            attrs() {
                const attrs = { ...this.$attrs };

                if (this.isNumber) {
                    if (this.min !== undefined) {
                        attrs.min = this.min;
                    }
                }

                return attrs;
            }
        },
        methods: {
            togglePass() {
                this.showedPass = !this.showedPass;

                this.$refs.input.focus();
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
            display: flex;
            overflow: hidden;
            width: 100%;

            &.is-error {
                border-color: var(--error);
            }

            &_icon {
                display: flex;
                align-items: center;
                justify-content: center;
                height: 38px;
                width: 38px;
                cursor: pointer;
                padding: 10px;
                color: var(--text-btn-color);
            }
        }

        &__input {
            background-color: transparent;
            color: var(--text-color);
            font-size: var(--main-font-size);
            height: 38px;
            font-family: 'Open Sans', serif;
            padding: 4px 12px;
            margin: 0;
            overflow: hidden;
            text-overflow: ellipsis;
            border: 0;
            flex: 1 1 auto;
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
