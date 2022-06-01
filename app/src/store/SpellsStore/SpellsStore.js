import { defineStore } from 'pinia';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';

const DB_NAME = 'spells';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useSpellsStore = defineStore('SpellsStore', {
    state: () => ({
        spells: [],
        filter: undefined,
        config: {
            page: 0,
            limit: 70,
            end: false,
            url: '/spells',
        },
        controllers: {
            spellsQuery: undefined,
            spellInfoQuery: undefined
        }
    }),

    getters: {
        getFilter: state => state.filter,
        getSpells: state => state.spells,
    },

    actions: {
        async initFilter(storeKey, url) {
            try {
                this.filter = new FilterService();

                const filterOptions = {
                    dbName: DB_NAME,
                    url: '/filters/spells'
                }

                if (storeKey) {
                    filterOptions.storeKey = storeKey;
                }

                if (url) {
                    filterOptions.url = url
                }

                await this.filter.init(filterOptions);
            } catch (err) {
                console.error(err);
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
        async spellsQuery(options = {}) {
            try {
                if (this.controllers.spellsQuery) {
                    this.controllers.spellsQuery.abort()
                }

                this.controllers.spellsQuery = new AbortController();

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

                const { data } = await http.post(this.config.url, apiOptions, this.controllers.spellsQuery.signal);

                this.controllers.spellsQuery = undefined;

                return data
            } catch (err) {
                console.error(err);

                return [];
            }
        },

        async initSpells(url) {
            this.clearSpells();
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

            this.spells = await this.spellsQuery(config);
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

            const spells = await this.spellsQuery(config);

            this.config.page = config.page;
            this.config.end = spells.length < this.config.limit;

            this.spells.push(...spells);
        },

        async spellInfoQuery(url) {
            try {
                if (this.controllers.spellInfoQuery) {
                    this.controllers.spellInfoQuery.abort()
                }

                this.controllers.spellInfoQuery = new AbortController();

                const resp = await http.post(url, {}, this.controllers.spellInfoQuery.signal);

                this.controllers.spellInfoQuery = undefined;

                return resp.data
            } catch (err) {
                console.error(err);

                return undefined;
            }
        },

        clearSpells() {
            this.spells = [];
        },

        clearFilter() {
            this.filter = undefined;
        },

        clearConfig() {
            this.config = {
                page: 0,
                limit: 70,
                end: false,
                url: '/spells',
            };
        },

        clearStore() {
            this.clearSpells();
            this.clearFilter();
            this.clearConfig();
        }
    }
})
