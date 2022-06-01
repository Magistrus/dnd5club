import { defineStore } from 'pinia';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';
import errorHandler from '@/helpers/errorHandler';

const DB_NAME = 'backgrounds';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useBackgroundsStore = defineStore('BackgroundsStore', {
    state: () => ({
        backgrounds: [],
        selectedBackground: undefined,
        filter: undefined
    }),

    getters: {
        getFilter: state => state.filter,
        getBackgrounds: state => state.backgrounds,
        getCurrentBackground: state => state.selectedBackground
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
                    url: '/filters/backgrounds'
                });
            } catch (err) {
                errorHandler(err);
            }
        },

        /**
         * @param {{searchStr: string, url: string}} options
         * @returns {Promise<void>}
         */
        async backgroundsQuery(options) {
            const opts = {
                searchStr: '',
                url: '/backgrounds',
                ...options
            }

            try {
                const apiOptions = {
                    page: 1,
                    limit: 70,
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

                const resp = await http.post(opts.url, apiOptions);

                this.backgrounds = resp.data;
            } catch (err) {
                errorHandler(err)
            }
        },

        async backgroundInfoQuery(spellName) {
            try {
                const resp = await http.post(`/backgrounds/${ spellName }`);

                this.selectedBackground = resp.data;
            } catch (err) {
                errorHandler(err)
            }
        }
    }
})
