<template>
    <div class="bookmarks is-default">
        <div class="bookmarks__header">
            <div class="bookmarks__info">
                <span class="bookmarks__info--title">Закладки</span>
            </div>
        </div>

        <div class="bookmarks__body">
            <div class="bookmarks__group">
                <div class="bookmarks__group_label">
                    Общие
                </div>

                <div class="bookmarks__group_body">
                    <div
                        v-for="(group, groupKey) in getBookmarks"
                        :key="group.name + group.order + groupKey"
                        class="bookmarks__cat"
                    >
                        <div class="bookmarks__cat_label">
                            {{ group.name }}
                        </div>

                        <div class="bookmarks__cat_body">
                            <div
                                v-for="link in group.childList"
                                :key="link.url + group.order"
                                class="bookmarks__item"
                            >
                                <a
                                    :href="link.url"
                                    :target="isExternal(link.url) ? '_blank' : '_self'"
                                    class="bookmarks__item_label"
                                >{{ link.name }}</a>

                                <div
                                    class="bookmarks__item_icon only-hover is-right"
                                    @click.left.exact.stop.prevent="removeBookmark(link.url)"
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
                <span class="bookmarks__info--desc">Здесь пока пусто</span>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapActions, mapState } from "pinia";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";

    export default {
        name: "DefaultBookmarks",
        computed: {
            ...mapState(useDefaultBookmarkStore, ['getBookmarks'])
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
