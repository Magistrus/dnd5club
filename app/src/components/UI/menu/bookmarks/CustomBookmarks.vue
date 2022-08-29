<template>
    <div class="bookmarks is-custom">
        <div class="bookmarks__header">
            <div class="bookmarks__info">
                <span class="bookmarks__info--title">Группы</span>
            </div>

            <label
                v-if="false"
                class="bookmarks__search"
            >
                <span class="bookmarks__search--icon">
                    <svg-icon icon-name="search"/>
                </span>
            </label>
        </div>

        <div class="bookmarks__wrapper">
            <div class="bookmarks__body">
                <div
                    v-for="(group, groupKey) in bookmarks"
                    :key="group.uuid + groupKey"
                    class="bookmarks__group"
                >
                    <div
                        class="bookmarks__group_head"
                        @click.left.exact.prevent="toggleGroup(group.uuid)"
                    >
                        <div
                            class="bookmarks__group_icon"
                            :class="{ 'is-active': isOpened(group.uuid) }"
                        >
                            <svg-icon icon-name="arrow-stroke"/>
                        </div>

                        <div class="bookmarks__group_label">
                            {{ group.name || 'Без категории' }}
                        </div>

                        <div
                            class="bookmarks__group_icon is-right only-hover"
                            @click.left.exact.prevent.stop="removeBookmark(group.uuid)"
                        >
                            <svg-icon icon-name="close"/>
                        </div>
                    </div>

                    <div
                        v-if="isOpened(group.uuid)"
                        class="bookmarks__group_body"
                    >
                        <div
                            v-for="(category, catKey) in group.children"
                            :key="category.uuid + catKey"
                            class="bookmarks__cat"
                        >
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

                        <form-button
                            v-if="categoryCreatingGroupUUID !== group.uuid"
                            type-link-filled
                            is-small
                            @click.left.exact.prevent="toggleCategoryCreating(group.uuid)"
                        >
                            Добавить категорию
                        </form-button>

                        <field-input
                            v-else-if="categoryCreatingGroupUUID === group.uuid"
                            v-model="newCategoryName"
                            @keyup.enter.exact.prevent="createCategory(group.uuid)"
                        />
                    </div>
                </div>

                <form-button
                    v-if="!groupCreating"
                    type-link-filled
                    is-small
                    @click.left.exact.prevent="toggleGroupCreating"
                >
                    Добавить группу
                </form-button>

                <field-input
                    v-else
                    v-model="newGroupName"
                    @keyup.enter.exact.prevent="createGroup"
                    @blur="toggleGroupCreating"
                />
            </div>
        </div>
    </div>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import FormButton from "@/components/form/FormButton";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import {
        computed,
        defineComponent, ref
    } from "vue";
    import FieldInput from "@/components/form/FieldType/FieldInput";

    export default defineComponent({
        name: "CustomBookmarks",
        components: {
            FieldInput,
            SvgIcon,
            FormButton
        },
        setup() {
            const customBookmarkStore = useCustomBookmarkStore();
            const bookmarks = computed(() => customBookmarkStore.getGroupBookmarks);
            const opened = ref([]);
            const isOpened = uuid => opened.value.findIndex(item => item === uuid) > -1;

            function toggleGroup(uuid) {
                if (opened.value.findIndex(item => item === uuid) > -1) {
                    opened.value = opened.value.filter(item => item !== uuid);

                    return;
                }

                opened.value.push(uuid);
            }

            const firstGroup = customBookmarkStore.getGroupBookmarks[0];

            if (firstGroup) {
                toggleGroup(firstGroup.uuid);
            }

            const newGroupName = ref('');
            const groupCreating = ref(false);

            function toggleGroupCreating() {
                groupCreating.value = !groupCreating.value;

                if (!groupCreating.value) {
                    newGroupName.value = '';
                }
            }

            async function createGroup() {
                const group = await customBookmarkStore.queryAddBookmark({
                    name: newGroupName.value,
                    order: customBookmarkStore.getGroupBookmarks.length
                });

                opened.value.push(group.uuid);

                toggleGroupCreating();
            }

            const newCategoryName = ref('');
            const categoryCreatingGroupUUID = ref('');

            function toggleCategoryCreating(groupUUID) {
                newCategoryName.value = '';

                if (!groupUUID) {
                    categoryCreatingGroupUUID.value = '';
                }

                if (
                    !categoryCreatingGroupUUID.value
                    || (categoryCreatingGroupUUID.value && categoryCreatingGroupUUID.value !== groupUUID)
                ) {
                    categoryCreatingGroupUUID.value = groupUUID;

                    return;
                }

                categoryCreatingGroupUUID.value = '';
            }

            async function createCategory(groupUUID) {
                const order = customBookmarkStore.getBookmarks
                    .filter(item => item.parentUUID === groupUUID)
                    .length;

                await customBookmarkStore.queryAddBookmark({
                    name: newCategoryName.value,
                    parentUUID: groupUUID,
                    order
                });

                toggleCategoryCreating(groupUUID);
            }

            return {
                bookmarks,
                isOpened,
                toggleGroup,
                removeBookmark: customBookmarkStore.queryDeleteBookmark,

                // Group creating
                newGroupName,
                groupCreating,
                toggleGroupCreating,
                createGroup,

                // Category creating
                newCategoryName,
                categoryCreatingGroupUUID,
                toggleCategoryCreating,
                createCategory
            };
        }
    });
</script>

<style lang="scss" scoped>
    @import "bookmarks.module";

    .bookmarks {
        background-color: var(--bg-sub-menu);

          &__group {
            &_body {
                padding: 0 8px 0 10px;
                margin-left: 16px;
                border-left: 1px solid var(--primary-active);
            }
        }

        .form-button {
            width: 100%;
        }
    }
</style>
