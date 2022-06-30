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
                    <span>DnD5 Club</span>
                </a>
                <span>
                    <span class="nav_text">
                        /
                    </span>
                    <span
                        class="nav_text"
                    >
                        <span>Name</span>
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
                        DnD5 Club
                    </div>
                </div>
            </header>

            <section class="club__card__menu">
                <div
                    v-for="(group, groupKey) in groups"
                    :key="groupKey"
                    class="navigation"
                >
                    <label class="navigation__head">
                        <span>
                            {{ group.label }}
                        </span>
                    </label>

                    <ul>
                        <li
                            v-for="(link, linkKey) in group.links"
                            :key="linkKey"
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

    export default {
        name: "NavBar",
        components: {
            NavProfile, SiteLogo, MenuThemeSwitcher, SvgIcon
        },
        data: () => ({
            menu: false,
            groups: [
                {
                    label: "Персонаж",
                    links: [
                        {
                            label: "Классы",
                            url: "/classes",
                            icon: "menu-classes"
                        },
                        {
                            label: "Расы",
                            url: "/races",
                            icon: "menu-races"
                        },
                        {
                            label: "Черты",
                            url: "/traits",
                            icon: "menu-traits"
                        },
                        {
                            label: "Особенности классов",
                            url: "/options",
                            icon: "menu-traits"
                        },
                        {
                            label: "Предыстории",
                            url: "/backgrounds",
                            icon: "menu-traits"
                        },
                        {
                            label: "Заклинания",
                            url: "/spells",
                            icon: "menu-traits"
                        }
                    ]
                },
                {
                    label: "Предметы",
                    links: [
                        {
                            label: "Оружие",
                            url: "/weapons",
                            icon: "menu-classes"
                        },
                        {
                            label: "Доспехи",
                            url: "/armors",
                            icon: "menu-races"
                        },
                        {
                            label: "Снаряжение",
                            url: "/items",
                            icon: "menu-traits"
                        },
                        {
                            label: "Драгоценности",
                            url: "/treasures",
                            icon: "menu-traits"
                        },
                        {
                            label: "Магические предметы",
                            url: "/items/magic",
                            icon: "menu-traits"
                        }
                    ]
                },
                {
                    label: "Мастерская",
                    links: [
                        {
                            label: "Бестиарий",
                            url: "/bestiary",
                            icon: "menu-classes"
                        },
                        {
                            label: "Ширма",
                            url: "/screens",
                            icon: "menu-races"
                        }
                    ]
                },
                {
                    label: "Инструменты",
                    links: [
                        {
                            label: "Торговец",
                            url: "/tools/trader",
                            icon: "menu-classes"
                        },
                        {
                            label: "Случайные столкновения",
                            url: "/tools/encounters",
                            icon: "menu-races"
                        },
                        {
                            label: "Сокровищница",
                            url: "/tools/treasury",
                            icon: "menu-races"
                        },
                        {
                            label: "Дикая магия",
                            url: "/tools/wildmagic",
                            icon: "menu-races"
                        },
                        {
                            label: "Безумие",
                            url: "/tools/madness",
                            icon: "menu-races"
                        }
                    ]
                },
                {
                    label: "База знаний",
                    links: [
                        {
                            label: "Боги",
                            url: "/gods",
                            icon: "menu-classes"
                        },
                        {
                            label: "Правила и термины",
                            url: "/rules",
                            icon: "menu-races"
                        },
                        {
                            label: "Источники",
                            url: "/books",
                            icon: "menu-races"
                        }
                    ]
                },
                {
                    label: "Информация",
                    links: [
                        {
                            label: "Мы в Discord",
                            url: "https://discord.gg/zqBnMJVf3z",
                            icon: "menu-classes",
                            external: true,
                        },
                        {
                            label: "Мы на ВКонтакте",
                            url: "https://vk.com/dnd5club",
                            icon: "menu-races",
                            external: true,
                        },
                        {
                            label: "Мы на Boosty",
                            url: "https://boosty.to/dnd5club",
                            icon: "menu-races",
                            external: true,
                        },
                        {
                            label: "Наш бот для Telegram",
                            url: "https://t.me/dnd5club_bot",
                            icon: "menu-races",
                            external: true,
                        },
                        {
                            label: "Мастер на Boosty",
                            url: "https://boosty.to/dnd5eclub",
                            icon: "menu-races",
                            external: true,
                        },
                        {
                            label: "Старый сайт",
                            url: "https://old.dnd5.club/",
                            icon: "menu-races",
                            external: true,
                        }
                    ]
                },
            ]
        }),
        methods: {
            toggleMenu() {
                this.menu = !this.menu
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
