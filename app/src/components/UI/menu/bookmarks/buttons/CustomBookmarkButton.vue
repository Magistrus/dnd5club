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

        <div
            v-if="isOpen"
            class="custom-bookmark-button__submenu"
        >
            <div
                v-for="(group, key) in groups"
                :key="key"
                class="custom-bookmark-button__group"
                :class="{ 'is-saved': isSaved(group.uuid) }"
                @click.left.exact.prevent="updateBookmark(group.uuid)"
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
            const getPath = () => (typeof props.url === "string" && props.url !== '' ? props.url : route.path);
            const isOpen = ref(false);
            const bookmarks = ref([]);
            const groups = computed(() => bookmarksStore.getGroups);
            const savedGroups = computed(() => {
                const url = route.path;
                const saved = bookmarks.value.filter(item => item.url === url);

                return saved
                    .map(item => bookmarks.value.find(bookmark => bookmark.uuid === item.parentUUID))
                    .filter(item => !!item)
                    .map(item => bookmarks.value.find(bookmark => bookmark.uuid === item.parentUUID))
                    .filter(item => !!item);
            });
            const isSaved = uuid => bookmarksStore.isBookmarkSavedInGroup(getPath(), uuid);

            async function openSubmenu() {
                try {
                    await bookmarksStore.queryGetBookmarks();

                    isOpen.value = true;
                } catch (err) {
                    errorHandler(err);
                }
            }

            function closeSubmenu() {
                isOpen.value = false;
            }

            async function toggleSubmenu() {
                if (isOpen.value) {
                    closeSubmenu();

                    return;
                }

                await openSubmenu();
            }

            async function updateBookmark(groupUUID) {
                await bookmarksStore.updateBookmarkInGroup({
                    url: getPath(),
                    name: props.name,
                    groupUUID
                });
            }

            return {
                bookmarkName,
                isOpen,
                isSaved,
                groups,
                savedGroups,
                toggleSubmenu,
                updateBookmark
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

        &__submenu {
            position: absolute;
            background-color: var(--bg-sub-menu);
            padding: 8px;
            border-radius: 6px;
            box-shadow: 0px 5px 30px #00000038;
            right: 0;
        }

        &__group {
            padding: 8px 6px;
            border-radius: 6px;
            cursor: pointer;
            min-width: 100px;

            &.is-saved {
                background-color: var(--primary);
            }

            &:hover {
                background-color: var(--hover);
            }
        }
    }
</style>
