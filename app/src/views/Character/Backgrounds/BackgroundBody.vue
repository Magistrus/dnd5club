<template>
    <div class="background-body">
        <div class="background-body__desc">
            <raw-content :url="background?.url"/>
        </div>
    </div>
</template>

<script>
    import DetailTobBar from "@/components/UI/DetailTobBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "BackgroundBody",
        components: { RawContent, DetailTobBar },
        props: {
            background: {
                type: Object,
                required: true
            }
        }
    }
</script>

<style scoped lang="scss">
    .background-body {
        padding: 24px;
    }
</style>
