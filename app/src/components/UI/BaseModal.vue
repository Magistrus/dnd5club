<template>
    <vue-final-modal
        v-slot="{ params, close }"
        v-bind="$attrs"
        content-class="base-modal"
        focus-trap
    >
        <div class="base-modal__header">
            <div class="base-modal__title">
                <slot name="title"/>
            </div>

            <button
                class="base-modal__close"
                @click.left.exact.prevent="close"
            >
                <span class="base-modal__close_icon">
                    <svg-icon icon-name="close"/>
                </span>
            </button>
        </div>

        <div class="base-modal__content">
            <div class="base-modal__safe">
                <div class="base-modal__body">
                    <slot :params="params"/>
                </div>
            </div>
        </div>
    </vue-final-modal>
</template>
<script>
    import SvgIcon from "@/components/UI/SvgIcon";

    export default {
        name: "BaseModal",
        components: { SvgIcon },
        inheritAttrs: true,
        emits: ['confirm', 'cancel'],

    }
</script>

<style lang="scss" scoped>
    ::v-deep(.vfm__container) {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    ::v-deep(.base-modal) {
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

    .base-modal {
        &__header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 12px 16px;
            flex-shrink: 0;
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
                padding: 8px;

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
            padding: 16px;
        }
    }
</style>
