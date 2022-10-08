<template>
    <form
        class="change-password form"
        @keyup.enter.exact.prevent="onSubmit"
        @submit.prevent="onSubmit"
    >
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
    import { useToast } from "vue-toastification";

    export default defineComponent({
        components: {
            FormButton,
            FieldInput
        },
        props: {
            token: {
                type: String,
                default: ""
            }
        },
        emits: ['close', 'switch:auth'],
        setup(props, { emit }) {
            const toast = useToast();
            const userStore = useUserStore();
            const success = ref(false);
            const inProgress = ref(false);
            const error = ref({});
            const state = reactive({
                email: '',
                password: '',
                repeat: ''
            });
            const isOnlyPassword = computed(() => props.token || userStore.isAuthenticated);
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

            async function sendQuery() {
                if (isOnlyPassword.value) {
                    try {
                        const payload = {
                            password: state.password,
                            [userStore.isAuthenticated ? 'userToken' : 'resetToken']: userStore.isAuthenticated
                                ? userStore.getUserToken()
                                : props.token
                        };

                        await userStore.changePassword(payload);

                        toast.success("Пароль успешно изменен!", {
                            onClose: () => {
                                if (props.token) {
                                    window.location.replace('/');

                                    return;
                                }

                                window.location.reload();
                            }
                        });

                        return Promise.resolve();
                    } catch (err) {
                        return Promise.reject(err);
                    }
                }

                try {
                    await userStore.resetPassword(state.email);

                    toast.success("Ссылка для изменения пароля отправлена на указанный e-mail");

                    emit('close');

                    return Promise.resolve();
                } catch (err) {
                    return Promise.reject(err);
                }
            }

            async function onSubmit() {
                inProgress.value = true;

                await v$.value.$reset();

                const result = await v$.value.$validate();

                if (!result) {
                    toast.error('Проверьте правильность заполнения полей');

                    inProgress.value = false;

                    return;
                }

                try {
                    await sendQuery();

                    success.value = true;
                } catch (err) {
                    toast.error('Неизвестная ошибка');
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
