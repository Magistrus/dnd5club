<template>
    <div class="bookmarks is-custom">
        <div class="bookmarks__header">
            <div class="bookmarks__info">
                <span class="bookmarks__info--title">Группы</span>
            </div>

            <form-button
                class="bookmarks__new"
                type-link
            >
                <div class="bookmarks__new--icon">
                    <svg-icon icon-name="plus"/>
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
        defineComponent, ref, watch
    } from "vue";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";

    export default defineComponent({
        name: "CustomBookmarks",
        components: {
            SvgIcon,
            FormButton
        },
        setup() {
            const opened = ref('');
            const defaultBookmarkStore = useDefaultBookmarkStore();
            const customBookmarkStore = useCustomBookmarkStore();

            watch(customBookmarkStore.getGroupBookmarks, value => {
                opened.value = value[0]?.uuid;
            }, {
                immediate: true
            });

            const bookmarks = computed(() => [...defaultBookmarkStore.getGroupBookmarks, ...customBookmarkStore.getGroupBookmarks]);

            function toggleGroup(uuid) {
                if (this.opened) {
                    this.opened = '';

                    return;
                }

                this.opened = uuid;
            }

            return {
                bookmarks,
                opened,
                toggleGroup
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
