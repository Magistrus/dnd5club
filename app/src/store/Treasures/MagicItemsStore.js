import { defineStore } from 'pinia';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';
import errorHandler from '@/helpers/errorHandler';
import _ from 'lodash';

const DB_NAME = 'items';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useMagicItemsStore = defineStore('MagicItemsStore', {
    state: () => ({
        items: [],
        filter: undefined,
        config: {
            page: 0,
            limit: 70,
            end: false,
            url: '/magic/items', // TODO: Согласовать URL
        },
        customFilter: undefined,
        controllers: {
            itemsQuery: undefined,
            itemInfoQuery: undefined
        }
    }),

    getters: {
        getFilter: state => state.filter,
        getItems: state => state.items,
    },

    actions: {
        async initFilter(storeKey, customFilter) {
            try {
                this.clearFilter();
                this.clearCustomFilter();

                this.filter = new FilterService();

                const filterItems = {
                    dbName: DB_NAME,
                    url: '/filters/magic/items'
                }

                if (storeKey) {
                    filterItems.storeKey = storeKey;
                }

                if (customFilter) {
                    filterItems.customFilter = customFilter;
                    this.customFilter = _.cloneDeep(customFilter);
                }

                await this.filter.init(filterItems);
            } catch (err) {
                errorHandler(err);
            }
        },

        /**
         * @param {{}} items
         * @param {number} items.page
         * @param {number} items.limit
         * @param {object} items.filter
         * @param {boolean} items.search.exact
         * @param {string} items.search.value
         * @param {{field: string, direction: 'asc' | 'desc'}[]} items.order
         * @returns {Promise<*[]>}
         */
        async itemsQuery(items = {}) {
            try {
                if (this.controllers.itemsQuery) {
                    this.controllers.itemsQuery.abort()
                }

                this.controllers.itemsQuery = new AbortController();

                const apiItems = {
                    page: 0,
                    limit: -1,
                    search: {
                        exact: false,
                        value: this.filter?.getSearchState || ''
                    },
                    order: [{
                        field: 'name',
                        direction: 'asc'
                    }],
                    ...items
                };

                if (this.customFilter) {
                    apiItems.customFilter = this.customFilter;
                }

                const { data } = await http.post(this.config.url, apiItems, this.controllers.itemsQuery.signal);

                this.controllers.itemsQuery = undefined;

                return data
            } catch (err) {
                errorHandler(err);

                return [];
            }
        },

        async initItems(url) {
            this.clearItems();
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

            const items = await this.itemsQuery(config);

            this.items = items;
            this.config.end = items.length < config.limit;
        },

        async nextPage() {
            if (this.config.end) {
                return
            }

            const config = {
                page: this.config.page + 1,
                limit: this.config.limit,
            }

            if (this.filter && this.filter.isCustomized) {
                config.filter = this.filter.getQueryParams;
            }

            const items = await this.itemsQuery(config);

            this.config.page = config.page;
            this.config.end = items.length < config.limit;

            this.items.push(...items);
        },

        async itemInfoQuery(url) {
            try {
                if (this.controllers.itemInfoQuery) {
                    this.controllers.itemInfoQuery.abort()
                }

                this.controllers.itemInfoQuery = new AbortController();

                const resp = await http.post(url, {}, this.controllers.itemInfoQuery.signal);

                this.controllers.itemInfoQuery = undefined;

                return resp.data
            } catch (err) {
                errorHandler(err);

                return undefined;
            }
        },

        clearItems() {
            this.items = [];
        },

        clearFilter() {
            this.filter = undefined;
        },

        clearCustomFilter() {
            this.customFilter = undefined;
        },

        clearConfig() {
            this.config = {
                page: 0,
                limit: 70,
                end: false,
                url: '/magic/items',
            };
        },

        clearStore() {
            this.clearItems();
            this.clearFilter();
            this.clearConfig();
        }
    }
})
