<template>
    <div class="bookmarks__cat">
        <div class="bookmarks__cat_label">
            <div class="bookmarks__cat_label_name">
                {{ category.name }}
            </div>

            <div
                class="bookmarks__cat_label_icon only-hover is-right"
                @click.left.exact.prevent="removeBookmark(category.uuid)"
            >
                <svg-icon icon-name="close"/>
            </div>
        </div>

        <div class="bookmarks__cat_body">
            <div
                v-for="(bookmark, bookmarkKey) in category.children"
                :key="bookmark.uuid + bookmarkKey"
                class="bookmarks__item"
            >
                <a
                    :href="bookmark.url"
                    class="bookmarks__item_label"
                >{{ bookmark.name }}</a>

                <div
                    class="bookmarks__item_icon only-hover is-right"
                    @click.left.exact.prevent="removeBookmark(bookmark.uuid)"
                >
                    <svg-icon icon-name="close"/>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { defineComponent } from "vue";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";

    export default defineComponent({
        props: {
            groupUuid: {
                type: String,
                default: ''
            },
            category: {
                type: Object,
                default: () => ({})
            },
            creating: {
                type: Boolean,
                default: false
            }
        },
        setup() {
            const customBookmarkStore = useCustomBookmarkStore();

            return {
                removeBookmark: customBookmarkStore.queryDeleteBookmark
            };
        }
    });
</script>
