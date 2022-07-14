<template>
    <div class="navbar">
        <header class="navbar__header">
            <div class="navbar__header_left">
                <div
                    :class="{ 'is-active': getMenuState }"
                    class="navbar__btn hamburger"
                    @click.left.exact.prevent="toggleMenu"
                >
                    <span class="line"/>
                    <span class="line"/>
                    <span class="line"/>
                </div>

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
                <nav-profile v-if="$isDev"/>

                <menu-theme-switcher/>
            </div>
        </header>

        <div
            v-if="popover"
            class="nav-popover"
            @click.left.exact.self.prevent="closeMenu"
        >
            <transition
                name="menu-animation"
                mode="out-in"
                @after-leave="onCloseMenu"
            >
                <div
                    v-if="getMenuState"
                    class="nav-popover__body"
                >
                    <div class="nav-popover__header">
                        <a
                            class="nav-popover__logo"
                            href="/"
                        >
                            <site-logo/>
                        </a>

                        <div class="nav-popover__info">
                            <span>Онлайн справчник по D&D 5e</span>

                            <span>DnD5 Club</span>
                        </div>
                    </div>

                    <div class="nav-popover__menu">
                        <div
                            v-for="(group, groupKey) in getNavItems"
                            :key="group.label + groupKey"
                            class="nav-popover__menu_group"
                        >
                            <div class="nav-popover__menu_group_label">
                                <div class="nav-popover__menu_group_label">
                                    {{ group.label }}
                                </div>

                                <div
                                    v-if="group.icon"
                                    class="nav-popover__menu_group_icon"
                                >
                                    <svg-icon :icon-name="group.icon"/>
                                </div>
                            </div>

                            <div class="nav-popover__menu_links">
                                <div
                                    v-for="link in group.links"
                                    :key="link.url"
                                    class="nav-popover__menu_link"
                                >
                                    <div class="nav-popover__menu_link_bookmark">
                                        <svg-icon icon-name="bookmark"/>
                                    </div>

                                    <a
                                        :href="link.url"
                                        :target="link.external ? '_blank' : '_self'"
                                        class="nav-popover__menu_link_label"
                                    >{{ link.label }}</a>

                                    <div
                                        v-if="link.icon"
                                        class="nav-popover__menu_link_icon"
                                    >
                                        <svg-icon :icon-name="link.icon"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </transition>
        </div>
    </div>
</template>

<script>
    import MenuThemeSwitcher from '@/components/UI/MenuThemeSwitcher.vue';
    import SiteLogo from "@/components/UI/SiteLogo";
    import {
        mapActions, mapState
    } from "pinia";
    import { useNavStore } from "@/store/UI/NavStore";
    import NavProfile from "@/components/UI/menu/NavProfile";

    export default {
        name: "NavBar",
        components: {
            NavProfile,
            SiteLogo,
            MenuThemeSwitcher
        },
        props: {
            section: {
                type: String,
                default: ''
            }
        },
        data: () => ({
            popover: false
        }),
        computed: {
            ...mapState(useNavStore, ['getNavItems', 'getMenuState'])
        },
        created() {
            this.setNavItems();
        },
        methods: {
            ...mapActions(useNavStore, ['setNavItems', 'setMenuState']),

            openMenu() {
                this.popover = true;

                this.$nextTick(() => {
                    this.setMenuState(true);
                });
            },

            closeMenu() {
                this.setMenuState(false);
            },

            onCloseMenu() {
                this.popover = false;
            },

            toggleMenu() {
                if (this.getMenuState) {
                    this.closeMenu();

                    return;
                }

                this.openMenu();
            }
        }
    };
</script>
