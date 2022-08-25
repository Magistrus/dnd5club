<template>
    <div class="bookmarks is-custom">
        <div class="bookmarks__header">
            <div class="bookmarks__info">
                <span class="bookmarks__info--title">Группы</span>
            </div>

            <form-button
                class="bookmarks__new"
                type-link
                @click.left.exact.prevent="toggleGroupCreating"
            >
                <div class="bookmarks__new--icon">
                    <svg-icon :icon-name="groupCreating ? 'close' : 'plus'"/>
                </div>

                <span v-if="false">Добавить</span>
            </form-button>

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
                            :class="{ 'is-active': group.uuid === opened }"
                        >
                            <svg-icon icon-name="arrow-stroke"/>
                        </div>

                        <div class="bookmarks__group_label">
                            {{ group.name || 'Без категории' }}
                        </div>
                    </div>

                    <div
                        v-if="group.uuid === opened"
                        class="bookmarks__group_body"
                    >
                        <div
                            v-for="(category, catKey) in group.children"
                            :key="category.uuid + catKey"
                            class="bookmarks__cat"
                        >
                            <div class="bookmarks__cat_label">
                                {{ category.name }}
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
                                    >
                                        <svg-icon icon-name="close"/>
                                    </div>
                                </div>

                                <div
                                    v-if="!category.children?.length"
                                    class="bookmarks__info"
                                >
                                    <div class="bookmarks__info--desc">
                                        Здесь пока пусто
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div
                            v-if="!group.children?.length"
                            class="bookmarks__info"
                        >
                            <div class="bookmarks__info--desc">
                                Здесь пока пусто
                            </div>
                        </div>
                    </div>
                </div>

                <field-input
                    v-if="groupCreating"
                    v-model="newGroupName"
                    @keyup.enter.exact.prevent="createGroup"
                />

                <div
                    v-if="!bookmarks?.length"
                    class="bookmarks__info"
                >
                    <div class="bookmarks__info--desc">
                        Здесь пока пусто
                    </div>
                </div>
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
    import { v4 as uuidV4 } from 'uuid';
    import FieldInput from "@/components/form/FieldType/FieldInput";
    import orderBy from 'lodash/orderBy';

    export default defineComponent({
        name: "CustomBookmarks",
        components: {
            FieldInput,
            SvgIcon,
            FormButton
        },
        setup() {
            const opened = ref('');
            const customBookmarkStore = useCustomBookmarkStore();
            const bookmarks = computed(() => {
                const list = [];

                if (opened.value) {
                    const selected = customBookmarkStore.getMergedBookmarkGroups
                        .find(item => item.uuid === opened.value);

                    if (!selected) {
                        return customBookmarkStore.getMergedBookmarkGroups;
                    }

                    list.push(selected);

                    list.push(...orderBy(
                        customBookmarkStore.getMergedBookmarkGroups.filter(item => item.uuid !== opened.value),
                        [o => o.order]
                    ));

                    return list;
                }

                return customBookmarkStore.getMergedBookmarkGroups;
            });

            function toggleGroup(uuid) {
                if (opened.value && opened.value === uuid) {
                    opened.value = '';

                    return;
                }

                opened.value = uuid;
            }

            const firstGroup = customBookmarkStore.getMergedBookmarkGroups[0];

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
                await customBookmarkStore.queryAddBookmark({
                    name: newGroupName.value,
                    order: customBookmarkStore.getGroupBookmarks.length,
                    uuid: uuidV4()
                });

                toggleGroupCreating();
            }

            return {
                bookmarks,
                opened,
                toggleGroup,
                newGroupName,
                groupCreating,
                toggleGroupCreating,
                createGroup
            };
        }
    });
</script>

<style lang="scss" scoped>
    @import "bookmarks.module";

    .bookmarks {
        background-color: var(--bg-sub-menu);
    }
</style>
