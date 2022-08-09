<template>
    <form-button
        v-tippy="{ content: 'Добавить в закладки' }"
        class="bookmark-save-button"
        type-link-filled
        @click.left.exact.prevent.stop="updateDefaultBookmark($route.path, title)"
    >
        <svg-icon
            :icon-name="isDefaultBookmarkSaved($route.path) ? 'bookmark-filled' : 'bookmark'"
            :stroke-enable="false"
            fill-enable
        />
    </form-button>

    <form-button
        v-if="isAuthorized"
        class="bookmark-submenu-button"
        type-link-filled
        @click.left.exact.prevent.stop="updateDefaultBookmark($route.path, title)"
    >
        <svg-icon
            icon-name="arrow-stroke"
        />
    </form-button>
</template>

<script>
    import { mapActions, mapState } from "pinia/dist/pinia";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import FormButton from "@/components/form/FormButton";
    import { useUserStore } from "@/store/UI/UserStore";

    export default {
        name: "BookmarkSaveButton",
        components: {
            FormButton
        },
        computed: {
            ...mapState(useUserStore, ['isAuthorized']),
            ...mapState(useDefaultBookmarkStore, {
                isDefaultBookmarkSaved: 'isBookmarkSaved'
            })
        },
        async beforeMount() {
            await this.updateUserFromSession();
        },
        methods: {
            ...mapActions(useUserStore, ['updateUserFromSession']),
            ...mapActions(useDefaultBookmarkStore, {
                updateDefaultBookmark: 'updateBookmark'
            }),
            ...mapActions(useCustomBookmarkStore, [
                'queryGetBookmarks',
                'querySaveBookmarks',
                'queryAddBookmark',
                'queryDeleteBookmark'
            ])
        }
    };
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

    .bookmark-submenu-button {
        margin-left: -16px !important;
    }
</style>
