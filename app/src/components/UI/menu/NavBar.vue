<template>
    <div
        class="navbar"
    >
        <div class="navbar__head">
            <a
                href="/"
                class="navbar__head__logo"
            >
                <site-logo :size="52"/>
            </a>
        </div>

        <menu-theme-switcher/>
    </div>
</template>

<script>
    import MenuThemeSwitcher from '@/components/UI/MenuThemeSwitcher.vue';

    export default {
        name: "NavBar",
        components: { MenuThemeSwitcher }
    }
</script>

<style lang="scss" scoped>

</style>
