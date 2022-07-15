<template>
    <form
        class="registration form"
        @submit.prevent="onSubmit"
        @keyup.enter.exact.prevent="onSubmit"
        @keyup.enter.ctrl.exact.prevent="onSubmit"
    >
        <transition>
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
                v-model="username"
                placeholder="Имя пользователя"
                :validator="isUsernameError"
                required
                @validated="isValid.username = $event"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="email"
                placeholder="Электронный адрес"
                :validator="isEmailError"
                is-email
                required
                @validated="isValid.email = $event"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="password"
                placeholder="Пароль"
                :validator="isPwdError"
                is-password
                required
                @validated="isValid.password = $event"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="repeat"
                placeholder="Повторите пароль"
                :validator="isRepeatError"
                is-password
                required
                @validated="isValid.repeat = $event"
            />
        </div>

        <div class="form__row">
            <form-button
                :disabled="success"
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
        validatePwdLowerCase,
        validatePwdNumber,
        validatePwdSpecial,
        validatePwdUpperCase,
        validateUsernameSpecialChars
    } from "@/common/helpers/authChecks";
    import errorHandler from "@/common/helpers/errorHandler";

    export default {
        name: 'RegistrationView',
        components: {
            FieldInput,
            FormButton
        },
        data: () => ({
            username: '',
            email: '',
            password: '',
            repeat: '',
            controllers: {
                username: undefined,
                email: undefined,
                form: undefined
            },
            error: {
                status: false,
                text: ''
            },
            success: false,
            isValid: {
                username: false,
                email: false,
                password: false,
                repeat: false
            }
        }),
        methods: {
            async isUsernameError() {
                if (this.username.length < 5) {
                    return 'Не менее 5 символов';
                }

                if (this.username.length > 24) {
                    return 'Не более 24 символов';
                }

                if (!validateUsernameSpecialChars(this.username)) {
                    return 'Разрешено: латинские буквы, 0-9 - _ .';
                }

                try {
                    if (this.controllers.username) {
                        this.controllers.username.abort();
                    }

                    this.controllers.username = new AbortController();

                    const resp = await this.$http.post(
                        '/auth/exist',
                        {
                            username: this.username
                        },
                        this.controllers.username.signal
                    );

                    if (resp.status !== 200) {
                        errorHandler(resp.statusText);

                        return 'Неизвестная ошибка';
                    }
                } catch (err) {
                    const resp = err.response;

                    if (resp.status === 409) {
                        return 'Это имя пользователя уже занято';
                    }

                    errorHandler(err);

                    return 'Неизвестная ошибка';
                } finally {
                    this.controllers.username = undefined;
                }

                return null;
            },

            async isEmailError() {
                try {
                    if (this.controllers.email) {
                        this.controllers.email.abort();
                    }

                    this.controllers.email = new AbortController();

                    const resp = await this.$http.post(
                        '/auth/exist',
                        {
                            email: this.email
                        },
                        this.controllers.email.signal
                    );

                    if (resp.status !== 200) {
                        errorHandler(resp.statusText);

                        return 'Неизвестная ошибка';
                    }
                } catch (err) {
                    const resp = err.response;

                    if (resp.status === 409) {
                        return 'Этот адрес уже занят';
                    }

                    errorHandler(err);

                    return 'Неизвестная ошибка';
                } finally {
                    this.controllers.email = undefined;
                }

                return null;
            },

            isPwdError() {
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
                    return 'Допустимые спец. символы: ! @ # $ % ^ & * _';
                }

                return null;
            },

            isRepeatError() {
                if (this.repeat !== this.password) {
                    return 'Пароли не совпадают';
                }

                return null;
            },

            clearError() {
                this.error = {
                    status: false,
                    text: ''
                };
            },

            successHandler() {
                this.clearError();

                this.success = true;

                setTimeout(() => {
                    this.$emit('change-type');
                }, 3000);
            },

            onError(status, text) {
                this.error = {
                    status,
                    text
                };
            },

            async onSubmit() {
                console.log('try');

                if (this.success || Object.values(this.isValid).includes(false)) {
                    return;
                }

                this.clearError();

                try {
                    if (this.controllers.form) {
                        this.controllers.form.abort();
                    }

                    this.controllers.form = new AbortController();

                    const resp = await this.$http.post(
                        '/auth/signup',
                        {
                            username: this.username,
                            password: this.password,
                            email: this.email
                        },
                        this.controllers.form.signal
                    );

                    if (resp.status !== 200) {
                        errorHandler(resp.statusText);

                        this.onError(resp.status, resp.statusText);

                        return;
                    }

                    this.successHandler();
                } catch (err) {
                    errorHandler(err);
                } finally {
                    this.controllers.form = undefined;
                }
            }
        }
    };
</script>
