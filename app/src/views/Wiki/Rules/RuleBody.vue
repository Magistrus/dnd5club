<template>
    <div class="rule-body">
        <detail-tob-bar
            :left="`Категория: ${rule.type}`"
            :source="rule.source"
        />

        <div class=" content-padding">
            <raw-content
                v-if="rule?.description"
                :template="rule.description"
            />
        </div>
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
