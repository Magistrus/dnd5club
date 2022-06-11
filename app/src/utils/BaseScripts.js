import { useUIStore } from '@/store/UI/UIStore';

// eslint-disable-next-line func-names
export default function initialScript() {
    async function initTheme() {
        const uiStore = useUIStore();

        await uiStore.setTheme();
    }

    initTheme()
        .then();

    document.documentElement.style.setProperty('--max-vh', `${ window.innerHeight }px`);

    window.addEventListener('resize', () => {
        document.documentElement.style.setProperty('--max-vh', `${ window.innerHeight }px`);
    });
}
