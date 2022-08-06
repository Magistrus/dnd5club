<template>
    <button
        :class="classList"
        :disabled="disabled"
        class="form-button"
        type="button"
    >
        <slot/>
    </button>
</template>

<script>
    export default {
        name: "FormButton",
        props: {
            typeLink: {
                type: Boolean,
                default: false
            },
            typeOutline: {
                type: Boolean,
                default: false
            },
            typePrimary: {
                type: Boolean,
                default: true
            },
            disabled: {
                type: Boolean,
                default: false
            },
            isSmall: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            type() {
                if (this.typeLink) {
                    return 'link';
                }

                if (this.typeOutline) {
                    return 'outline';
                }

                return 'primary';
            },

            classList() {
                const list = [`is-${ this.type }`];

                if (this.isSmall) {
                    list.push('is-small');
                }

                return list;
            }
        }
    };
</script>

<style lang="scss" scoped>
    .form-button {
        @include css_anim();

        background-color: var(--primary);
        border: 1px solid var(--primary);
        color: var(--text-btn-color);
        border-radius: 8px;
        padding: 12px;
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        margin: 0;
        line-height: 16px;

        & + & {
            margin-left: 16px;
        }

        &:focus-within,
        &:focus,
        &:hover {
            @include css_anim();

            background-color: var(--primary-hover);
            border-color: var(--primary-hover);
        }

        &:active {
            @include css_anim();

            background-color: var(--primary-active);
            border-color: var(--primary-active);
        }

        &:disabled {
            opacity: .6;
            background-color: var(--primary);
            border-color: var(--primary);
            cursor: not-allowed;
        }

        ::v-deep(svg) {
            width: 24px;
            height: 24px;
        }

        &.is-link {
            background-color: transparent;
            border-color: transparent;
            color: var(--primary);

            &:focus-within,
            &:focus,
            &:hover {
                background-color: var(--bg-sub-menu);
                border-color: var(--bg-sub-menu);
                color: var(--primary-hover);
            }

            &:active {
                background-color: var(--bg-main);
                border-color: var(--bg-main);
            }

            &:disabled {
                opacity: .6;
                background-color: transparent;
                border-color: transparent;
                color: var(--primary);
                cursor: not-allowed;
            }
        }

        &.is-outline {
            background-color: transparent;
            border-color: var(--primary);
            color: var(--primary);

            &:focus-within,
            &:focus,
            &:hover {
                background-color: var(--bg-sub-menu);
                border-color: var(--primary-hover);
                color: var(--primary-hover);
            }

            &:active {
                background-color: var(--primary-active);
                border-color: var(--primary-active);
                color: var(--text-btn-color);
            }

            &:disabled {
                opacity: .6;
                background-color: transparent;
                border-color: var(--primary);
                color: var(--primary);
                cursor: not-allowed;
            }
        }

        &.is-small {
            padding: 8px;

            & + & {
                margin-left: 8px;
            }

            ::v-deep(svg) {
                width: 18px;
                height: 18px;
            }
        }
    }
</style>
