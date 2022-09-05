<script>
    import { useUIStore } from '@/store/UI/UIStore';

    export default {
        data: () => ({
            uiStore: useUIStore()
        }),
        async mounted() {
            this.uiStore.watchMaxHeight();
            this.uiStore.watchIsMobile();

            await this.uiStore.setTheme();
            await this.uiStore.setFullscreenState(false);
        }
    };
</script>
