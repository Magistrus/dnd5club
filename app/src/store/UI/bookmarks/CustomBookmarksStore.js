import { defineStore } from 'pinia';
import { useUserStore } from '@/store/UI/UserStore';
import cloneDeep from 'lodash/cloneDeep';
import { useDefaultBookmarkStore } from '@/store/UI/bookmarks/DefaultBookmarkStore';
import sortBy from 'lodash/sortBy';

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
        getGroups: state => {
            const groups = state.bookmarks.filter(group => !group.parentUUID);

            return sortBy(
                groups.map(group => ({
                    ...group,
                    children: sortBy(
                        state.bookmarks
                            .filter(category => category.parentUUID && category.parentUUID === group.uuid)
                            .map(category => ({
                                ...category,
                                children: sortBy(
                                    state.bookmarks.filter(bookmark => (
                                        bookmark.parentUUID
                                        && bookmark.parentUUID === category.uuid
                                    )),
                                    [o => o.order]
                                )
                            })),
                        [o => o.order]
                    )
                })),
                [o => o.order]
            );
        },
        getDefaultBookmarks: state => {
            const parent = state.bookmarks.find(item => item.order === -1);

            if (!parent) {
                return [];
            }

            const categories = [];
            const bookmarks = [];

            categories.push(...state.bookmarks.filter(item => item.parentUUID === parent.uuid));

            for (let i = 0; i < categories.length; i++) {
                const category = categories[i];

                bookmarks.push(...state.bookmarks.filter(item => item.parentUUID === category.uuid));
            }

            return [
                parent,
                ...categories,
                ...bookmarks
            ];
        },
        getCustomBookmarks: state => {
            const parents = state.bookmarks.filter(item => item.order !== -1 && !item.parentUUID);
            const categories = state.bookmarks
                .filter(item => parents.map(parent => parent.uuid).includes(item.parentUUID));
            const bookmarks = state.bookmarks
                .filter(item => categories.map(category => category.uuid).includes(item.parentUUID));

            return [
                ...parents,
                ...categories,
                ...bookmarks
            ];
        },
        isBookmarkSaved(state) {
            return url => state.bookmarks.findIndex(bookmark => bookmark.url === url) >= 0;
        },
        isDefaultBookmarkSaved() {
            return url => this.getDefaultBookmarks().findIndex(bookmark => bookmark.url === url) >= 0;
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
                if (!await this.userStore.getUserStatus()) {
                    return Promise.reject();
                }

                const resp = await this.$http.get('/bookmarks');

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                this.bookmarks = resp.data;

                return Promise.resolve(resp.data);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async querySaveBookmarks(payload) {
            try {
                if (!await this.userStore.getUserStatus()) {
                    return Promise.reject();
                }

                const resp = await this.$http.post('/bookmarks', payload);

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
                if (!await this.userStore.getUserStatus()) {
                    return Promise.reject();
                }

                if (signals.add) {
                    signals.add.abort();
                }

                const resp = await this.$http.put('/bookmarks', bookmark);

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
                if (!await this.userStore.getUserStatus()) {
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
        },

        async queryMergeDefaultBookmark() {
            try {
                if (!await this.userStore.getUserStatus()) {
                    return Promise.reject();
                }

                const defaultBookmarkStore = useDefaultBookmarkStore();
                const resp = await this.$http.patch('/bookmarks', cloneDeep(defaultBookmarkStore.getBookmarks));

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                await this.queryGetBookmarks();

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            }
        }
    }
});
