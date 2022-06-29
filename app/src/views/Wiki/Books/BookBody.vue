<template>
    <div class="book-body">
        <detail-top-bar
            :left="book.type.name"
        >
            <template
                v-if="book.shortName"
                #default
            >
                Аббревиатура: {{ book.shortName }}
            </template>
        </detail-top-bar>

        <div class=" content-padding">
            <raw-content
                v-if="book?.description"
                :template="book.description"
            />
        </div>
    </div>
</template>

<script>
    import RawContent from "@/components/content/RawContent";
    import DetailTopBar from "@/components/UI/DetailTopBar";

    export default {
        name: "BookBody",
        components: { DetailTopBar, RawContent },
        props: {
            book: {
                type: Object,
                default: undefined,
                required: true
            }
        },
    }
</script>
