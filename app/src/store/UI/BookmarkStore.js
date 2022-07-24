import { defineStore } from 'pinia';
import cloneDeep from 'lodash/cloneDeep';
import localforage from 'localforage';
import { DB_NAME } from '@/common/const/UI';
import errorHandler from '@/common/helpers/errorHandler';
import { useUserStore } from '@/store/UI/UserStore';
import isArray from 'lodash/isArray';

// eslint-disable-next-line import/prefer-default-export
export const useBookmarkStore = defineStore('BookmarkStore', {
    state: () => ({
        section: 'Без группы',
        items: [],
        store: localforage.createInstance({
            name: DB_NAME,
            storeName: 'bookmarks'
        })
    }),

    getters: {
        getItems: state => state.items,
        isBookmarkSaved(state) {
            return (url, returnIndexes) => {
                const items = cloneDeep(state.items);
                const indexes = {
                    group: -1,
                    link: -1
                };

                let found = false;

                for (let groupIndex = 0; groupIndex < items.length && !found; groupIndex++) {
                    if (!items[groupIndex]?.links?.length) {
                        continue;
                    }

                    for (let linkIndex = 0; linkIndex < items[groupIndex].links.length && !found; linkIndex++) {
                        if (items[groupIndex]?.links[linkIndex]?.url === url) {
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
        async restoreItems() {
            try {
                await this.store.ready();

                const restored = await this.store.getItem('saved');
                const userStore = useUserStore();

                await userStore.updateUserFromSession();

                if (userStore.isAuthorized) {
                    console.log('Aiuthorized');

                    // const resp = await this.getProfileBookmark();
                    //
                    // if (resp.status === 200 && resp.data?.length) {
                    //     restored = resp.data;
                    // }
                }

                this.items = isArray(restored) && restored?.length
                    ? restored
                    : [];
            } catch (err) {
                errorHandler(err);
            }
        },

        async getProfileBookmark() {
            try {
                await console.log('success');
            } catch (err) {
                errorHandler(err);
            }
        },

        async saveBookmarks() {
            try {
                await this.store.ready();

                await this.store.setItem('saved', cloneDeep(this.items));
            } catch (err) {
                errorHandler(err);
            }
        },

        setSection(name) {
            if (!name) {
                return;
            }

            this.section = name;
        },

        async addBookmark(url, label, section = this.section) {
            if (!url || !label) {
                return;
            }

            this.$patch(state => {
                const items = cloneDeep(state.items);

                let groupIndex = items.findIndex(group => group.label === section);

                if (groupIndex === -1) {
                    items.push({
                        label: section,
                        links: []
                    });

                    groupIndex = items.findIndex(group => group.label === section);
                }

                items[groupIndex].links.push({
                    label,
                    url
                });

                state.items = items;
            });

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

            this.items[group].links.splice(link, 1);

            if (!this.items[group].links.length) {
                this.items.splice(group, 1);
            }

            await this.saveBookmarks();
        },

        async updateBookmark(url, label, section = this.section) {
            if (this.isBookmarkSaved(url)) {
                await this.removeBookmark(url);

                return;
            }

            await this.addBookmark(url, label, section);
        }
    }
});
