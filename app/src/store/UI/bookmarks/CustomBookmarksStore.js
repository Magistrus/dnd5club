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
        getBookmarks: state => state.bookmarks,
        isBookmarkSaved(state) {
            return url => {
                const bookmarks = cloneDeep(state.bookmarks);
                const index = bookmarks.findIndex(bookmark => bookmark.url === url);

                return index >= 0;
            };
        },
        getBookmarkParentUUIDs(state) {
            return url => {
                if (!this.isBookmarkSaved(url)) {
                    return undefined;
                }

                const bookmarks = cloneDeep(state.bookmarks);
                const index = bookmarks.findIndex(bookmark => bookmark.url === url);
                const categoryIndex = bookmarks.findIndex(category => category.uuid === bookmarks[index].parentUUID);

                if (categoryIndex < 0) {
                    return undefined;
                }

                return bookmarks.find(group => group.uuid === bookmarks[categoryIndex].parentUUID);
            };
        }
    },

    actions: {
        async queryGetBookmarks() {
            try {
                await this.userStore.updateUserFromSession();

                if (!this.userStore.isAuthorized) {
                    return Promise.reject();
                }

                const resp = await this.$http.get('/bookmarks');

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

                const resp = await this.$http.put('/bookmarks', cloneDeep(this.bookmarks));

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

                const resp = await this.$http.post('/bookmarks', bookmark);

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

                const resp = await this.$http.delete(`/bookmarks/${ uuid }`);

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
