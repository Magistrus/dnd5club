<template>
    <div class="trait-body">
        <detail-tob-bar
            :left="`Требования: ${trait?.requirements || 'Нет'}`"
            :source="trait?.source"
        />

        <div class="trait-body__desc content-padding">
            <raw-content :template="trait?.description"/>
        </div>
    </div>
</template>

<script>
    import DetailTobBar from "@/components/UI/DetailTobBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "TraitBody",
        components: { RawContent, DetailTobBar },
        props: {
            trait: {
                type: Object,
                default: undefined,
                required: true
            }
        }
    }
</script>
