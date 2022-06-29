<template>
    <button
        class="form-button"
        :class="`is-${type}`"
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
                default: false,
            },
            typeOutline: {
                type: Boolean,
                default: false,
            },
            typePrimary: {
                type: Boolean,
                default: true,
            },
        },
        computed: {
            type() {
                if (this.typeLink) {
                    return 'link'
                }

                if (this.typeOutline) {
                    return 'outline'
                }

                return 'primary'
            }
        }
    }
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

        ::v-deep(svg) {
            width: 18px;
            height: 18px;
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
        }
    }
</style>
