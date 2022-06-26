<template>
    <vue-final-modal
        v-slot="{ params, close }"
        v-bind="$attrs"
        content-class="auth-reg-modal"
        focus-trap
        esc-to-close
    >
        <div class="auth-reg-modal__header">
            <div class="auth-reg-modal__title">
                <slot name="title"/>
            </div>

            <button
                class="auth-reg-modal__close"
                @click.left.exact.prevent="close"
            >
                <span class="auth-reg-modal__close_icon">
                    <svg-icon icon-name="close"/>
                </span>
            </button>
        </div>

        <div class="auth-reg-modal__content">
            <div class="auth-reg-modal__safe">
                <div class="auth-reg-modal__body">
                    <slot :params="params"/>
                </div>
            </div>
        </div>
    </vue-final-modal>
</template>
<script>
    import SvgIcon from "@/components/UI/SvgIcon";

    export default {
        name: "AuthRegModal",
        components: { SvgIcon },
        inheritAttrs: true,
        emits: ['confirm', 'cancel'],

    }
</script>

<style lang="scss" scoped>
    ::v-deep(.auth-reg-modal) {
        background-color: var(--bg-secondary);
        max-width: 90%;
        max-height: 90%;
        margin: auto;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 0 12px -8px var(--bg-transparent);
        display: flex;
        flex-direction: column;
    }

    .auth-reg-modal {
        &__header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 16px 24px;
            flex-shrink: 0;
            background-color: var(--hover);

            @media (max-width: 1200px) {
                padding: 12px 16px;
            }
        }

        &__title {
            color: var(--text-color-title);
            font-size: 22px;
            line-height: 28px;
            padding-bottom: 4px;
        }

        &__close {
            @include css_anim();

            margin-left: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 0;
            background-color: transparent;
            cursor: pointer;
            color: var(--primary);
            appearance: none;
            padding: 0;
            border-radius: 8px;
            overflow: hidden;

            &_icon {
                width: 32px;
                height: 32px;

                ::v-deep(> svg) {
                    width: 100%;
                    height: 100%;
                }
            }

            &:hover {
                @include media-min($lg) {
                    color: var(--primary-hover);
                    background-color: var(--bg-secondary);
                }
            }
        }

        &__content {
            overflow: auto;
            width: 100%;
            flex: 1;
        }

        &__safe {
            // padding: 16px;
        }
    }
</style>
