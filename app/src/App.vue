<script>
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'App',
        data: () => ({
            uiStore: useUIStore()
        }),
        computed: {
            currentTheme() {
                return this.uiStore.getTheme
            },
        },
        async beforeMount() {
            await this.uiStore.setTheme();

            document.documentElement.style.setProperty('--max-vh', `${ window.innerHeight }px`);

            window.addEventListener('resize', () => {
                document.documentElement.style.setProperty('--max-vh', `${ window.innerHeight }px`);
            });
        },
    };
</script>

<style lang="scss" scoped>
    //.dnd5club {
    //    &__background {
    //        display: none;
    //
    //        @include media-min($xl) {
    //            display: block;
    //            position: fixed;
    //            bottom: 0;
    //            right: 0;
    //            width: 100%;
    //            height: 100%;
    //            opacity: 0.65;
    //            z-index: -1;
    //            pointer-events: none;
    //            user-select: none;
    //            object-fit: contain;
    //            object-position: bottom right;
    //        }
    //    }
    //
    //    &__body {
    //        width: 100%;
    //        height: calc(100% - 73px);
    //        overflow: auto;
    //
    //        @include media-min($md) {
    //            height: var(--max-vh);
    //        }
    //
    //        .content {
    //            width: 100%;
    //            position: relative;
    //            padding: 0 calc((100% - 1320px) / 2);
    //        }
    //    }
    //}
</style>
