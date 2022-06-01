import { defineStore } from 'pinia';
import _ from 'lodash';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';
import errorHandler from '@/helpers/errorHandler';

const DB_NAME = 'classes';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useClassesStore = defineStore('ClassesStore', {
    state: () => ({
        classes: [],
        selectedClass: undefined,
        currentArchetypes: [],
        filter: undefined
    }),

    getters: {
        getFilter: state => state.filter,
        getClasses: state => state.classes,
        getCurrentClass: state => state.selectedClass,
        getCurrentArchetypes: state => state.currentArchetypes
    },

    actions: {
        async initFilter(storeKey) {
            try {
                this.filter = new FilterService();

                const filterOptions = {
                    dbName: DB_NAME,
                }

                if (storeKey) {
                    filterOptions.storeKey = storeKey;
                }

                await this.filter.init({
                    ...filterOptions,
                    url: '/filters/classes'
                });
            } catch (err) {
                errorHandler(err);
            }
        },

        /**
         * @param {{searchStr: string, url: string}} options
         * @returns {Promise<void>}
         */
        async classesQuery(options) {
            const opts = {
                searchStr: '',
                url: '/classes',
                ...options
            }

            try {
                const apiOptions = {
                    page: 1,
                    limit: 30,
                    search: {
                        exact: false,
                        value: opts.searchStr
                    },
                    order: [{
                        field: 'level',
                        direction: 'asc'
                    }, {
                        field: 'name',
                        direction: 'asc'
                    }]
                };

                if (this.filter && this.filter.getFilterState && this.filter.isCustomized) {
                    apiOptions.filter = this.filter.getQueryParams;
                }

                const res = await http.post(opts.url, apiOptions);

                if (res.status !== 200) {
                    errorHandler(res.statusText);

                    return;
                }

                const result = [];
                const sort = list => {
                    const types = list.map(arch => arch.type);
                    const typesSorted = _.uniqWith(_.sortBy(types, ['order']), _.isEqual);
                    const formatted = [];

                    let index = 0;

                    for (let i = 0; i < typesSorted.length; i++) {
                        if (i === 0 || i % 2 === 0) {
                            formatted.push([]);

                            index++;
                        }

                        formatted[index - 1].push({
                            name: typesSorted[i].name,
                            list: list.filter(arch => arch.type.name === typesSorted[i].name)
                        });
                    }

                    return formatted
                }

                for (let i = 0; i < res.data.length; i++) {
                    result.push({
                        ...res.data[i],
                        archetypes: sort(res.data[i].archetypes),
                    })
                }

                this.classes = result;
            } catch (err) {
                errorHandler(err);
            }
        },

        async classInfoQuery(url) {
            try {
                const getArchetypes = list => {
                    const sorted = [];

                    for (let i = 0; i < list.length; i++) {
                        const el = list[i];

                        if (Array.isArray(el) && el.length) {
                            sorted.push(...el);
                        }
                    }

                    return sorted.map(el => ({
                        group: el.name,
                        list: el.list.map(arch => ({
                            name: arch.name.rus,
                            source: arch.source.shortName,
                            url: arch.url
                        }))
                    }));
                }

                const classLink = this.classes.find(classItem => url.match(classItem.url));

                this.currentArchetypes = getArchetypes(classLink.archetypes);

                const res = await http.post(url);

                if (res.status !== 200) {
                    errorHandler(res.statusText);

                    return;
                }

                this.selectedClass = {
                    ...res.data,
                    tabs: _.sortBy(res.data.tabs, ['order'])
                        .map((tab, index) => ({
                            ...tab,
                            active: index === 0
                        }))
                };
            } catch (err) {
                errorHandler(err);
            }
        },

        deselectClass() {
            this.selectedClass = undefined;
        }
    }
});
