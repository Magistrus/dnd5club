<template>
    <div class="background-body">
        <detail-top-bar
            :left="background?.name?.eng"
            :source="background?.source"
        />

        <div class="background-body__desc content-padding">
            <raw-content :url="background?.url"/>
        </div>
    </div>
</template>

<script>
    import RawContent from "@/components/content/RawContent";
    import DetailTopBar from "@/components/UI/DetailTopBar";

    export default {
        name: "BackgroundBody",
        components: { DetailTopBar, RawContent },
        props: {
            background: {
                type: Object,
                default: undefined,
                required: true
            }
        }
    }
</script>
