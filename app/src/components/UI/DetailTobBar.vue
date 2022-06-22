<template>
    <p
        class="row_info"
        :class="{ 'bg_grey': bgGrey }"
    >
        <span
            v-if="left"
            class="left_info"
        >
            {{ left }}
        </span>

        <span>
            <slot>
                Источник:

                <span
                    v-if="source.homebrew"
                    class="homebrew_text"
                >Homebrew</span>

                <span
                    v-tooltip="{content: source.name}"
                    class="tip"
                >&nbsp;{{ source.shortName }}</span>
            </slot>
        </span>
    </p>
</template>

<script>
    export default {
        name: "DetailTobBar",
        props: {
            left: {
                type: String,
                default: ''
            },
            source: {
                type: Object,
                default: () => ({})
            },
            bgGrey: {
                type: Boolean,
                default: true
            },
        }
    }
</script>

<style lang="scss" scoped>
    .row_info {
        display: flex;
        margin-top: -24px;
        margin-left: -24px;
        margin-right: -24px;
        margin-bottom: 0;
        padding: 12px 24px 12px 24px;

        &.left_info {
            font-style: italic;
            margin-right: auto;
        }

        &.bg_grey{
            background: var(--bg-sub-menu);
            border-bottom: 1px solid var(--border);
            margin-bottom: 16px;
        }

        @media (max-width: 1200px) {
            margin-left: -16px;
            margin-right: -16px;
            padding: 12px 16px 12px 16px;
        }
    }
</style>
