import { defineStore } from 'pinia';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';
import errorHandler from '@/helpers/errorHandler';

const DB_NAME = 'traits';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useTraitsStore = defineStore('TraitsStore', {
    state: () => ({
        traits: [],
        selectedTrait: undefined,
        filter: undefined
    }),

    getters: {
        getFilter: state => state.filter,
        getTraits: state => state.traits,
        getCurrentTrait: state => state.selectedTrait
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
                    url: '/filters/traits'
                });
            } catch (err) {
                errorHandler(err);
            }
        },

        /**
         * @param {{searchStr: string, url: string}} options
         * @returns {Promise<void>}
         */
        async traitsQuery(options) {
            const opts = {
                searchStr: '',
                url: '/traits',
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

                this.traits = resp.data;
            } catch (err) {
                errorHandler(err)
            }
        },

        async traitInfoQuery(traitName) {
            try {
                const resp = await http.post(`/traits/${ traitName }`);

                this.selectedTrait = resp.data;
            } catch (err) {
                errorHandler(err)
            }
        }
    }
})
