<template>
    <div
        class="navbar"
    >
        <div class="navbar__head">
            <div class="navbar__head__start">
                <div class="hamburger btn_nav" :class="{ 'is-active': menu }" @click.left.exact.prevent="toggleMenu">
                    <span class="line"/>
                    <span class="line"/>
                    <span class="line"/>
                </div>
                <a
                    href="/"
                    class="navbar__head__logo"
                >
                    <site-logo :size="52"/>
                </a>
            </div>

            <div class="navbar__head__end">
                <menu-theme-switcher/>
            </div>
        </div>

		<aside class="club__card" :class="{ 'is-active': menu }">
			<header class="club__card__header">
				<div class="club__card__header_logo">
					<site-logo :size="62"/>
				</div>
				<div class="club__card__header_info">
					<div>
						row 1
					</div>
					<div>
						DND 5 Club
					</div>
				</div>
			</header>
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
			toggleMenu () {
				this.menu=!this.menu
			}
		}
    }
</script>

<style lang="scss" scoped>

</style>
