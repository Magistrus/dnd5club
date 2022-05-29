import { defineStore } from 'pinia';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';

const DB_NAME = 'spells';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useSpellsStore = defineStore('SpellsStore', {
    state: () => ({
        spells: [],
        selectedSpell: undefined,
        filter: undefined
    }),

    getters: {
        getFilter: state => state.filter,
        getSpells: state => state.spells,
        getCurrentSpell: state => state.selectedSpell
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
                    url: '/filters/spells'
                });
            } catch (err) {
                console.error(err);
            }
        },

        /**
         * @param {{searchStr: string, url: string}} options
         * @returns {Promise<void>}
         */
        async spellsQuery(options) {
            const opts = {
                searchStr: '',
                url: '/spells',
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

                if (this.filter && this.filter.getState && this.filter.isCustomized) {
                    apiOptions.filter = this.filter.getQueryParams;
                }

                const resp = await http.post(opts.url, apiOptions);

                this.spells = resp.data;
            } catch (err) {
                console.error(err)
            }
        },

        async spellInfoQuery(spellName) {
            try {
                const resp = await http.post(`/spells/${ spellName }`);

                this.selectedSpell = resp.data;
            } catch (err) {
                console.error(err)
            }
        }
    }
})
