<template>
    <form
        class="login form"
        @keyup.enter.exact.prevent="onSubmit"
        @submit.prevent="onSubmit"
    >
        <transition
            name="fade"
            mode="out-in"
        >
            <div
                v-if="error.status"
                class="form__row is-error"
            >
                {{ error.text }}
            </div>

            <div
                v-else-if="success"
                class="form__row is-success"
            >
                Вы авторизованы
            </div>
        </transition>

        <div class="form__row">
            <field-input
                v-model="v$.usernameOrEmail.$model"
                placeholder="Логин или электронная почта"
                autocomplete="username"
                autocapitalize="off"
                autocorrect="off"
                required
                :error-text="v$.usernameOrEmail.$dirty ? v$.usernameOrEmail.$errors?.[0]?.$message : ''"
                @input="v$.usernameOrEmail.$reset()"
                @blur="v$.usernameOrEmail.$touch()"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="v$.password.$model"
                placeholder="Пароль"
                is-password
                required
                autocomplete="current-password"
                autocapitalize="off"
                autocorrect="off"
                :error-text="v$.password.$dirty ? v$.password.$errors?.[0]?.$message : ''"
                @input="v$.password.$reset()"
                @blur="v$.password.$touch()"
            />
        </div>

        <div class="form__row">
            <field-checkbox
                v-model="remember"
                type="toggle"
            >
                Запомнить меня
            </field-checkbox>
        </div>

        <div class="form__row">
            <form-button
                :disabled="success || inProgress"
                @click.left.exact.prevent="onSubmit"
            >
                Вход
            </form-button>

            <form-button
                type-link
                @click.left.exact.prevent="$emit('change-type')"
            >
                Регистрация
            </form-button>
        </div>
    </form>
</template>

<script>
    import FieldInput from "@/components/form/FieldType/FieldInput";
    import FieldCheckbox from "@/components/form/FieldType/FieldCheckbox";
    import FormButton from "@/components/form/FormButton";
    import { mapActions } from "pinia";
    import { useUserStore } from "@/store/UI/UserStore";
    import {
        validateEmailFormat,
        validatePwdSpecial,
        validateRequired,
        validateUsernameSpecialChars
    } from "@/common/helpers/authChecks";
    import useVuelidate from "@vuelidate/core/dist/index.esm";
    import { helpers, or } from "@vuelidate/validators";

    export default {
        name: 'LoginView',
        components: {
            FormButton,
            FieldCheckbox,
            FieldInput
        },
        setup: () => ({
            v$: useVuelidate()
        }),
        data: () => ({
            usernameOrEmail: '',
            password: '',
            remember: true,
            error: {
                status: false,
                text: ''
            },
            inProgress: false,
            success: false
        }),
        methods: {
            ...mapActions(useUserStore, ['authorization']),

            clearError() {
                this.error = {
                    status: false,
                    text: ''
                };
            },

            async clearForm() {
                this.usernameOrEmail = '';
                this.password = '';
                this.remember = true;
                this.success = false;

                await this.v$.$reset();
            },

            successHandler() {
                this.clearError();

                this.success = true;

                setTimeout(() => {
                    this.clearForm();

                    this.$emit('close');
                }, 2000);
            },

            onError(text) {
                this.error = {
                    status: true,
                    text
                };
            },

            async onSubmit() {
                this.inProgress = true;

                await this.v$.$reset();

                const result = await this.v$.$validate();

                if (this.success || !result) {
                    this.inProgress = false;

                    return;
                }

                this.clearError();

                try {
                    await this.authorization({
                        usernameOrEmail: this.usernameOrEmail,
                        password: this.password,
                        remember: this.remember
                    });

                    this.successHandler();
                } catch (err) {
                    const { response } = err;

                    if (!response?.status) {
                        this.onError('Неизвестная ошибка');

                        return;
                    }

                    switch (response.status) {
                        case 401:
                            this.onError('Неверный логин или пароль');

                            break;
                        default:
                            this.onError('Неизвестная ошибка');

                            break;
                    }
                } finally {
                    this.inProgress = false;
                }
            }
        },
        validations() {
            return {
                usernameOrEmail: {
                    required: validateRequired(),
                    format: helpers.withMessage(
                        'Поле заполнено неверно',
                        or(
                            validateUsernameSpecialChars(),
                            validateEmailFormat()
                        )
                    )
                },
                password: {
                    required: validateRequired(),
                    format: validatePwdSpecial()
                }
            };
        }
    };
</script>
