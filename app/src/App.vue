<script>
    import { useUIStore } from '@/store/UI/UIStore';

    export default {
        data: () => ({
            uiStore: useUIStore()
        }),
        async mounted() {
            this.uiStore.watchMaxHeight();
            this.uiStore.watchIsMobile();

            await this.initTheme();
            await this.initFullscreen();
        },
        methods: {
            async initTheme() {
                await this.uiStore.setTheme();
            },

            async initFullscreen() {
                await this.uiStore.setFullscreenState();
            }
        }
    };
</script>
