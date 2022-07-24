<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { mapActions } from "pinia/dist/pinia";
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
            await this.updateUserFromSession();
        },
        methods: {
            ...mapActions(useUserStore, ['updateUserFromSession']),

            async initTheme() {
                await this.uiStore.setTheme();
            },

            async initFullscreen() {
                await this.uiStore.setFullscreenState();
            }
        }
    };
</script>
