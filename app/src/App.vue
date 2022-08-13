<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { mapActions } from "pinia";
    import { useUserStore } from "@/store/UI/UserStore";

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
        async beforeMount() {
            await this.getUserStatus();
        },
        methods: {
            ...mapActions(useUserStore, ['getUserStatus']),

            async initTheme() {
                await this.uiStore.setTheme();
            },

            async initFullscreen() {
                await this.uiStore.setFullscreenState(false);
            }
        }
    };
</script>
