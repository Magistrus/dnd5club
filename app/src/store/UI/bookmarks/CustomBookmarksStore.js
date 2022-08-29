import { defineStore } from 'pinia';
import { useUserStore } from '@/store/UI/UserStore';
import sortBy from 'lodash/sortBy';
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
        getGroupBookmarks: state => {
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
        getGroups: state => sortBy(state.bookmarks.filter(bookmark => !bookmark.parentUUID), [o => o.order]),
        isBookmarkSaved: state => url => state.bookmarks.findIndex(bookmark => bookmark.url === url) >= 0,
        isBookmarkSavedInGroup: state => (url, groupUUID) => {
            const categoriesUUIDs = state.bookmarks
                .filter(item => item.parentUUID === groupUUID)
                .map(item => item.uuid);

            return state.bookmarks
                .findIndex(item => categoriesUUIDs.includes(item.parentUUID) && item.url === url) > -1;
        },
        getBookmarkParentUUIDs: state => url => {
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

        async queryAddBookmark(bookmark) {
            try {
                if (!await this.userStore.getUserStatus()) {
                    return Promise.reject();
                }

                if (!bookmark?.name) {
                    return Promise.reject(new Error('Name is undefined'));
                }

                if (signals.add) {
                    signals.add.abort();
                }

                signals.add = new AbortController();

                const resp = await this.$http.post('/bookmarks', cloneDeep(bookmark), signals.add.signal);

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                await this.queryGetBookmarks();

                return Promise.resolve(resp.data);
            } catch (err) {
                return Promise.reject(err);
            } finally {
                signals.add = undefined;
            }
        },

        async queryUpdateBookmark(bookmark) {
            try {
                if (!await this.userStore.getUserStatus()) {
                    return Promise.reject();
                }

                if (!bookmark?.name) {
                    return Promise.reject(new Error('Name is undefined'));
                }

                if (signals.add) {
                    signals.add.abort();
                }

                signals.add = new AbortController();

                const resp = await this.$http.put('/bookmarks', cloneDeep(bookmark), signals.add.signal);

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                await this.queryGetBookmarks();

                return Promise.resolve(resp.data);
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

                if (!uuid) {
                    return Promise.reject(new Error('UUID is undefined'));
                }

                if (signals.delete) {
                    signals.delete.abort();
                }

                signals.delete = new AbortController();

                const resp = await this.$http.delete(`/bookmarks/${ uuid }`, {}, signals.delete.signal);

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

        async createDefaultGroup() {
            try {
                const defaultGroup = await this.queryAddBookmark(cloneDeep({
                    name: 'Общие',
                    order: -1
                }));

                return Promise.resolve(defaultGroup);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async getDefaultGroup() {
            try {
                const bookmarks = await this.queryGetBookmarks();

                let group = bookmarks.find(bookmark => bookmark.order === -1);

                if (!group) {
                    group = await this.createDefaultGroup();
                }

                return Promise.resolve(group);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async createCategory(category, groupUUID) {
            try {
                const newCategory = await this.queryAddBookmark(cloneDeep({
                    name: category.name,
                    parentUUID: groupUUID
                }));

                return Promise.resolve(newCategory);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async getCategoryByURL(url) {
            try {
                const resp = await this.$http.get('/bookmarks/category', {
                    url: encodeURIComponent(url)
                });

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                return Promise.resolve(resp.data);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async getCategoryByCode(code) {
            try {
                const resp = await this.$http.get('/bookmarks/category', { code });

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                return Promise.resolve(resp.data);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async getCategoryInGroup({
            url, category, groupUUID
        }) {
            try {
                let cat;

                if (typeof category === 'string') {
                    cat = await this.getCategoryByCode(category);
                }

                if (!cat) {
                    cat = await this.getCategoryByURL(url);
                }

                const categories = this.bookmarks.filter(item => item.parentUUID === groupUUID);

                let savedCat = categories.find(bookmark => bookmark.name === cat.name);

                if (!savedCat) {
                    savedCat = await this.createCategory(cat, groupUUID);
                }

                return Promise.resolve(savedCat);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async addBookmarkInGroup({
            url,
            name,
            category,
            groupUUID
        }) {
            try {
                if (!url || !name) {
                    return Promise.reject();
                }

                const savedCat = await this.getCategoryInGroup({
                    url,
                    category,
                    groupUUID
                });

                await this.queryAddBookmark(cloneDeep({
                    url,
                    name,
                    parentUUID: savedCat.uuid
                }));

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async getSavedBookmarkInGroup({ url, groupUUID }) {
            let bookmarks;

            try {
                bookmarks = await this.queryGetBookmarks();
            } catch (err) {
                return Promise.reject(err);
            }

            const categoriesUUIDs = bookmarks
                .filter(item => item.parentUUID === groupUUID)
                .map(item => item.uuid);

            return bookmarks
                .find(item => item.url === url && categoriesUUIDs.includes(item.parentUUID));
        },

        async updateBookmarkInGroup({
            url,
            name,
            category,
            groupUUID
        }) {
            const bookmark = await this.getSavedBookmarkInGroup({
                url,
                groupUUID
            });

            if (bookmark) {
                try {
                    await this.queryDeleteBookmark(bookmark.uuid);

                    return Promise.resolve();
                } catch (err) {
                    return Promise.reject(err);
                }
            }

            try {
                await this.addBookmarkInGroup({
                    url,
                    name,
                    category,
                    groupUUID
                });

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            }
        }
    }
});
