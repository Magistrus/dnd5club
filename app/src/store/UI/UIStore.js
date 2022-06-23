import { defineStore } from 'pinia';
import localforage from 'localforage';
import { DB_NAME, THEME_DB_KEY } from '@/common/const/UI';
import HTTPService from '@/common/services/HTTPService';
import errorHandler from '@/common/helpers/errorHandler';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useUIStore = defineStore('UIStore', {
    state: () => ({
        theme: '',
        content: {
            fullscreen: false,
        },
        store: localforage.createInstance({
            name: DB_NAME,
            storeName: 'UI'
        })
    }),

    getters: {
        getTheme: state => state.theme,
        getMenuConfig: state => state.menu,
        getContentConfig: state => state.content,
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

                this.store.setItem(THEME_DB_KEY, themeName);

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

        setFullscreenState(state) {
            this.content.fullscreen = state;
        }
    }
});
