<template>
    <component
        :is="layout"
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="booksQuery"
        @update="booksQuery"
        @list-end="nextPage"
    >
        <div
            v-for="(group, groupKey) in books"
            :key="groupKey"
            class="books-group"
        >
            <div class="books-group__name">
                {{ group.name }}
            </div>

            <div class="books-group__list">
                <book-link
                    v-for="book in group.list"
                    :key="book.url"
                    :book="book"
                    :in-tab="inTab"
                    :to="{ path: book.url }"
                />
            </div>
        </div>
    </component>
</template>

<script>
    import { shallowRef } from "vue";
    import sortBy from "lodash/sortBy";
    import { mapState } from "pinia";
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { useBooksStore } from "@/store/Wiki/BooksStore";
    import BookLink from "@/views/Wiki/Books/BookLink";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'BooksView',
        components: {
            BookLink,
            TabLayout,
            ContentLayout
        },
        props: {
            inTab: {
                type: Boolean,
                default: false
            },
            storeKey: {
                type: String,
                default: ''
            },
            customFilter: {
                type: Object,
                default: undefined
            }
        },
        data: () => ({
            booksStore: useBooksStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            ...mapState(useUIStore, ['isMobile']),

            filter() {
                return this.booksStore.getFilter || undefined;
            },

            books() {
                const books = [];
                const types = [];

                if (!this.booksStore.getBooks) {
                    return books;
                }

                for (const book of this.booksStore.getBooks) {
                    if (types.find(obj => obj.name === book.type.name)) {
                        continue;
                    }

                    types.push(book.type);
                }

                for (const type of sortBy(types, [o => o.order])) {
                    books.push({
                        name: type.name,
                        list: this.booksStore.getBooks.filter(book => book.type.name === type.name)
                    });
                }

                return books;
            },

            showRightSide() {
                return this.$route.name === 'bookDetail';
            },

            layout() {
                return this.inTab
                    ? this.layoutComponents.tab
                    : this.layoutComponents.content;
            }
        },
        watch: {
            storeKey: {
                async handler() {
                    await this.init();
                }
            },
            customFilter: {
                deep: true,
                async handler() {
                    await this.init();
                }
            }
        },
        async mounted() {
            await this.init();

            if (!this.isMobile && this.books.length && this.$route.name === 'books') {
                await this.$router.push({ path: this.books[0]?.list[0]?.url });
            }
        },
        beforeUnmount() {
            this.booksStore.clearStore();
        },
        methods: {
            async booksQuery() {
                await this.booksStore.initBooks();
            },

            async init() {
                await this.booksStore.initFilter(this.storeKey, this.customFilter);
                await this.booksStore.initBooks();
            },

            async nextPage() {
                await this.booksStore.nextPage();
            }
        }
    };
</script>
