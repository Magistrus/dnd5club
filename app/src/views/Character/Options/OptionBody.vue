<template>
    <div class="option-body">
        <detail-tob-bar
            :left="`Требования: ${option?.requirements || 'Нет'}`"
            :source="option?.source"
        />

        <div class="option-body__desc content-padding">
            <raw-content :template="option?.description"/>
        </div>
    </div>
</template>

<script>
    import DetailTobBar from "@/components/UI/DetailTobBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "OptionBody",
        components: { RawContent, DetailTobBar },
        props: {
            option: {
                type: Object,
                default: undefined,
                required: true
            }
        }
    }
</script>
