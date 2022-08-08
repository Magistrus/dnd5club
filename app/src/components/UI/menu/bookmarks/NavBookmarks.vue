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
                    :icon-name="getDefaultBookmarks?.length ? 'bookmark-filled' : 'bookmark'"
                    :stroke-enable="false"
                    fill-enable
                />
            </div>
        </template>

        <template #default>
            <div class="nav-bookmarks">
                <default-bookmarks/>

                <custom-bookmarks v-if="isAuthorized"/>
            </div>
        </template>
    </nav-popover>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import NavPopover from "@/components/UI/menu/NavPopover";
    import { mapState, mapActions } from "pinia";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import DefaultBookmarks from "@/components/UI/menu/bookmarks/DefaultBookmarks";
    import CustomBookmarks from "@/components/UI/menu/bookmarks/CustomBookmarks";
    import { useUserStore } from "@/store/UI/UserStore";

    export default {
        name: "NavBookmarks",
        components: {
            CustomBookmarks,
            DefaultBookmarks,
            NavPopover,
            SvgIcon
        },
        data: () => ({
            opened: false
        }),
        computed: {
            ...mapState(useUserStore, ['isAuthorized']),
            ...mapState(useDefaultBookmarkStore, {
                getDefaultBookmarks: 'getBookmarks'
            })
        },
        async beforeMount() {
            await this.updateUserFromSession();
            await this.restoreDefaultBookmarks();
        },
        methods: {
            ...mapActions(useUserStore, ['updateUserFromSession']),
            ...mapActions(useDefaultBookmarkStore, {
                restoreDefaultBookmarks: 'restoreBookmarks'
            })
        }
    };
</script>

<style lang="scss" scoped>
    .nav-bookmarks {
        display: flex;
    }
</style>
