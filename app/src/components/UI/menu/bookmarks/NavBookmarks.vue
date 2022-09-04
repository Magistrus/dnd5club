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
    import {
        computed,
        onBeforeMount,
        ref
    } from "vue";

    export default {
        name: "NavBookmarks",
        components: {
            CustomBookmarks,
            DefaultBookmarks,
            NavPopover,
            SvgIcon
        },
        setup() {
            const opened = ref(false);
            const userStore = useUserStore();
            const defaultBookmarkStore = useDefaultBookmarkStore();
            const customBookmarkStore = useCustomBookmarkStore();
            const isBookmarksExist = computed(() => {
                let status = false;

                if (!userStore.isAuthenticated) {
                    status = defaultBookmarkStore.getBookmarks.filter(item => item.url).length > 0;
                }

                if (userStore.isAuthenticated) {
                    status = customBookmarkStore.getBookmarks.filter(item => item.url).length > 0;
                }

                return status
                    ? 'bookmark-filled'
                    : 'bookmark';
            });

            userStore.$onAction(({ name, after }) => {
                switch (name) {
                    case 'authorization':
                        after(async () => {
                            await customBookmarkStore.queryGetBookmarks();
                        });

                        break;

                    case 'logout':
                        after(() => {
                            customBookmarkStore.clearBookmarks();
                        });

                        break;
                    default:
                        break;
                }
            });

            onBeforeMount(async () => {
                await defaultBookmarkStore.restoreBookmarks();

                if (await userStore.getUserStatus()) {
                    await customBookmarkStore.queryGetBookmarks();
                }
            });

            return {
                opened,
                isBookmarksExist,
                userStore
            };
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
