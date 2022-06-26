<template>
    <div class="book-body">
        <detail-tob-bar
            :left="book.type.name"
        >
            <template
                v-if="book.shortName"
                #default
            >
                Аббревиатура: {{ book.shortName }}
            </template>
        </detail-tob-bar>

        <div class=" content-padding">
            <raw-content
                v-if="book?.description"
                :template="book.description"
            />
        </div>
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
