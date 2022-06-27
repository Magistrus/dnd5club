<template>
    <button
        class="form-button"
        :class="{ 'is-link': isLink }"
        type="button"
    >
        <slot/>
    </button>
</template>

<script>
    export default {
        name: "FormButton",
        props: {
            isLink: {
                type: Boolean,
                default: false
            }
        }
    }
</script>

<style lang="scss" scoped>
    .form-button {
        @include css_anim();

        background-color: var(--primary);
        padding: 12px 24px;
        display: block;
        border: 0;
        cursor: pointer;
        color: var(--text-btn-color);
        border-radius: 8px;
        margin: 0;

        & + & {
            margin-left: 16px;
        }

        &:focus-within,
        &:focus,
        &:hover {
            @include css_anim();

            background-color: var(--primary-hover);
        }

        &:active {
            @include css_anim();

            background-color: var(--primary-active);
        }

        &.is-link {
            background: transparent;
            color: var(--primary);

            &:focus-within,
            &:focus,
            &:hover {
                background-color: var(--bg-sub-menu);
                color: var(--primary-hover);
            }

            &:active {
                background-color: var(--bg-main);
            }
        }
    }
</style>
