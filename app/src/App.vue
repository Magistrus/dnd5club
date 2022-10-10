<script>
    import { useUIStore } from '@/store/UI/UIStore';
    import { useUserStore } from "@/store/UI/UserStore";

    export default {
        data: () => ({
            uiStore: useUIStore(),
            userStore: useUserStore()
        }),
        async beforeMount() {
            try {
                // User
                if (await this.userStore.getUserStatus()) {
                    await this.userStore.getUserInfo();
                }
            } catch (err) {
                this.userStore.clearUser();
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
