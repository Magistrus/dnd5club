<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { useUserStore } from "@/store/UI/UserStore";
    import errorHandler from "@/common/helpers/errorHandler";

    export default {
        data: () => ({
            uiStore: useUIStore(),
            userStore: useUserStore()
        }),
        async beforeMount() {
            try {
                // User
                await this.userStore.getUserInfo();
            } catch (err) {
                errorHandler(err);
            }
        },
        async mounted() {
            // UI
            this.uiStore.watchMaxHeight();
            this.uiStore.watchIsMobile();

            await this.uiStore.setTheme();
            await this.uiStore.setFullscreenState(false);
        }
    };
</script>
