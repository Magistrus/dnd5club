<template>
    <div
        v-if="screen"
        class="screen-body"
        :class="{ 'in-tooltip': inTooltip }"
    >
        <detail-top-bar :source="screen.source"/>

        <div class="content-padding">
            <raw-content
                v-if="screen.description"
                :template="screen.description"
            />
        </div>
    </div>
</template>

<script>
    import DetailTopBar from "@/components/UI/DetailTopBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "ScreenBody",
        components: {
            RawContent,
            DetailTopBar,
        },
        props: {
            screen: {
                type: Object,
                default: undefined,
                required: true
            },
            inTooltip: {
                type: Boolean,
                default: false,
            }
        },
        data: () => ({})
    }
</script>
