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
        fullscreen: false,
        windowSize: {
            height: 0,
            width: 0
        },
        store: localforage.createInstance({
            name: DB_NAME,
            storeName: 'UI'
        })
    }),

    getters: {
        isMobile: state => state.windowSize.width < 1200
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

        watchWindowSize() {
            const updateCallback = () => {
                const size = {
                    width: window.innerWidth,
                    height: window.innerHeight
                };

                document.documentElement.style.setProperty('--max-vh', `${ size.height }px`);

                this.windowSize = size;
            };

            updateCallback();

            window.addEventListener('resize', updateCallback);
        }
    }
});
