<script>
    import { useUIStore } from '@/store/UI/UIStore';

    export default {
        data: () => ({
            uiStore: useUIStore()
        }),
        async mounted() {
            document.documentElement.style.setProperty('--max-vh', `${ window.innerHeight }px`);

            window.addEventListener('resize', () => {
                document.documentElement.style.setProperty('--max-vh', `${ window.innerHeight }px`);
            });

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
