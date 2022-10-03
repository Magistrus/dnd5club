<template>
    <form
        class="change-password form"
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
                {{ isOnlyPassword ? 'Пароль изменен' : 'Проверьте почту' }}
            </div>
        </transition>

        <div
            class="form__row"
            :class="{ 'is-hidden': isOnlyPassword }"
        >
            <field-input
                v-model.trim="v$.email.$model"
                placeholder="Электронный адрес"
                required
                :autocomplete="isOnlyPassword ? 'username' : 'email'"
                autocapitalize="off"
                autocorrect="off"
                :error-text="v$.email.$dirty ? v$.email.$errors?.[0]?.$message : ''"
                @input="v$.email.$reset()"
                @blur="v$.email.$touch()"
            />
        </div>

        <div
            v-if="isOnlyPassword"
            class="form__row"
        >
            <field-input
                v-model.trim="v$.password.$model"
                placeholder="Новый пароль"
                is-password
                required
                autocomplete="new-password"
                autocapitalize="off"
                autocorrect="off"
                :error-text="v$.password.$dirty ? v$.password.$errors?.[0]?.$message : ''"
                @input="v$.password.$reset()"
                @blur="v$.password.$touch()"
            />
        </div>

        <div
            v-if="isOnlyPassword"
            class="form__row"
        >
            <field-input
                v-model.trim="v$.repeat.$model"
                placeholder="Повторите пароль"
                is-password
                required
                autocomplete="new-password"
                autocapitalize="off"
                autocorrect="off"
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
                {{ isOnlyPassword ? 'Изменить пароль' : 'Восстановить пароль' }}
            </form-button>

            <form-button
                v-if="!isAuthenticated"
                type-link
                @click.left.exact.prevent="$emit('switch:auth')"
            >
                Авторизация
            </form-button>
        </div>
    </form>
</template>

<script>
    import FormButton from "@/components/form/FormButton";
    import FieldInput from "@/components/form/FieldType/FieldInput";
    import { useUserStore } from "@/store/UI/UserStore";
    import useVuelidate from "@vuelidate/core";
    import {
        validateEmailFormat,
        validateMinLength,
        validatePwdLowerCase,
        validatePwdNumber,
        validatePwdSpecial,
        validatePwdUpperCase,
        validateRequired, validateUsernameSpecialChars
    } from "@/common/helpers/authChecks";
    import {
        helpers, or, sameAs
    } from "@vuelidate/validators";
    import {
        computed, defineComponent, reactive, ref
    } from "vue";
    import { useRoute } from "vue-router";

    export default defineComponent({
        components: {
            FormButton,
            FieldInput
        },
        props: {
            inModal: {
                type: Boolean,
                default: false
            }
        },
        emits: ['close', 'switch:auth'],
        setup(props, { emit }) {
            const route = useRoute();
            const userStore = useUserStore();
            const success = ref(false);
            const inProgress = ref(false);
            const error = ref({});
            const state = reactive({
                email: '',
                password: '',
                repeat: ''
            });
            const isOnlyPassword = computed(() => (route.name === 'recovery-password' && !props.inModal)
                || userStore.isAuthenticated);
            const validations = computed(() => {
                if (isOnlyPassword.value) {
                    return {
                        email: {
                            format: or(
                                validateUsernameSpecialChars(),
                                validateEmailFormat()
                            )
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
                            sameAs: helpers.withMessage('Пароли не совпадают', sameAs(computed(() => state.password)))
                        }
                    };
                }

                return {
                    email: {
                        required: validateRequired(),
                        format: validateEmailFormat()
                    }
                };
            });
            const v$ = useVuelidate(validations.value, state, { $lazy: true });

            function clearError() {
                error.value = {
                    status: false,
                    text: ''
                };
            }

            async function clearForm() {
                state.email = '';
                state.password = '';
                state.repeat = '';
                success.value = false;

                await v$.value.$reset();
            }

            function successHandler() {
                clearError();

                success.value = true;

                setTimeout(() => {
                    clearForm();

                    emit('close');

                    if (route.name === 'recovery-password') {
                        window.location.replace('/');

                        return;
                    }

                    window.location.reload();
                }, 2000);
            }

            function onError(text) {
                error.value = {
                    status: true,
                    text
                };
            }

            async function sendQuery() {
                if (isOnlyPassword.value) {
                    try {
                        const payload = {
                            password: state.password,
                            [userStore.isAuthenticated ? 'userToken' : 'resetToken']: userStore.isAuthenticated
                                ? userStore.getUserToken()
                                : route.query.token
                        };

                        await userStore.changePassword(payload);

                        return Promise.resolve();
                    } catch (err) {
                        return Promise.reject(err);
                    }
                }

                try {
                    await userStore.resetPassword(state.email);

                    return Promise.resolve();
                } catch (err) {
                    return Promise.reject(err);
                }
            }

            async function onSubmit() {
                clearError();

                inProgress.value = true;

                await v$.value.$reset();

                const result = await v$.value.$validate();

                if (!result) {
                    inProgress.value = false;

                    return;
                }

                try {
                    await sendQuery();

                    successHandler();
                } catch (err) {
                    onError('Неизвестная ошибка');
                } finally {
                    inProgress.value = false;
                }
            }

            return {
                isAuthenticated: computed(() => userStore.isAuthenticated),
                inProgress,
                isOnlyPassword,
                v$,
                error,
                success,
                onSubmit
            };
        }
    });
</script>

<style lang="scss" scoped>

</style>
