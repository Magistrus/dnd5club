import { defineStore } from 'pinia';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';
import errorHandler from '@/helpers/errorHandler';

const DB_NAME = 'options';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useOptionsStore = defineStore('OptionsStore', {
    state: () => ({
        options: [],
        selectedOption: undefined,
        filter: undefined
    }),

    getters: {
        getFilter: state => state.filter,
        getOptions: state => state.options,
        getCurrentOption: state => state.selectedOption
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
                    url: '/filters/options'
                });
            } catch (err) {
                errorHandler(err);
            }
        },

        /**
         * @param {{searchStr: string, url: string}} options
         * @returns {Promise<void>}
         */
        async optionsQuery(options) {
            const opts = {
                searchStr: '',
                url: '/options',
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

                this.options = resp.data;
            } catch (err) {
                errorHandler(err)
            }
        },

        async optionInfoQuery(spellName) {
            try {
                const resp = await http.post(`/options/${ spellName }`);

                this.selectedOption = resp.data;
            } catch (err) {
                errorHandler(err)
            }
        }
    }
})
