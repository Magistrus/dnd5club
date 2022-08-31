<template>
    <div
        class="bookmarks__cat"
    >
        <div
            class="bookmarks__cat_label"
        >
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

        <draggable
            tag="div"
            class="bookmarks__cat_body"
            :model-value="category.children"
            item-key="uuid"
            handle=".bookmarks__item_label"
            group="bookmarks"
            @change="onChangeHandler"
        >
            <template #item="{ element: bookmark }">
                <div
                    :key="bookmark.uuid + bookmark.order"
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
            </template>
        </draggable>
    </div>
</template>

<script>
    import { defineComponent } from "vue";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import draggableComponent from "vuedraggable";

    export default defineComponent({
        components: {
            Draggable: draggableComponent
        },
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
        setup(props) {
            const customBookmarkStore = useCustomBookmarkStore();

            async function onChangeHandler(e) {
                const {
                    added
                } = e;

                if (added) {
                    const {
                        element: {
                            uuid,
                            name,
                            url
                        },
                        newIndex: order
                    } = added;

                    await customBookmarkStore.queryUpdateBookmark({
                        uuid,
                        name,
                        order,
                        url,
                        parentUUID: props.category.uuid
                    });
                }
            }

            return {
                removeBookmark: customBookmarkStore.queryDeleteBookmark,
                onChangeHandler
            };
        }
    });
</script>
