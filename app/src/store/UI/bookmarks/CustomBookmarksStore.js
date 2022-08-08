import { defineStore } from 'pinia';
import { v4 as uuid } from 'uuid';
import { random } from 'lodash';

// eslint-disable-next-line import/prefer-default-export
export const useCustomBookmarkStore = defineStore('CustomBookmarkStore', {
    state: () => ({
        bookmarks: []
    }),

    getters: {
        getBookmarks: state => state.bookmarks
    },

    actions: {
        generateBookmarks() {
            if (this.bookmarks.length) {
                this.bookmarks = [];
            }

            for (let i = 0; i < 4; i++) {
                const groupUUID = uuid();

                this.bookmarks.push({
                    uuid: groupUUID,
                    name: `Группа №${ random(1, 100) }`,
                    order: i
                });

                for (let k = 0; k < 5; k++) {
                    const catUUID = uuid();

                    this.bookmarks.push({
                        uuid: catUUID,
                        name: `Категория №${ random(1, 100) }`,
                        order: k,
                        parentUUID: groupUUID
                    });

                    for (let j = 0; j < 4; j++) {
                        this.bookmarks.push({
                            uuid: uuid(),
                            name: `Закладка №${ random(1, 100) }`,
                            order: j,
                            parentUUID: catUUID,
                            type: 'spell',
                            url: '/spells/Analyze_device'
                        });
                    }
                }
            }
        }
    }
});
