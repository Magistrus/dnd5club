<template>
    <form
        class="login form"
        @submit.prevent="submit()"
    >
        <div class="form__row">
            <field-input
                v-model="username"
                placeholder="Имя пользователя"
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="password"
                placeholder="Пароль"
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
            <form-button @click.left.exact.prevent="submit">
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

    export default {
        name: 'LoginView',
        components: { FormButton, FieldCheckbox, FieldInput },
        data: () => ({
            username: '',
            password: '',
            remember: false
        }),
        methods: {
            submit() {
                // console.log('login')
            }
        }
    }
</script>
