import { defineStore } from 'pinia';
import cloneDeep from 'lodash/cloneDeep';
import localforage from 'localforage';
import { DB_NAME } from '@/common/const/UI';
import errorHandler from '@/common/helpers/errorHandler';
import isArray from 'lodash/isArray';
import { v4 as uuidV4 } from 'uuid';

// eslint-disable-next-line import/prefer-default-export
export const useDefaultBookmarkStore = defineStore('DefaultBookmarkStore', {
    state: () => ({
        bookmarks: [],
        store: localforage.createInstance({
            name: DB_NAME,
            storeName: 'bookmarks'
        })
    }),

    getters: {
        getBookmarks: state => state.bookmarks,
        isBookmarkSaved(state) {
            return url => cloneDeep(state.bookmarks).findIndex(bookmark => bookmark.url === url) >= 0;
        }
    },

    actions: {
        async convertOldBookmarks() {
            try {
                await this.store.ready();

                const oldFormat = await this.store.getItem('saved');

                if (isArray(oldFormat) && oldFormat.length) {
                    const parent = cloneDeep({
                        uuid: uuidV4(),
                        order: 0,
                        name: 'Общие',
                        default: true
                    });
                    const list = [parent];

                    for (let i = 0; i < oldFormat.length; i++) {
                        const category = oldFormat[i];
                        const updatedCat = cloneDeep({
                            uuid: uuidV4(),
                            order: i,
                            name: category.label,
                            parentUUID: parent.uuid
                        });

                        list.push(updatedCat);

                        for (let j = 0; j < category.links.length; j++) {
                            const bookmark = category.links[j];

                            list.push({
                                uuid: uuidV4(),
                                order: j,
                                name: bookmark.label,
                                url: bookmark.url,
                                parentUUID: updatedCat.uuid
                            });
                        }
                    }

                    this.bookmarks = list;

                    await this.saveBookmarks();

                    await this.store.removeItem('saved');
                }
            } catch (err) {
                errorHandler(err);
            }
        },

        async restoreBookmarks() {
            try {
                await this.store.ready();

                await this.convertOldBookmarks();

                const restored = await this.store.getItem('default');

                this.bookmarks = isArray(restored) && restored.length
                    ? cloneDeep(restored)
                    : [];
            } catch (err) {
                errorHandler(err);
            }
        },

        async saveBookmarks() {
            try {
                await this.store.ready();

                await this.store.setItem('default', cloneDeep(this.bookmarks));
            } catch (err) {
                errorHandler(err);
            }
        },

        async getCategories() {
            try {
                const resp = await this.$http.get('/bookmarks/categories');

                if (resp.status !== 200) {
                    return Promise.reject(resp.statusText);
                }

                return Promise.resolve(resp.data);
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async getCategoryByURL(url) {
            try {
                const resp = await this.$http.get('/bookmarks/category', {
                    url: encodeURI(url)
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

        createCategory(category) {
            const parent = this.bookmarks.find(bookmarks => bookmarks.default);
            const newCategory = cloneDeep({
                uuid: uuidV4(),
                name: category.name,
                order: category.order,
                parentUUID: parent.uuid
            });

            this.bookmarks.push(newCategory);

            return newCategory;
        },

        async addBookmark(url, name, category) {
            try {
                if (!url || !name) {
                    return Promise.reject();
                }

                let cat;

                if (typeof category === 'string') {
                    cat = await this.getCategoryByCode(category);
                }

                if (!cat) {
                    cat = await this.getCategoryByURL(url);
                }

                const savedCat = this.bookmarks.find(bookmark => bookmark.name === cat.name);

                if (!savedCat) {
                    cat = await this.createCategory(cat);
                }

                this.bookmarks.push(cloneDeep({
                    uuid: uuidV4(),
                    name,
                    url,
                    order: this.bookmarks.filter(bookmark => bookmark.parentUUID === cat.uuid).length,
                    parentUUID: cat.uuid
                }));

                await this.saveBookmarks();

                return Promise.resolve();
            } catch (err) {
                return Promise.reject(err);
            }
        },

        async removeBookmark(url) {
            if (!url) {
                return;
            }

            const indexes = this.isBookmarkSaved(url, true);

            if (!indexes) {
                return;
            }

            const { group, link } = indexes;

            this.bookmarks[group].childList.splice(link, 1);

            if (!this.bookmarks[group].childList.length) {
                this.bookmarks.splice(group, 1);
            }

            await this.saveBookmarks();
        },

        async updateBookmark(url, name, category) {
            if (this.isBookmarkSaved(url)) {
                await this.removeBookmark(url);

                return;
            }

            await this.addBookmark(url, name, category);
        }
    }
});
