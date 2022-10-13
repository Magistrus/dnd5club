<template>
    <div
        class="navbar__btn"
        @click.left.exact.prevent="switchTheme"
    >
        <svg-icon
            :icon-name="`${icon}-theme`"
            :stroke-enable="false"
            fill-enable
        />
    </div>
</template>

<script>
    import SvgIcon from '@/components/UI/icons/SvgIcon';
    import { useUIStore } from '@/store/UI/UIStore';
    import { computed } from "vue";

    export default {
        name: 'MenuThemeSwitcher',
        components: { SvgIcon },
        setup() {
            const uiStore = useUIStore();
            const icon = computed(() => (uiStore.theme === 'dark'
                ? 'light'
                : 'dark'));
            const switchTheme = async () => {
                await uiStore.setTheme(icon.value);
            };

            return {
                icon,
                switchTheme
            };
        }
    };
</script>
