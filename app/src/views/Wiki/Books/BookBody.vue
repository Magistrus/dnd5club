<template>
    <div class="book-body">
        <detail-tob-bar
            :left="book.type"
        >
            {{ book.book }}
        </detail-tob-bar>

        <raw-content
            v-if="book?.description"
            :template="book.description"
        />
    </div>
</template>

<script>
    import DetailTobBar from "@/components/UI/DetailTobBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "BookBody",
        components: { RawContent, DetailTobBar },
        props: {
            book: {
                type: Object,
                default: undefined,
                required: true
            }
        },
    }
</script>

<style scoped lang="scss">
    .book-body {
        padding: 24px;
    }
</style>
