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
        },
        methods: {
            async initTheme() {
                const uiStore = useUIStore();

                await uiStore.setTheme();
            }
        }
    }
</script>
