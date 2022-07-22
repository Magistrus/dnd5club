<template>
    <form
        class="login form"
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
                v-model="form.usernameOrEmail"
                placeholder="Логин или электронная почта"
                required
                @validated="isValid.usernameOrEmail = $event"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="form.password"
                placeholder="Пароль"
                is-password
                required
                @validated="isValid.password = $event"
            />
        </div>

        <div class="form__row">
            <field-checkbox
                v-model="form.remember"
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

    export default {
        name: 'LoginView',
        components: {
            FormButton,
            FieldCheckbox,
            FieldInput
        },
        data: () => ({
            form: {
                usernameOrEmail: '',
                password: '',
                remember: true
            },
            isValid: {
                usernameOrEmail: false,
                password: false
            },
            error: {
                status: false,
                text: ''
            },
            success: false,
            inProgress: false
        }),
        methods: {
            ...mapActions(useUserStore, ['authorization']),

            clearError() {
                this.error = {
                    status: false,
                    text: ''
                };
            },

            clearForm() {
                this.form = {
                    usernameOrEmail: '',
                    password: '',
                    remember: true
                };
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

                if (this.success || Object.values(this.isValid).includes(false)) {
                    this.inProgress = false;

                    this.onError('Необходимо заполнить все поля');

                    return;
                }

                this.clearError();

                try {
                    await this.authorization(this.form);

                    this.successHandler();
                } catch (err) {
                    this.onError(err || 'Неизвестная ошибка');
                } finally {
                    this.inProgress = false;
                }
            }
        }
    };
</script>
