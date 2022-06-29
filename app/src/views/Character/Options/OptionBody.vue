<template>
    <div class="option-body">
        <detail-top-bar
            :left="`Требования: ${option?.requirements || 'Нет'}`"
            :source="option?.source"
        />

        <div class="option-body__desc content-padding">
            <raw-content :template="option?.description"/>
        </div>
    </div>
</template>

<script>
    import RawContent from "@/components/content/RawContent";
    import DetailTopBar from "@/components/UI/DetailTopBar";

    export default {
        name: "OptionBody",
        components: { DetailTopBar, RawContent },
        props: {
            option: {
                type: Object,
                default: undefined,
                required: true
            }
        }
    }
</script>
