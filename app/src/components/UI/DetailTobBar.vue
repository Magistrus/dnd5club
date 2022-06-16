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
                >{{ source.shortName }}</span>
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
        margin: 0;
    }
</style>
