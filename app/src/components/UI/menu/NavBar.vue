<template>
    <div class="navbar">
        <div class="navbar__head">
            <div class="navbar__head__start">
                <div
                    class="hamburger btn_nav"
                    :class="{ 'is-active': menu }"
                    @click.left.exact.prevent="toggleMenu"
                >
                    <span class="line"/>
                    <span class="line"/>
                    <span class="line"/>
                </div>

                <a
                    href="/"
                    class="link_nav"
                >
                    <span>DND5 Club</span>
                </a>

                <span v-if="section">
                    <span class="nav_text">
                        /
                    </span>

                    <span
                        class="nav_text"
                    >
                        <span>{{ section }}</span>
                    </span>
                </span>
            </div>

            <div class="navbar__head__end">
                <menu-theme-switcher/>

                <nav-profile/>
            </div>
        </div>

        <div
            class="close_blok"
            :class="{ 'is-active': menu }"
            @click.left.exact.prevent="toggleMenu"
        />

        <aside
            class="club__card"
            :class="{ 'is-active': menu }"
        >
            <header class="club__card__header">
                <div class="club__card__header__logo">
                    <a
                        href="/"
                        class="navbar__head__logo"
                    >
                        <site-logo :size="76"/>
                    </a>
                </div>

                <div class="club__card__header__info">
                    <div class="row">
                        Онлайн справчник по D&D 5e
                    </div>

                    <div class="row">
                        DND5 Club
                    </div>
                </div>
            </header>

            <section class="club__card__menu">
                <div
                    v-for="(group, groupKey) in getNavItems"
                    :key="group.label + groupKey"
                    class="navigation"
                >
                    <label class="navigation__head">
                        <span>
                            {{ group.label }}
                        </span>
                    </label>

                    <ul>
                        <li
                            v-for="link in group.links"
                            :key="link.url"
                        >
                            <a
                                :target="link.external ? '_blank' : '_self'"
                                :href="link.url"
                            >{{ link.label }}</a>
                        </li>
                    </ul>
                </div>
            </section>
        </aside>
    </div>
</template>

<script>
    import MenuThemeSwitcher from '@/components/UI/MenuThemeSwitcher.vue';
    import SvgIcon from "@/components/UI/SvgIcon";
    import SiteLogo from "@/components/UI/SiteLogo";
    import NavProfile from "@/components/UI/menu/NavProfile";
    import { mapActions, mapState } from "pinia";
    import { useNavStore } from "@/store/UI/NavStore";

    export default {
        name: "NavBar",
        components: {
            NavProfile, SiteLogo, MenuThemeSwitcher, SvgIcon
        },
        props: {
            section: {
                type: String,
                default: ''
            }
        },
        data: () => ({
            menu: false,
        }),
        computed: {
            ...mapState(useNavStore, ['getNavItems'])
        },
        created() {
            this.setNavItems();
        },
        methods: {
            ...mapActions(useNavStore, ['setNavItems']),

            toggleMenu() {
                this.menu = !this.menu
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
