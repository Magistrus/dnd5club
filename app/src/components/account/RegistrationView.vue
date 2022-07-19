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
                v-model="form.username"
                placeholder="Имя пользователя"
                :validator="isUsernameError"
                required
                @validated="isValid.username = $event"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="form.email"
                placeholder="Электронный адрес"
                :validator="isEmailError"
                is-email
                required
                @validated="isValid.email = $event"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="form.password"
                placeholder="Пароль"
                :validator="isPwdError"
                is-password
                required
                @validated="isValid.password = $event"
                @input="isValid.repeat = !!isRepeatError"
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
        validatePwdLowerCase,
        validatePwdNumber,
        validatePwdSpecial,
        validatePwdUpperCase,
        validateUsernameSpecialChars
    } from "@/common/helpers/authChecks";
    import errorHandler from "@/common/helpers/errorHandler";
    import { mapActions } from "pinia";
    import { useUserStore } from "@/store/UI/UserStore";

    export default {
        name: 'RegistrationView',
        components: {
            FieldInput,
            FormButton
        },
        data: () => ({
            form: {
                username: '',
                email: '',
                password: ''
            },
            repeat: '',
            controllers: {
                username: undefined,
                email: undefined
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
            },
            inProgress: false
        }),
        methods: {
            ...mapActions(useUserStore, ['registration', 'authorization']),

            async isUsernameError() {
                if (this.form.username.length < 5) {
                    return 'Не менее 5 символов';
                }

                if (this.form.username.length > 24) {
                    return 'Не более 24 символов';
                }

                if (!validateUsernameSpecialChars(this.form.username)) {
                    return 'Допустимы латинские буквы, 0-9 - _ .';
                }

                try {
                    if (this.controllers.username) {
                        this.controllers.username.abort();
                    }

                    this.controllers.username = new AbortController();

                    const resp = await this.$http.post(
                        '/auth/exist',
                        {
                            username: this.form.username
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
                            email: this.form.email
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
                if (this.form.password.length < 8) {
                    return 'Не менее 8 символов';
                }

                if (!validatePwdLowerCase(this.form.password)) {
                    return 'Должна быть хотя бы одна латинская буква в нижнем регистре';
                }

                if (!validatePwdUpperCase(this.form.password)) {
                    return 'Должна быть хотя бы одна латинская буква в верхнем регистре';
                }

                if (!validatePwdNumber(this.form.password)) {
                    return 'Должна быть хотя бы одна цифра';
                }

                if (!validatePwdSpecial(this.form.password)) {
                    return 'Допустимые спец. символы: ! @ # $ % ^ & * _';
                }

                return null;
            },

            isRepeatError() {
                if (this.repeat !== this.form.password) {
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

                if (this.success || Object.values(this.isValid).includes(false)) {
                    this.inProgress = false;

                    this.onError('Необходимо заполнить все поля');

                    return;
                }

                this.clearError();

                try {
                    await this.registration(this.form);
                    await this.authorization({
                        usernameOrEmail: this.form.username,
                        password: this.form.password,
                        remember: false
                    });

                    this.successHandler();
                } catch (err) {
                    this.onError('Неизвестная ошибка');
                } finally {
                    this.inProgress = false;
                }
            }
        }
    };
</script>
