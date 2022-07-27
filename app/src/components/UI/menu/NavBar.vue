<template>
    <div class="navbar">
        <header class="navbar__header">
            <div class="navbar__header_left">
                <nav-menu/>

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
    import NavProfile from "@/components/UI/menu/NavProfile";
    import NavBookmarks from "@/components/UI/menu/NavBookmarks";
    import NavMenu from "@/components/UI/menu/NavMenu";
    import { mapActions } from "pinia";
    import { useBookmarkStore } from "@/store/UI/BookmarkStore";

    export default {
        name: "NavBar",
        components: {
            NavMenu,
            NavBookmarks,
            NavProfile,
            MenuThemeSwitcher
        },
        props: {
            section: {
                type: String,
                default: ''
            }
        },
        created() {
            this.setSection(this.section);
        },
        methods: {
            ...mapActions(useBookmarkStore, ['setSection'])
        }
    };
</script>
