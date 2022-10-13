<template>
    <button
        :class="classList"
        :disabled="disabled"
        class="ui-button"
        type="button"
    >
        <slot/>
    </button>
</template>

<script>
    import { computed, defineComponent } from "vue";

    export default defineComponent({
        props: {
            typeLink: {
                type: Boolean,
                default: false
            },
            typeLinkFilled: {
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
            },
            useFullWidth: {
                type: Boolean,
                default: false
            }
        },

        setup(props) {
            const type = computed(() => {
                if (props.typeLink) {
                    return 'link';
                }

                if (props.typeLinkFilled) {
                    return 'link-filled';
                }

                return 'primary';
            });
            const classList = computed(() => {
                const list = [`is-${ type.value }`];

                if (props.isSmall) {
                    list.push('is-small');
                }

                if (props.useFullWidth) {
                    list.push('is-full-width');
                }

                return list;
            });

            return {
                type,
                classList
            };
        }
    });
</script>

<style lang="scss" scoped>
    .ui-button {
        @include css_anim();

        background-color: var(--primary);
        color: var(--text-btn-color);
        border-radius: 6px;
        padding: 12px;
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        margin: 0;
        line-height: 16px;

        &.is-full-width {
            display: flex;
            width: 100%;
        }

        & + & {
            margin-left: 16px;
        }

        @include media-min($xl) {
            &:hover {
                @include css_anim();

                background-color: var(--primary-hover);
            }

            &:active,
            &.is-active {
                @include css_anim();

                background-color: var(--primary-active);
            }
        }

        &:disabled {
            opacity: .6;
            background-color: var(--primary);
            cursor: not-allowed;
        }

        ::v-deep(svg) {
            width: 24px;
            height: 24px;
        }

        &.is-link {
            background-color: transparent;
            color: var(--primary);

            @include media-min($xl) {
                &:hover {
                    background-color: var(--bg-sub-menu);
                    color: var(--primary-hover);
                }

                &:active,
                &.is-active {
                    background-color: var(--bg-main);
                }
            }

            &:disabled {
                opacity: .6;
                background-color: transparent;
                color: var(--primary);
                cursor: not-allowed;
            }
        }

        &.is-link-filled {
            background-color: transparent;
            color: var(--primary);

            @include media-min($xl) {
                &:hover {
                    background-color: var(--hover);
                    color: var(--text-btn-color);
                }

                &:active,
                &.is-active {
                    background-color: var(--primary-active);
                    color: var(--text-btn-color);
                }
            }

            &:disabled {
                opacity: .6;
                background-color: transparent;
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
