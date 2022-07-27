<template>
    <div
        class="navbar__btn"
        @click.left.exact.prevent="switchTheme"
    >
        <svg-icon
            :icon-name="`${currentIcon}-theme`"
            :stroke-enable="false"
            fill-enable
        />
    </div>
</template>

<script>
    import {
        mapActions, mapState
    } from 'pinia';
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useUIStore } from '@/store/UI/UIStore';

    export default {
        name: 'MenuThemeSwitcher',
        components: { SvgIcon },
        computed: {
            ...mapState(useUIStore, {
                theme: 'getTheme'
            }),

            currentIcon() {
                return this.theme === 'dark'
                    ? 'light'
                    : 'dark';
            },

            currentLabel() {
                return this.theme === 'dark'
                    ? 'Светлая тема'
                    : 'Темная тема';
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
    };
</script>
