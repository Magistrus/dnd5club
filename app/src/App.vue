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
            this.uiStore.watchWindowSize();

            await this.uiStore.setFullscreenState(false);

            const html = document.querySelector('html');

            if (!html?.dataset?.theme || !(/theme-[dark|light]/).test(html.dataset.theme)) {
                this.uiStore.setTheme();
            }

            await this.uiStore.removeOldTheme();
        }
    };
</script>
