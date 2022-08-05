import { defineStore } from 'pinia';
import cloneDeep from 'lodash/cloneDeep';
import localforage from 'localforage';
import { DB_NAME } from '@/common/const/UI';
import errorHandler from '@/common/helpers/errorHandler';
import isArray from 'lodash/isArray';
import sortBy from 'lodash/sortBy';
import { getSectionObj } from '@/common/helpers/bookmarkSections';

// eslint-disable-next-line import/prefer-default-export
export const useDefaultBookmarkStore = defineStore('DefaultBookmarkStore', {
    state: () => ({
        section: getSectionObj('none'),
        bookmarks: [],
        store: localforage.createInstance({
            name: DB_NAME,
            storeName: 'bookmarks'
        })
    }),

    getters: {
        getBookmarks: state => sortBy(state.bookmarks, [o => o.order]),
        isBookmarkSaved(state) {
            return (url, returnIndexes) => {
                const bookmarks = cloneDeep(state.bookmarks);
                const indexes = {
                    group: -1,
                    link: -1
                };

                let found = false;

                for (let groupIndex = 0; groupIndex < bookmarks.length && !found; groupIndex++) {
                    if (!bookmarks[groupIndex]?.childList?.length) {
                        continue;
                    }

                    for (let linkIndex = 0; linkIndex < bookmarks[groupIndex].childList.length && !found; linkIndex++) {
                        if (bookmarks[groupIndex]?.childList[linkIndex]?.url === url) {
                            found = true;
                            indexes.group = groupIndex;
                            indexes.link = linkIndex;
                        }
                    }
                }

                const result = returnIndexes ? indexes : true;

                return indexes.group > -1 && indexes.link > -1
                    ? result
                    : null;
            };
        }
    },

    actions: {
        async restoreBookmarks() {
            try {
                await this.store.ready();

                const oldFormat = await this.store.getItem('saved');
                const restored = await this.store.getItem('default');

                if (isArray(oldFormat) && oldFormat.length) {
                    this.bookmarks = oldFormat.map(group => ({
                        ...getSectionObj(group.label),
                        childList: group.links.map(link => ({
                            name: link.label,
                            url: link.url
                        }))
                    }));

                    await this.saveBookmarks();
                    await this.store.removeItem('saved');

                    return;
                }

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

        setSection(code) {
            const section = getSectionObj(code);

            this.section = section.name;
        },

        async addBookmark(url, name, section = this.section) {
            if (!url || !name) {
                return;
            }

            const bookmarks = cloneDeep(this.bookmarks);
            const getIndex = () => bookmarks.findIndex(group => group.name === section);

            let groupIndex = getIndex();

            if (groupIndex === -1) {
                bookmarks.push({
                    ...getSectionObj(section),
                    childList: []
                });

                groupIndex = getIndex();
            }

            bookmarks[groupIndex].childList.push({
                name,
                url
            });

            this.bookmarks = bookmarks;

            await this.saveBookmarks();
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

        async updateBookmark(url, name, section = this.section) {
            if (this.isBookmarkSaved(url)) {
                await this.removeBookmark(url);

                return;
            }

            await this.addBookmark(url, name, section);
        }
    }
});
