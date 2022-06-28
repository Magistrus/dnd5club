<template>
    <vue-final-modal
        v-slot="{ params, close }"
        v-bind="$attrs"
        content-class="base-modal"
        focus-trap
        esc-to-close
        lock-scroll
    >
        <div class="base-modal__header">
            <div class="base-modal__title">
                <slot name="title"/>
            </div>

            <form-button
                class="base-modal__close"
                type-link
                @click.left.exact.prevent="close"
            >
                <svg-icon icon-name="close"/>
            </form-button>
        </div>

        <div class="base-modal__content">
            <div class="base-modal__safe">
                <div class="base-modal__body">
                    <slot :params="params"/>
                </div>
            </div>
        </div>

        <div
            v-if="$slots.buttons"
            class="base-modal__buttons"
        >
            <slot
                name="buttons"
                :close="close"
            />
        </div>

        <div
            v-else-if="typeConfirm"
            class="base-modal__buttons"
        >
            <form-button @click.left.exact.prevent="$emit('confirm', close)">
                Применить
            </form-button>

            <form-button
                type-outline
                @click.left.exact.prevent="close"
            >
                Отменить
            </form-button>
        </div>

        <div
            v-else-if="typeRemove"
            class="base-modal__buttons"
        >
            <form-button @click.left.exact.prevent="$emit('confirm', close)">
                Удалить
            </form-button>

            <form-button
                type-outline
                @click.left.exact.prevent="close"
            >
                Отменить
            </form-button>
        </div>

        <div
            v-else-if="typeNotify"
            class="base-modal__buttons"
        >
            <form-button
                type-outline
                @click.left.exact.prevent="close"
            >
                Закрыть
            </form-button>
        </div>

        <div
            v-else-if="typeError"
            class="base-modal__buttons"
        >
            <form-button
                outline
                @click.left.exact.prevent="close"
            >
                Закрыть
            </form-button>
        </div>
    </vue-final-modal>
</template>
<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import FormButton from "@/components/form/FormButton";

    export default {
        name: "BaseModal",
        components: { FormButton, SvgIcon },
        inheritAttrs: true,
        props: {
            typeConfirm: {
                type: Boolean,
                default: false
            },
            typeRemove: {
                type: Boolean,
                default: false
            },
            typeNotify: {
                type: Boolean,
                default: false
            },
            typeError: {
                type: Boolean,
                default: false
            },
        },
        emits: ['confirm'],
        computed: {
            type() {
                if (this.typeConfirm) {
                    return 'confirm'
                }

                if (this.typeRemove) {
                    return 'remove'
                }

                if (this.typeNotify) {
                    return 'notify'
                }

                if (this.typeError) {
                    return 'error'
                }

                return ''
            }
        }
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
        }

        &__close {
            margin-left: 16px;
        }

        &__content {
            overflow: auto;
            width: 100%;
            flex: 1;
        }

        &__buttons {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            padding: 16px;
            flex-shrink: 0;
            background-color: var(--bg-sub-menu);

            > * + * {
                margin-left: 8px;
                flex-shrink: 0;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        }
    }
</style>
