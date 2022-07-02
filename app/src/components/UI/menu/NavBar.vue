<template>
    <div
        v-scroll-lock="getMenuState"
        class="navbar"
    >
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
                <menu-theme-switcher/>

                <nav-profile/>
            </div>
        </header>

        <div
            v-if="popover"
            class="navbar__popover"
            @click.left.exact.self.prevent="closeMenu"
        >
            <transition
                name="menu-animation"
                mode="out-in"
                @after-leave="onCloseMenu"
            >
                <div
                    v-if="getMenuState"
                    class="navbar__popover_body"
                >
                    <div class="navbar__popover_header">
                        <a
                            class="navbar__popover_logo"
                            href="/"
                        >
                            <site-logo :size="76"/>
                        </a>

                        <div class="navbar__popover_info">
                            <span>Онлайн справчник по D&D 5e</span>

                            <span>DnD5 Club</span>
                        </div>
                    </div>

                    <div class="navbar__menu">
                        <div
                            v-for="(group, groupKey) in getNavItems"
                            :key="group.label + groupKey"
                            class="navbar__menu_group"
                        >
                            <div class="navbar__menu_group_label">
                                {{ group.label }}
                            </div>

                            <div class="navbar__menu_group_links">
                                <a
                                    v-for="link in group.links"
                                    :key="link.url"
                                    :href="link.url"
                                    :target="link.external ? '_blank' : '_self'"
                                    class="navbar__menu_group_link"
                                >{{ link.label }}</a>
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
                })
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
    }
</script>
