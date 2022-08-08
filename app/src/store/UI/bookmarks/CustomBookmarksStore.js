import { defineStore } from 'pinia';
import { useUserStore } from '@/store/UI/UserStore';
import cloneDeep from 'lodash/cloneDeep';

const signals = {
    add: undefined,
    delete: undefined
};

// eslint-disable-next-line import/prefer-default-export
export const useCustomBookmarkStore = defineStore('CustomBookmarkStore', {
    state: () => ({
        bookmarks: [],
        userStore: useUserStore()
    }),

    getters: {
        getBookmarks: state => state.bookmarks
    },

    actions: {
        async queryGetBookmarks() {
            try {
                await this.userStore.updateUserFromSession();

                if (!this.userStore.isAuthorized) {
                    return Promise.reject();
                }

                const resp = this.$http.get('/bookmarks');

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                this.bookmarks = resp.data;

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async querySaveBookmarks() {
            try {
                await this.userStore.updateUserFromSession();

                if (!this.userStore.isAuthorized) {
                    return Promise.reject();
                }

                const resp = this.$http.put('/bookmarks/', cloneDeep(this.bookmarks));

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                await this.queryGetBookmarks();

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async queryAddBookmark(bookmark) {
            try {
                await this.userStore.updateUserFromSession();

                if (!this.userStore.isAuthorized) {
                    return Promise.reject();
                }

                if (signals.add) {
                    signals.add.abort();
                }

                const resp = this.$http.post('/bookmarks/', bookmark);

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                await this.queryGetBookmarks();

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            } finally {
                signals.add = undefined;
            }
        },

        async queryDeleteBookmark(uuid) {
            try {
                await this.userStore.updateUserFromSession();

                if (!this.userStore.isAuthorized) {
                    return Promise.reject();
                }

                if (signals.delete) {
                    signals.delete.abort();
                }

                const resp = this.$http.delete(`/bookmarks/${ uuid }`);

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                await this.queryGetBookmarks();

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            } finally {
                signals.delete = undefined;
            }
        }
    }
});
