<template>
    <div class="custom-bookmark-button__wrapper">
        <form-button
            v-tippy="{ content: 'Добавить в закладки' }"
            class="custom-bookmark-button"
            type-link-filled
            @click.left.exact.prevent.stop="toggleSubmenu"
        >
            <svg-icon
                icon-name="arrow-2"
                :stroke-enable="false"
                fill-enable
            />
        </form-button>

        <div class="custom-bookmark-button__submenu">
            <div
                v-for="(group, key) in groups"
                :key="key"
                class="custom-bookmark-button__group"
            >
                {{ group.name }}
            </div>
        </div>
    </div>
</template>

<script>
    import FormButton from "@/components/form/FormButton";
    import {
        computed,
        defineComponent, ref, toRefs
    } from "vue";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import errorHandler from "@/common/helpers/errorHandler";
    import { useRoute } from "vue-router";

    export default defineComponent({
        components: {
            FormButton
        },
        props: {
            name: {
                type: String,
                default: ''
            },
            url: {
                type: String,
                default: ''
            }
        },
        setup(props) {
            const { name: bookmarkName } = toRefs(props);
            const bookmarksStore = useCustomBookmarkStore();
            const route = useRoute();
            const isOpen = ref(false);
            const bookmarks = ref([]);
            const groups = ref([]);
            const savedGroups = computed(() => {
                const url = route.path;
                const saved = bookmarks.value.filter(item => item.url === url);

                return saved
                    .map(item => bookmarks.value.find(bookmark => bookmark.uuid === item.parentUUID))
                    .filter(item => !!item)
                    .map(item => bookmarks.value.find(bookmark => bookmark.uuid === item.parentUUID))
                    .filter(item => !!item);
            });

            async function openSubmenu() {
                try {
                    bookmarks.value = await bookmarksStore.queryGetBookmarks();
                    groups.value = bookmarks.value.filter(item => !item.parentUUID);
                    isOpen.value = true;
                } catch (err) {
                    errorHandler(err);
                }
            }

            function closeSubmenu() {
                isOpen.value = false;
                groups.value = [];
            }

            async function toggleSubmenu() {
                if (isOpen.value) {
                    closeSubmenu();

                    return;
                }

                await openSubmenu();
            }

            // async function updateBookmark(uuid) {
            //     if (savedGroups.value.find(item => item.uuid === uuid)) {
            //         const url = route.path;
            //         const categories = bookmarks.value
            //             .filter(category => category.parentUUID === uuid)
            //             .map(category => category.uuid);
            //         const bookmark = bookmarks.value
            //             .find(item => categories.includes(item.parentUUID) && item.url === url);
            //
            //         await bookmarksStore.queryDeleteBookmark(bookmark.uuid);
            //     }
            // }

            return {
                bookmarkName,
                isOpen,
                groups,
                savedGroups,
                toggleSubmenu
            };
        }
    });
</script>

<style lang="scss" scoped>
    .custom-bookmark-button {
        z-index: 1;
        margin: 0 !important;
        width: 18px;
        padding: 12px 0;

        &__wrapper {
            margin-left: -4px;
            position: relative;
        }

        &:hover {
            position: relative;
            z-index: 2;
        }
    }
</style>
