<template>
    <div class="background-body">
        <detail-tob-bar
            :left="background?.name?.eng"
            :source="background?.source"
        />

        <div class="background-body__desc content-padding">
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
                default: undefined,
                required: true
            }
        }
    }
</script>
