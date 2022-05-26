import { defineStore } from 'pinia';
import HTTPService from '@/utils/HTTPService';
import { useFilterStore } from '@/store/FilterStore/FilterStore';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useSpellsStore = defineStore('SpellsStore', {
    state: () => ({
        spells: [],
        selectedSpell: undefined
    }),

    getters: {
        getSpells: state => state.spells,
        getCurrentSpell: state => state.selectedSpell
    },

    actions: {
        async spellsQuery() {
            try {
                const filterStore = useFilterStore();

                await filterStore.initFilter('spells');

                const apiOptions = {
                    page: 1,
                    limit: 30,
                    search: {
                        exact: false,
                        value: ''
                    },
                    order: [{
                        field: 'level',
                        direction: 'asc'
                    }, {
                        field: 'name',
                        direction: 'asc'
                    }]
                };

                if (filterStore.getFilter && filterStore.isFilterCustomized) {
                    apiOptions.filter = filterStore.getQueryParams();
                }

                const resp = await http.post('/spells', apiOptions);

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
