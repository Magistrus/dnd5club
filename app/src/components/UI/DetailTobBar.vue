<template>
    <div
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
                    v-tippy="{content: source.name}"
                >&nbsp;{{ source.shortName }}</span>
            </slot>
        </span>
    </div>
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
        padding: 12px 24px 12px 24px;
        justify-content: space-between;

        &.left_info {
            font-style: italic;
            margin-right: auto;
        }

        &.bg_grey{
            background: var(--bg-sub-menu);
            border-bottom: 1px solid var(--border);
        }

        @media (max-width: 1200px) {
            padding: 12px 16px 12px 16px;
        }
    }
</style>
