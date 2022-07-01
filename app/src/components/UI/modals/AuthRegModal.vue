<template>
    <vue-final-modal
        v-slot="{ close }"
        content-class="auth-reg-modal"
        esc-to-close
        focus-trap
        v-bind="$attrs"
    >
        <img
            :alt="type + '_background'"
            class="auth-reg-modal__bg"
            src="/img/bg_login.png"
        >

        <div class="auth-reg-modal__content">
            <form-button
                class="auth-reg-modal__close"
                type-link
                @click.left.exact.prevent="closeHandler(close)"
            >
                <svg-icon icon-name="close"/>
            </form-button>

            <div class="auth-reg-modal__body">
                <h4>{{ type === 'reg' ? 'Регистрация' : 'Авторизация' }}</h4>

                <transition
                    mode="out-in"
                    name="fade"
                >
                    <component
                        :is="formType"
                        @change-type="type = type === 'auth' ? 'reg' : 'auth'"
                    />
                </transition>
            </div>
        </div>
    </vue-final-modal>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import { defineAsyncComponent } from "vue";
    import FormButton from "@/components/form/FormButton";

    export default {
        name: "AuthRegModal",
        components: {
            FormButton,
            SvgIcon
        },
        inheritAttrs: true,
        emits: ['confirm', 'cancel'],
        data: () => ({
            type: 'auth'
        }),
        computed: {
            formType() {
                switch (this.type) {
                    case 'reg':
                        return defineAsyncComponent(() => import('@/components/account/RegistrationView'));

                    default:
                        return defineAsyncComponent(() => import('@/components/account/LoginView'));
                }
            }
        },
        methods: {
            closeHandler(callback) {
                callback();

                this.type = 'auth';
            }
        }
    }
</script>

<style lang="scss" scoped>
    ::v-deep(.auth-reg-modal) {
        background-color: var(--bg-secondary);
        max-height: 90%;
        margin: auto;
        overflow: hidden;
        box-shadow: 0 0 12px -8px var(--bg-transparent);
        display: flex;
        width: 100%;
        max-width: 600px;

        @include media-min($sm) {
            border-radius: 8px;
        }
    }

    .auth-reg-modal {
        &__bg {
            width: 240px;
            height: 400px;
            object-fit: cover;
            display: none;

            @include media-min($md) {
                display: block;
            }
        }

        &__content {
            flex: 1 1 100%;
            height: 400px;
            padding: 16px;
            position: relative;

            @include media-min($md) {
                padding: 24px 48px 24px 48px;
            }
        }

        &__header {
            width: 100%;
            display: flex;
            justify-content: flex-end;
        }

        &__close {
            @include css_anim();

            padding: 4px;
            width: 40px;
            height: 40px;
            position: absolute;
            top: 12px;
            right: 12px;
        }

        &__body {
            height: 100%;
            display: flex;
            flex-direction: column;
            justify-content: center;

            h4 {
                margin: 0;
            }

            .form {
                margin-top: 24px;
            }
        }
    }
</style>
