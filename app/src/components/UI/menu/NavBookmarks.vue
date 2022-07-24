<template>
    <nav-popover v-model="opened">
        <template #trigger="{ setRef }">
            <div
                :ref="el => setRef(el)"
                class="navbar__btn"
                @click.left.exact.prevent="opened = !opened"
            >
                <svg-icon icon-name="bookmark"/>
            </div>
        </template>

        <template #default>
            <div class="navbar__menu">
                <div
                    v-for="(group, groupKey) in getItems"
                    :key="group.label + groupKey"
                    class="navbar__menu_group"
                >
                    <div class="navbar__menu_group_label">
                        <div
                            v-if="isEdit"
                            class="navbar__menu_group_icon is-left"
                        >
                            <svg-icon icon-name="close"/>
                        </div>

                        <div class="navbar__menu_group_label">
                            {{ group.label }}
                        </div>

                        <div
                            v-if="isEdit"
                            class="navbar__menu_group_icon is-right"
                        >
                            <svg-icon icon-name="sandwich"/>
                        </div>
                    </div>

                    <div class="navbar__menu_links">
                        <div
                            v-for="link in group.links"
                            :key="link.url"
                            class="navbar__menu_link"
                        >
                            <div
                                v-if="isEdit"
                                class="navbar__menu_link_icon is-left"
                            >
                                <svg-icon icon-name="close"/>
                            </div>

                            <a
                                :href="link.url"
                                :target="isExternal(link.url) ? '_blank' : '_self'"
                                class="navbar__menu_link_label"
                            >{{ link.label }}</a>

                            <div
                                v-if="isEdit"
                                class="navbar__menu_link_icon is-right"
                            >
                                <svg-icon icon-name="sandwich"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </template>
    </nav-popover>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import NavPopover from "@/components/UI/menu/NavPopover";
    import { mapActions, mapState } from "pinia";
    import { useBookmarkStore } from "@/store/UI/BookmarkStore";

    export default {
        name: "NavBookmarks",
        components: {
            NavPopover,
            SvgIcon
        },
        data: () => ({
            opened: false,
            isEdit: false
        }),
        computed: {
            ...mapState(useBookmarkStore, ['getItems'])
        },
        mounted() {
            this.setItems();
        },
        methods: {
            ...mapActions(useBookmarkStore, ['setItems']),

            isExternal(url) {
                return url.startsWith('http');
            }
        }
    };
</script>

<style lang="scss" scoped>

</style>
