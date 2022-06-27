<template>
    <form
        class="registration form"
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
                v-model="email"
                placeholder="Электронный адрес"
                is-email
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="password"
                placeholder="Пароль"
                is-password
            />
        </div>

        <div class="form__row">
            <field-input
                v-model="repeat"
                placeholder="Повторите пароль"
                is-password
            />
        </div>

        <div class="form__row">
            <form-button @click.left.exact.prevent="submit">
                Регистрация
            </form-button>

            <form-button
                is-link
                @click.left.exact.prevent="$emit('change-type')"
            >
                Авторизация
            </form-button>
        </div>
    </form>
</template>

<script>
    import FieldInput from "@/components/form/FieldType/FieldInput";
    import FieldCheckbox from "@/components/form/FieldType/FieldCheckbox";
    import FormButton from "@/components/form/FormButton";

    export default {
        name: 'RegistrationView',
        components: { FormButton, FieldCheckbox, FieldInput },
        data: () => ({
            username: '',
            email: '',
            password: '',
            repeat: ''
        }),
        methods: {
            submit() {
                console.log('login')
            }
        }
    }
</script>
