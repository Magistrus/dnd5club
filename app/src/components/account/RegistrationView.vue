<template>
    <form
        class="registration form"
        @submit.prevent="onSubmit"
    >
        <div class="form__row">
            <field-input
                v-model="username.value"
                placeholder="Имя пользователя"
                required
                @blur="username.showCheck = true"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="email.value"
                placeholder="Электронный адрес"
                is-email
                required
                @blur="email.showCheck = true"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="password.value"
                placeholder="Пароль"
                is-password
                required
                @blur="password.showCheck = true"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="repeat.value"
                placeholder="Повторите пароль"
                is-password
                required
                @blur="repeat.showCheck = true"
            />
        </div>

        <div class="form__row">
            <form-button @click.left.exact.prevent="onSubmit">
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
        validatePwdLowerCase,
        validatePwdNumber,
        validatePwdSpecial,
        validatePwdUpperCase,
        validateUsernameSpecialChars
    } from "@/common/helpers/authChecks";

    export default {
        name: 'RegistrationView',
        components: {
            FieldInput,
            FormButton
        },
        data: () => ({
            username: {
                showCheck: false,
                value: ''
            },
            email: {
                showCheck: false,
                value: ''
            },
            password: {
                showCheck: false,
                value: ''
            },
            repeat: {
                showCheck: false,
                value: ''
            }
        }),
        computed: {
            usernameError() {
                if (!this.username.length) {
                    return 'Имя пользователя должно быть заполнено';
                }

                if (this.username.length < 5) {
                    return 'Имя пользователя не может быть менее 5 символов';
                }

                if (this.username.length > 24) {
                    return 'Имя пользователя не может быть более 24 символов';
                }

                if (!validateUsernameSpecialChars(this.username)) {
                    return 'В имени пользователя разрешены: латинские буквы, цифры, дефис (-), точка';
                }

                return null;
            },

            pwdError() {
                if (!validatePwdLowerCase(this.password)) {
                    return 'Должна быть хотя бы одна латинская буква в нижнем регистре';
                }

                if (!validatePwdUpperCase(this.password)) {
                    return 'Должна быть хотя бы одна латинская буква в верхнем регистре';
                }

                if (!validatePwdNumber(this.password)) {
                    return 'Должна быть хотя бы одна цифра';
                }

                if (!validatePwdSpecial(this.password)) {
                    return 'Должен быть хотя бы один символ из этого списка: ! @ # $ % ^ & *';
                }

                return null;
            },

            repeatError() {
                if (this.repeat !== this.password) {
                    return 'Пароли не совпадают';
                }

                return null;
            },

            formSuccess() {
                return !this.usernameError || !this.pwdError || !this.repeatError;
            }
        },
        methods: {
            onSubmit() {
                if (!this.formSuccess) {
                    return;
                }

                // eslint-disable-next-line no-console
                console.log('Form success');
            }
        }
    };
</script>
