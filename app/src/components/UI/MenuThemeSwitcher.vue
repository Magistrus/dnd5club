<template>
    <div
        class="theme_mode btn_nav"
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
        <!-- <span>{{ currentLabel }}</span> -->
    </div>
</template>

<script>
    import { mapActions, mapState } from 'pinia/dist/pinia';
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useUIStore } from '@/store/UI/UIStore';

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

<style lang="scss" scoped>
.theme_mode {
    display: flex;
    align-items: center;

    i {
        // margin-right: 4px;

        svg {
            width: 30px;
            height: 30px;
        }
    }
}
</style>
