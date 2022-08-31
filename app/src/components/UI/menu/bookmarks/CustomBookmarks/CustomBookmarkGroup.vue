<template>
    <div
        class="bookmarks__group"
        :class="{ 'is-active': isOpened }"
    >
        <div
            class="bookmarks__group_head"
            @click.left.exact.prevent="isOpened = !isOpened"
        >
            <div
                class="bookmarks__group_icon"
                :class="{ 'is-active': isOpened }"
            >
                <svg-icon icon-name="arrow-stroke"/>
            </div>

            <div class="bookmarks__group_label">
                {{ group.name || 'Без категории' }}
            </div>

            <div
                v-if="isOpened"
                class="bookmarks__group_icon only-hover is-right"
                @click.left.exact.prevent.stop="enableCategoryCreating"
            >
                <svg-icon
                    icon-name="plus"
                    :stroke-enable="false"
                    fill-enable
                />
            </div>

            <div
                class="bookmarks__group_icon is-right only-hover"
                @click.left.exact.prevent.stop="removeBookmark(group.uuid)"
            >
                <svg-icon icon-name="close"/>
            </div>
        </div>

        <div
            v-if="isOpened"
            class="bookmarks__group_body"
        >
            <draggable
                tag="div"
                :model-value="group.children"
                item-key="uuid"
                handle=".bookmarks__cat_label"
                group="category"
                @change="onChangeHandler"
            >
                <template #item="{ element: category }">
                    <custom-bookmark-category
                        :key="category.uuid + category.order"
                        :category="category"
                        :group-uuid="group.uuid"
                    />
                </template>
            </draggable>

            <div
                v-if="isCategoryCreating"
                class="bookmarks__input"
            >
                <field-input
                    v-model="newCategoryName"
                    placeholder="Название категории"
                    autofocus
                    @keyup.enter.exact.prevent="createCategory"
                />

                <form-button
                    type-link-filled
                    is-small
                    @click.left.exact.prevent="createCategory"
                >
                    <svg-icon icon-name="check"/>
                </form-button>

                <form-button
                    type-link-filled
                    is-small
                    @click.left.exact.prevent="disableCategoryCreating"
                >
                    <svg-icon icon-name="close"/>
                </form-button>
            </div>
        </div>
    </div>
</template>

<script>
    import { defineComponent, ref } from "vue";
    import FieldInput from "@/components/form/FieldType/FieldInput";
    import FormButton from "@/components/form/FormButton";
    import CustomBookmarkCategory from "@/components/UI/menu/bookmarks/CustomBookmarks/CustomBookmarkCategory";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import draggableComponent from 'vuedraggable';

    export default defineComponent({
        components: {
            CustomBookmarkCategory,
            FieldInput,
            FormButton,
            Draggable: draggableComponent
        },
        props: {
            group: {
                type: Object,
                default: () => ({})
            },
            isFirst: {
                type: Boolean,
                default: false
            }
        },
        setup(props) {
            const customBookmarkStore = useCustomBookmarkStore();
            const isOpened = ref(props.isFirst);
            const isCategoryCreating = ref(false);
            const newCategoryName = ref('');

            function enableCategoryCreating() {
                isCategoryCreating.value = true;
                newCategoryName.value = '';
            }

            function disableCategoryCreating() {
                isCategoryCreating.value = false;
                newCategoryName.value = '';
            }

            async function createCategory() {
                await customBookmarkStore.queryAddBookmark({
                    name: newCategoryName.value,
                    order: customBookmarkStore.getGroupBookmarks.length
                });

                disableCategoryCreating();
            }

            async function onChangeHandler(e) {
                const {
                    added

                    // moved
                } = e;

                if (added) {
                    const {
                        element: { uuid, name },
                        newIndex: order
                    } = added;

                    await customBookmarkStore.queryUpdateBookmark({
                        uuid,
                        name,
                        order,
                        parentUUID: props.group.uuid
                    });
                }
            }

            return {
                isOpened,
                isCategoryCreating,
                newCategoryName,
                enableCategoryCreating,
                disableCategoryCreating,
                createCategory,
                removeBookmark: customBookmarkStore.queryDeleteBookmark,
                onChangeHandler
            };
        }
    });
</script>

<style lang="scss" scoped>

</style>
