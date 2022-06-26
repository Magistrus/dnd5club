<template>
    <div
        class="navbar"
    >
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
                    class="btn_nav"
                >
                    <span>DND5 Club</span>
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
                <div class="btn_nav">
                    <svg
                        width="30"
                        height="30"
                        viewBox="0 0 30 30"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                            d="M24 25C24.5523 25 25 24.5523 25 24V21.25C25 19.9239 24.4732 18.6521 23.5355 17.7145C22.5979 16.7768 21.3261 16.25 20 16.25H10C8.67392 16.25 7.40215 16.7768 6.46447 17.7145C5.52678 18.6521 5 19.9239 5 21.25V24C5 24.5523 5.44772 25 6 25H24Z"
                            fill="#4D4DAA"
                        />
                        <path
                            d="M20 10C20 12.7614 17.7614 15 15 15C12.2386 15 10 12.7614 10 10C10 7.23858 12.2386 5 15 5C17.7614 5 20 7.23858 20 10Z"
                            fill="#4D4DAA"
                        />
                    </svg>
                </div>
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

    export default {
        name: "NavBar",
        components: { MenuThemeSwitcher },
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
                    label: "DM",
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
