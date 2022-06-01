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
        filter: undefined,
        config: {
            page: 0,
            url: '/classes',
        },
        controllers: {
            classesQuery: undefined,
            classInfoQuery: undefined
        }
    }),

    getters: {
        getFilter: state => state.filter,
        getClasses: state => state.classes,
    },

    actions: {
        async initFilter(storeKey, url) {
            try {
                this.filter = new FilterService();

                const filterOptions = {
                    dbName: DB_NAME,
                    url: '/filters/classes'
                }

                if (storeKey) {
                    filterOptions.storeKey = storeKey;
                }

                if (url) {
                    filterOptions.url = url
                }

                await this.filter.init(filterOptions);
            } catch (err) {
                errorHandler(err);
            }
        },

        /**
         * @param {{}} options
         * @param {number} options.page
         * @param {number} options.limit
         * @param {object} options.filter
         * @param {boolean} options.search.exact
         * @param {string} options.search.value
         * @param {{field: string, direction: 'asc' | 'desc'}[]} options.order
         * @returns {Promise<*[]>}
         */
        async classesQuery(options = {}) {
            try {
                if (this.controllers.classesQuery) {
                    this.controllers.classesQuery.abort()
                }

                this.controllers.classesQuery = new AbortController();

                const apiOptions = {
                    page: 0,
                    limit: -1,
                    search: {
                        exact: false,
                        value: this.filter?.getSearchState || ''
                    },
                    order: [{
                        field: 'level',
                        direction: 'asc'
                    }, {
                        field: 'name',
                        direction: 'asc'
                    }],
                    ...options
                };

                const { data } = await http.post(this.config.url, apiOptions, this.controllers.classesQuery.signal);

                this.controllers.classesQuery = undefined;

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

                for (let i = 0; i < data.length; i++) {
                    result.push({
                        ...data[i],
                        archetypes: sort(data[i].archetypes),
                    })
                }

                return result
            } catch (err) {
                errorHandler(err);

                return []
            }
        },

        async initClasses(url) {
            this.clearClasses();
            this.clearConfig();

            if (url) {
                this.config.url = url
            }

            const config = {
                page: this.config.page,
                limit: this.config.limit,
            }

            if (this.filter && this.filter.isCustomized) {
                config.filter = this.filter.getQueryParams;
            }

            this.classes = await this.classesQuery(config);
        },

        async classInfoQuery(url) {
            try {
                if (this.controllers.classInfoQuery) {
                    this.controllers.classInfoQuery.abort()
                }

                this.controllers.classInfoQuery = new AbortController();

                const { data } = await http.post(url, {}, this.controllers.classInfoQuery.signal);

                this.controllers.classInfoQuery = undefined;

                return {
                    ...data,
                    tabs: _.sortBy(data.tabs, ['order'])
                        .map((tab, index) => ({
                            ...tab,
                            active: index === 0
                        }))
                };
            } catch (err) {
                errorHandler(err);

                return undefined
            }
        },

        clearClasses() {
            this.classes = [];
        },

        clearFilter() {
            this.filter = undefined;
        },

        clearConfig() {
            this.config = {
                page: 0,
                url: '/classes',
            };
        },

        clearStore() {
            this.clearClasses();
            this.clearFilter();
            this.clearConfig();
        }
    }
});
