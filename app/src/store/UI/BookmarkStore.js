import { defineStore } from 'pinia';
import cloneDeep from 'lodash/cloneDeep';

// eslint-disable-next-line import/prefer-default-export
export const useBookmarkStore = defineStore('BookmarkStore', {
    state: () => ({
        section: 'Без группы',
        items: []
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
        setItems() {
        },

        setSection(name) {
            if (!name) {
                return;
            }

            this.section = name;
        },

        addBookmark(url, label, section = this.section) {
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
        },

        removeBookmark(url) {
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
        },

        updateBookmark(url, label, section = this.section) {
            if (this.isBookmarkSaved(url)) {
                this.removeBookmark(url);

                return;
            }

            this.addBookmark(url, label, section);
        }
    }
});
