<template>
    <nav-popover v-model="opened">
        <template #trigger="{ setRef, isActive }">
            <div
                :ref="el => setRef(el)"
                class="navbar__btn"
                :class="{ 'is-active': isActive }"
                @click.left.exact.prevent="opened = !opened"
            >
                <svg-icon
                    :icon-name="isBookmarksExist"
                    :stroke-enable="false"
                    fill-enable
                />
            </div>
        </template>

        <template #default>
            <div class="nav-bookmarks">
                <default-bookmarks v-if="!userStore.isAuthenticated"/>

                <custom-bookmarks v-else/>
            </div>
        </template>
    </nav-popover>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import NavPopover from "@/components/UI/menu/NavPopover";
    import DefaultBookmarks from "@/components/UI/menu/bookmarks/DefaultBookmarks";
    import CustomBookmarks from "@/components/UI/menu/bookmarks/CustomBookmarks";
    import { useUserStore } from "@/store/UI/UserStore";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";

    export default {
        name: "NavBookmarks",
        components: {
            CustomBookmarks,
            DefaultBookmarks,
            NavPopover,
            SvgIcon
        },
        data: () => ({
            opened: false,
            userStore: useUserStore(),
            defaultBookmarkStore: useDefaultBookmarkStore(),
            customBookmarkStore: useCustomBookmarkStore()
        }),
        computed: {
            isBookmarksExist() {
                let status = this.defaultBookmarkStore.getBookmarks.filter(item => item.url).length > 0;

                if (!status && this.userStore.isAuthenticated) {
                    status = this.customBookmarkStore.getBookmarks.filter(item => item.url).length > 0;
                }

                return status
                    ? 'bookmark-filled'
                    : 'bookmark';
            }
        },
        async beforeMount() {
            await this.defaultBookmarkStore.restoreBookmarks();

            if (await this.userStore.getUserStatus()) {
                await this.customBookmarkStore.queryGetBookmarks();
            }
        }
    };
</script>

<style lang="scss" scoped>
    .nav-bookmarks {
        display: flex;
        height: 100%;
        overflow: hidden;
    }
</style>
