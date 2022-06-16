<template>
    <div class="rule-body">
        <detail-tob-bar
            :left="`Категория: ${rule.type}`"
            :source="rule.source"
        />

        <raw-content
            v-if="rule?.description"
            :template="rule.description"
        />
    </div>
</template>

<script>
    import DetailTobBar from "@/components/UI/DetailTobBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "RuleBody",
        components: { RawContent, DetailTobBar },
        props: {
            rule: {
                type: Object,
                default: undefined,
                required: true
            }
        },
    }
</script>

<style scoped lang="scss">
    .rule-body {
        padding: 24px;
    }
</style>
