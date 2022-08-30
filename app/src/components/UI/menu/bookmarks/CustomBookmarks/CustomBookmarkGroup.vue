<template>
    <div
        :class="{ 'is-active': isOpened }"
        class="bookmarks__group"
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
            <custom-bookmark-category
                v-for="(category, catKey) in group.children"
                :key="category.uuid + catKey"
                :category="category"
                :group-uuid="group.uuid"
            />

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

    export default defineComponent({
        components: {
            CustomBookmarkCategory,
            FieldInput,
            FormButton
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

            return {
                isOpened,
                isCategoryCreating,
                newCategoryName,
                enableCategoryCreating,
                disableCategoryCreating,
                createCategory,
                removeBookmark: customBookmarkStore.queryDeleteBookmark
            };
        }
    });
</script>

<style lang="scss" scoped>

</style>
