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
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import DefaultBookmarks from "@/components/UI/menu/bookmarks/DefaultBookmarks";
    import CustomBookmarks from "@/components/UI/menu/bookmarks/CustomBookmarks";
    import { useUserStore } from "@/store/UI/UserStore";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import cloneDeep from "lodash/cloneDeep";

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
                let status = this.defaultBookmarkStore.getBookmarks?.length;

                if (!status && this.userStore.isAuthenticated) {
                    status = this.customBookmarkStore.getBookmarks?.length;
                }

                return status
                    ? 'bookmark-filled'
                    : 'bookmark';
            }
        },
        async beforeMount() {
            await this.defaultBookmarkStore.restoreBookmarks();
            await this.userStore.getUserStatus();

            const unsubscribeLoginListener = this.userStore.$onAction(({ name, after }) => {
                if (name === 'authorization') {
                    after(async () => {
                        await this.customBookmarkStore.queryMergeDefaultBookmark();

                        unsubscribeLoginListener();
                    });
                }
            });

            if (this.userStore.isAuthenticated) {
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
