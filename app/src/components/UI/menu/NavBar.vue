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
            </div>

            <div class="navbar__head__end">
                <menu-theme-switcher/>
            </div>
        </div>

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
                        <site-logo :size="84"/>
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
                <nav class="navigation">
                    <label class="navigation__head">
                        <span>
                            Навигация
                        </span>
                    </label>
                    <ul>
                        <li>
                            <a href="">Row 1</a>
                        </li>
                        <li>
                            <a href="">Row 2</a>
                        </li>
                        <li>
                            <a href="">Row 3</a>
                        </li>
                    </ul>
                </nav>
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
            menu: false
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
