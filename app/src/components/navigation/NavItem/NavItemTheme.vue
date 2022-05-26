<template>
    <div
        class="nav-item is-bottom"
        :class="{ 'is-minified': menuConfig.minified }"
    >
        <div
            class="nav-item__trigger"
            @click.left.exact.prevent="switchTheme"
        >
            <div class="nav-item__icon">
                <svg-icon
                    :icon-name="`${currentIcon}-theme`"
                    :fill-enable="true"
                    :stroke-enable="true"
                />
            </div>

            <div class="nav-item__name">
                {{ currentLabel }}
            </div>
        </div>

        <div
            ref="submenu"
            class="nav-item__submenu"
        >
            <div class="nav-item__sub-title">
                {{ currentLabel }}
            </div>
        </div>
    </div>
</template>

<script>
    import { mapActions, mapState } from 'pinia/dist/pinia';
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'NavItemTheme',
        components: { SvgIcon },
        computed: {
            ...mapState(useUIStore, {
                theme: 'getTheme',
                menuConfig: 'getMenuConfig',
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
    @import "NavItem.module.scss";
</style>
