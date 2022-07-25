<template>
    <div class="navbar">
        <header class="navbar__header">
            <div class="navbar__header_left">
                <nav-popover
                    :model-value="menu"
                    is-menu
                    is-left
                    @close="menu = false"
                >
                    <template #trigger="{ setRef }">
                        <div
                            :ref="el => setRef(el)"
                            :class="{ 'is-active': menu }"
                            class="navbar__btn hamburger"
                            @click.left.exact.prevent="menu = !menu"
                        >
                            <span class="line"/>
                            <span class="line"/>
                            <span class="line"/>
                        </div>
                    </template>

                    <template #default>
                        <div class="navbar__menu_header">
                            <a
                                class="navbar__menu_logo"
                                href="/"
                            >
                                <site-logo/>
                            </a>

                            <div class="navbar__menu_info">
                                <span class="navbar__menu_info--desc">Онлайн справочник по D&D 5e</span>

                                <span class="navbar__menu_info--title">DnD5 Club</span>
                            </div>
                        </div>

                        <div class="navbar__menu">
                            <div
                                v-for="(group, groupKey) in getNavItems"
                                :key="group.label + groupKey"
                                class="navbar__menu_group"
                            >
                                <div class="navbar__menu_group_label">
                                    <div
                                        v-if="group.icon"
                                        class="navbar__menu_group_icon"
                                    >
                                        <svg-icon :icon-name="group.icon"/>
                                    </div>

                                    <div class="navbar__menu_group_label">
                                        {{ group.label }}
                                    </div>
                                </div>

                                <div class="navbar__menu_links">
                                    <div
                                        v-for="link in group.links"
                                        :key="link.url"
                                        class="navbar__menu_link"
                                    >
                                        <div
                                            class="navbar__menu_link_icon only-hover"
                                            :class="{'is-active': isBookmarkSaved(link.url)}"
                                            @click.left.exact.stop.prevent="updateBookmark(
                                                link.url,
                                                link.label,
                                                'Разделы'
                                            )"
                                        >
                                            <svg-icon
                                                :icon-name="isBookmarkSaved(link.url)
                                                    ? 'bookmark-dot-filled'
                                                    : 'bookmark-dot'
                                                "
                                                :stroke-enable="false"
                                                fill-enable
                                            />
                                        </div>

                                        <a
                                            :href="link.url"
                                            :target="link.external ? '_blank' : '_self'"
                                            class="navbar__menu_link_label"
                                        >{{ link.label }}</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                </nav-popover>

                <div class="navbar__section">
                    <a
                        class="navbar__link"
                        href="/"
                    >DnD5 Club</a>

                    <div
                        v-if="section"
                        class="navbar__text"
                    >
                        <span>/</span>

                        <span>{{ section }}</span>
                    </div>
                </div>
            </div>

            <div class="navbar__header_right">
                <nav-bookmarks/>

                <nav-profile v-if="$isDev"/>

                <menu-theme-switcher/>
            </div>
        </header>
    </div>
</template>

<script>
    import MenuThemeSwitcher from '@/components/UI/MenuThemeSwitcher.vue';
    import SiteLogo from "@/components/UI/SiteLogo";
    import { mapActions, mapState } from "pinia";
    import { useNavStore } from "@/store/UI/NavStore";
    import NavProfile from "@/components/UI/menu/NavProfile";
    import NavBookmarks from "@/components/UI/menu/NavBookmarks";
    import NavPopover from "@/components/UI/menu/NavPopover";
    import { useBookmarkStore } from "@/store/UI/BookmarkStore";
    import SvgIcon from "@/components/UI/SvgIcon";

    export default {
        name: "NavBar",
        components: {
            NavPopover,
            NavBookmarks,
            NavProfile,
            SiteLogo,
            MenuThemeSwitcher,
            SvgIcon
        },
        props: {
            section: {
                type: String,
                default: ''
            }
        },
        data: () => ({
            menu: false
        }),
        computed: {
            ...mapState(useNavStore, ['getNavItems']),
            ...mapState(useBookmarkStore, ['isBookmarkSaved'])
        },
        created() {
            this.setNavItems();
            this.setSection(this.section);
        },
        methods: {
            ...mapActions(useNavStore, ['setNavItems']),
            ...mapActions(useBookmarkStore, ['setSection', 'updateBookmark'])
        }
    };
</script>
