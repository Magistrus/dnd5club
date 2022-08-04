<template>
    <form
        class="registration form"
        @submit.prevent="onSubmit"
        @keyup.enter.exact.prevent="onSubmit"
        @keyup.enter.ctrl.exact.prevent="onSubmit"
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
                Вы зарегистрированы. Теперь нужно авторизоваться
            </div>
        </transition>

        <div class="form__row">
            <field-input
                v-model="v$.username.$model"
                placeholder="Имя пользователя"
                required
                :error-text="v$.username.$dirty ? v$.username.$errors?.[0]?.$message : ''"
                @input="v$.username.$reset()"
                @blur="v$.username.$touch()"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="v$.email.$model"
                placeholder="Электронный адрес"
                required
                :error-text="v$.email.$dirty ? v$.email.$errors?.[0]?.$message : ''"
                @input="v$.email.$reset()"
                @blur="v$.email.$touch()"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="v$.password.$model"
                placeholder="Пароль"
                is-password
                required
                :error-text="v$.password.$dirty ? v$.password.$errors?.[0]?.$message : ''"
                @input="v$.password.$reset()"
                @blur="v$.password.$touch()"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="v$.repeat.$model"
                placeholder="Повторите пароль"
                is-password
                required
                :error-text="v$.repeat.$dirty ? v$.repeat.$errors?.[0]?.$message : ''"
                @input="v$.repeat.$reset()"
                @blur="v$.repeat.$touch()"
            />
        </div>

        <div class="form__row">
            <form-button
                :disabled="success || inProgress"
                @click.left.exact.prevent="onSubmit"
            >
                Регистрация
            </form-button>

            <form-button
                type-link
                @click.left.exact.prevent="$emit('change-type')"
            >
                Авторизация
            </form-button>
        </div>
    </form>
</template>

<script>
    import FieldInput from "@/components/form/FieldType/FieldInput";
    import FormButton from "@/components/form/FormButton";
    import {
        validateEmailExist,
        validateEmailFormat,
        validateMinLength,
        validatePwdLowerCase,
        validatePwdNumber,
        validatePwdSpecial,
        validatePwdUpperCase,
        validateRequired,
        validateUsernameExist,
        validateUsernameSpecialChars
    } from "@/common/helpers/authChecks";
    import { mapActions } from "pinia";
    import { useUserStore } from "@/store/UI/UserStore";
    import useVuelidate from "@vuelidate/core";
    import { helpers, sameAs } from "@vuelidate/validators";

    export default {
        name: 'RegistrationView',
        components: {
            FieldInput,
            FormButton
        },
        setup: () => ({
            v$: useVuelidate()
        }),
        data: () => ({
            username: '',
            email: '',
            password: '',
            repeat: '',
            error: {
                status: false,
                text: ''
            },
            success: false,
            inProgress: false
        }),
        methods: {
            ...mapActions(useUserStore, ['registration', 'authorization']),

            clearError() {
                this.error = {
                    status: false,
                    text: ''
                };
            },

            async clearForm() {
                this.username = '';
                this.email = '';
                this.password = '';
                this.repeat = '';
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

                if (!result) {
                    this.inProgress = false;

                    return;
                }

                this.clearError();

                try {
                    await this.registration({
                        username: this.username,
                        email: this.email,
                        password: this.password
                    });
                    await this.authorization({
                        usernameOrEmail: this.username,
                        password: this.password,
                        remember: false
                    });

                    this.successHandler();
                } catch (err) {
                    this.onError('Неизвестная ошибка');
                } finally {
                    this.inProgress = false;
                }
            }
        },
        validations() {
            return {
                username: {
                    required: validateRequired(),
                    minLength: validateMinLength(5),
                    specialChars: validateUsernameSpecialChars(),
                    exist: validateUsernameExist()
                },
                email: {
                    required: validateRequired(),
                    format: validateEmailFormat(),
                    exist: validateEmailExist()
                },
                password: {
                    required: validateRequired(),
                    minLength: validateMinLength(8),
                    lowerCase: validatePwdLowerCase(),
                    upperCase: validatePwdUpperCase(),
                    numbers: validatePwdNumber(),
                    specialChars: validatePwdSpecial()
                },
                repeat: {
                    required: validateRequired(),
                    sameAs: helpers.withMessage('Пароли не совпадают', sameAs(this.password))
                }
            };
        }
    };
</script>
