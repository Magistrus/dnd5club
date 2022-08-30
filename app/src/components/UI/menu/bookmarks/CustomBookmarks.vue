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
                    :class="{ 'is-active': isOpened(group.uuid) }"
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
                            v-if="isOpened(group.uuid)"
                            class="bookmarks__group_icon only-hover is-right"
                            @click.left.exact.prevent.stop="toggleCategoryCreating(group.uuid)"
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

                        <div
                            v-if="categoryCreatingGroupUUID === group.uuid"
                            class="bookmarks__input"
                        >
                            <field-input
                                v-model="newCategoryName"
                                placeholder="Название категории"
                                autofocus
                                @keyup.enter.exact.prevent="createCategory(group.uuid)"
                            />

                            <form-button
                                type-link-filled
                                is-small
                                @click.left.exact.prevent="createCategory(group.uuid)"
                            >
                                <svg-icon icon-name="check"/>
                            </form-button>

                            <form-button
                                type-link-filled
                                is-small
                                @click.left.exact.prevent="toggleCategoryCreating(group.uuid)"
                            >
                                <svg-icon icon-name="close"/>
                            </form-button>
                        </div>
                    </div>
                </div>

                <div
                    v-if="groupCreating"
                    class="bookmarks__input"
                >
                    <field-input
                        v-model="newGroupName"
                        placeholder="Название группы"
                        autofocus
                        @keyup.enter.exact.prevent="createGroup"
                    />

                    <form-button
                        type-link-filled
                        is-small
                        @click.left.exact.prevent="createGroup"
                    >
                        <svg-icon icon-name="check"/>
                    </form-button>

                    <form-button
                        type-link-filled
                        is-small
                        @click.left.exact.prevent="toggleGroupCreating"
                    >
                        <svg-icon icon-name="close"/>
                    </form-button>
                </div>

                <form-button
                    v-else
                    class="bookmarks__new"
                    type-link-filled
                    is-small
                    @click.left.exact.prevent="toggleGroupCreating"
                >
                    <svg-icon
                        icon-name="plus"
                        :stroke-enable="false"
                        fill-enable
                    />

                    <span>Группа</span>
                </form-button>
            </div>
        </div>
    </div>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import FormButton from "@/components/form/FormButton";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import {
        computed, defineComponent, ref
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
</style>
