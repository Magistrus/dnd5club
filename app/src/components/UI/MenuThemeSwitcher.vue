<template>
    <li
        class="mt-auto"
    >
        <a
            href="#"
            @click.left.exact.prevent="switchTheme"
        >
            <i
                id="icon_dark_mod"
                class="icon_menu"
            >
                <svg-icon
                    :fill-enable="true"
                    :icon-name="`${currentIcon}-theme`"
                    :stroke-enable="true"
                />
            </i>
            <span
                id="theme_mode"
                class="link_name"
            >{{ currentLabel }}</span>
        </a>

        <ul class="sub_menu blank">
            <li class="link_name">
                <span id="theme_mode_link">{{ currentLabel }}</span>
            </li>
        </ul>
    </li>
</template>

<script>
    import { mapActions, mapState } from 'pinia/dist/pinia';
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'MenuThemeSwitcher',
        components: { SvgIcon },
        computed: {
            ...mapState(useUIStore, {
                theme: 'getTheme',
            }),

            currentIcon() {
                return this.theme === 'dark'
                    ? 'light'
                    : 'dark'
            },

            currentLabel() {
                return this.theme === 'dark'
                    ? 'Светлая тема'
                    : 'Темная тема'
            }
        },
        methods: {
            ...mapActions(useUIStore, {
                setTheme: 'setTheme'
            }),

            async switchTheme() {
                await this.setTheme(this.currentIcon);
            }
        }
    }
</script>
