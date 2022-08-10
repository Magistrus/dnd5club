<template>
    <div class="bookmarks is-default">
        <div class="bookmarks__header">
            <div class="bookmarks__info">
                <span class="bookmarks__info--title">Закладки</span>
            </div>
        </div>

        <div class="bookmarks__wrapper">
            <div class="bookmarks__body">
                <div
                    v-for="(group, groupKey) in groupBookmarks"
                    :key="group.uuid + groupKey"
                    class="bookmarks__group"
                >
                    <div class="bookmarks__group_head">
                        <div class="bookmarks__group_label">
                            {{ group.name || 'Без группы' }}
                        </div>
                    </div>

                    <div class="bookmarks__group_body">
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
                            </div>
                        </div>
                    </div>
                </div>

                <div
                    v-if="!getBookmarks?.length"
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
    import { mapActions, mapState } from "pinia";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import sortBy from "lodash/sortBy";

    export default {
        name: "DefaultBookmarks",
        computed: {
            ...mapState(useDefaultBookmarkStore, ['getBookmarks']),

            groupBookmarks() {
                const list = this.getBookmarks;
                const groups = list.filter(group => !group.parentUUID);

                return sortBy(
                    groups.map(group => ({
                        ...group,
                        children: sortBy(
                            list
                                .filter(category => category.parentUUID && category.parentUUID === group.uuid)
                                .map(category => ({
                                    ...category,
                                    children: sortBy(
                                        list.filter(bookmark => (
                                            bookmark.parentUUID
                                            && bookmark.parentUUID === category.uuid
                                        )),
                                        [o => o.order]
                                    )
                                })),
                            [o => o.order]
                        )
                    })),
                    [o => o.order]
                );
            }
        },
        methods: {
            ...mapActions(useDefaultBookmarkStore, ['removeBookmark']),

            isExternal(url) {
                return url.startsWith('http');
            }
        }
    };
</script>

<style lang="scss" scoped>
    @import "bookmarks.module";
</style>
