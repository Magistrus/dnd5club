<template>
    <form-button
        v-tippy="{ content: 'Добавить в закладки' }"
        class="bookmark-save-button"
        type-link-filled
        @click.left.exact.prevent.stop="updateDefaultBookmark($route.path, bookmarkName)"
    >
        <svg-icon
            :icon-name="isDefaultBookmarkSaved($route.path) ? 'bookmark-filled' : 'bookmark'"
            :stroke-enable="false"
            fill-enable
        />
    </form-button>

    <div
        v-if="isAuthenticated"
        class="bookmark-submenu-button__wrapper"
    >
        <form-button
            v-tippy="{ content: 'Добавить в закладки' }"
            class="bookmark-submenu-button"
            type-link-filled
            @click.left.exact.prevent.stop="isOpen = !isOpen"
        >
            <svg-icon
                icon-name="arrow-2"
                :stroke-enable="false"
                fill-enable
            />
        </form-button>
    </div>
</template>

<script>
    import FormButton from "@/components/form/FormButton";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import { useUserStore } from "@/store/UI/UserStore";
    import {
        defineComponent, onBeforeMount, ref, toRefs
    } from "vue";
    import errorHandler from "@/common/helpers/errorHandler";

    export default defineComponent({
        components: {
            FormButton
        },
        props: {
            name: {
                type: String,
                default: ''
            }
        },
        setup(props) {
            const { name: bookmarkName } = toRefs(props);
            const {
                isAuthenticated,
                getUserStatus,
                $onAction: $onUserStoreAction
            } = useUserStore();
            const {
                isBookmarkSaved: isDefaultBookmarkSaved,
                updateBookmark: updateDefaultBookmark

            } = useDefaultBookmarkStore();
            const {
                // queryGetBookmarks,
                // querySaveBookmarks,
                // queryAddBookmark,
                // queryDeleteBookmark,
                queryMergeDefaultBookmark
            } = useCustomBookmarkStore();
            const isOpen = ref(false);
            const unsubscribeAuthAction = $onUserStoreAction(({ name, after }) => {
                after(async () => {
                    try {
                        switch (name) {
                            case 'authorization':
                                await queryMergeDefaultBookmark();

                                unsubscribeAuthAction();

                                break;
                            default:
                                break;
                        }
                    } catch (err) {
                        errorHandler(err);
                    }
                });
            });

            onBeforeMount(async () => {
                await getUserStatus();
            });

            return {
                bookmarkName,
                isOpen,
                isAuthenticated,
                isDefaultBookmarkSaved,
                updateDefaultBookmark
            };
        }
    });
</script>

<style lang="scss" scoped>
    .bookmark-save-button,
    .bookmark-submenu-button {
        z-index: 1;

        &:hover {
            position: relative;
            z-index: 2;
        }
    }

    .bookmark-save-button {
        width: 28px;
    }

    .bookmark-submenu-button {
        margin: 0 !important;
        width: 18px;
        padding: 12px 0;

        &__wrapper {
            margin-left: -4px;
            position: relative;
        }
    }
</style>
