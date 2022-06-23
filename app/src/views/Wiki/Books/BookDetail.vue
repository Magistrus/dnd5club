<template>
    <div class="book-detail">
        <section-header
            :subtitle="book?.name?.eng || ''"
            :title="book?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
        />

        <book-body
            v-if="book"
            :book="book"
        />
    </div>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import errorHandler from "@/common/helpers/errorHandler";
    import { useBooksStore } from "@/store/Wiki/BooksStore";
    import BookBody from "@/views/Wiki/Books/BookBody";

    export default {
        name: 'BookDetail',
        components: { BookBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewBook(to.path);

            next();
        },
        data: () => ({
            bookStore: useBooksStore(),
            book: undefined,
            loading: false,
            error: false,
        }),
        async mounted() {
            await this.loadNewBook(this.$route.path);
        },
        methods: {
            async loadNewBook(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.book = await this.bookStore.bookInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err);
                }
            },

            close() {
                this.$router.push({ name: 'books' })
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
