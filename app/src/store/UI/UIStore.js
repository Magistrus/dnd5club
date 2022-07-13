import { defineStore } from 'pinia';
import localforage from 'localforage';
import {
    DB_NAME, FULLSCREEN_DB_KEY, THEME_DB_KEY
} from '@/common/const/UI';
import errorHandler from '@/common/helpers/errorHandler';

// eslint-disable-next-line import/prefer-default-export
export const useUIStore = defineStore('UIStore', {
    state: () => ({
        theme: '',
        isMobile: false,
        fullscreen: false,
        maxHeight: 0,
        store: localforage.createInstance({
            name: DB_NAME,
            storeName: 'UI'
        })
    }),

    getters: {
        getTheme: state => state.theme,
        getIsMobile: state => state.isMobile,
        getMaxHeight: state => state.maxHeight,
        getFullscreen: state => state.fullscreen
    },

    actions: {
        async setTheme(payload = '') {
            try {
                await this.store.ready();

                const storageTheme = await this.store.getItem(THEME_DB_KEY)
                    || localStorage.getItem('theme')
                    || 'dark';
                const themeName = payload || storageTheme;

                this.theme = themeName;

                await this.store.setItem(THEME_DB_KEY, themeName);

                if (localStorage.getItem(THEME_DB_KEY)) {
                    localStorage.removeItem(THEME_DB_KEY);
                }

                const html = document.querySelector('html');

                if (!html) {
                    return;
                }

                html.dataset.theme = `theme-${ themeName }`;
            } catch (err) {
                errorHandler(err);
            }
        },

        async setFullscreenState(payload) {
            try {
                await this.store.ready();

                let fullscreen = payload || false;

                const storageState = await this.store.getItem(FULLSCREEN_DB_KEY);

                if (typeof payload !== 'boolean' && typeof storageState === 'boolean') {
                    fullscreen = storageState;
                }

                this.fullscreen = fullscreen;

                await this.store.setItem(FULLSCREEN_DB_KEY, fullscreen);
            } catch (err) {
                errorHandler(err);
            }
        },

        watchMaxHeight() {
            const updateCallback = () => {
                document.documentElement.style.setProperty('--max-vh', `${ window.innerHeight }px`);

                this.maxHeight = window.innerHeight;
            };

            updateCallback();

            window.addEventListener('resize', updateCallback);
        },

        watchIsMobile() {
            const updateCallback = () => {
                const isMobile = window.innerWidth < 1200;

                if (this.isMobile !== isMobile) {
                    this.isMobile = isMobile;
                }
            };

            updateCallback();

            window.addEventListener('resize', updateCallback);
        }
    }
});
