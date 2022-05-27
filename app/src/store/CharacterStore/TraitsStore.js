import { defineStore } from 'pinia';
import HTTPService from '@/utils/HTTPService';
import { useFilterStore } from '@/store/FilterStore/FilterStore';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useTraitsStore = defineStore('TraitsStore', {
    state: () => ({
        traits: [],
        selectedTrait: undefined
    }),

    getters: {
        getTraits: state => state.traits,
        getCurrentTrait: state => state.selectedTrait
    },

    actions: {
        async traitsQuery() {
            try {
                const filterStore = useFilterStore();

                await filterStore.initFilter('traits');

                const apiOptions = {
                    page: 1,
                    limit: 120,
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

                const resp = await http.post('/traits', apiOptions);

                this.traits = resp.data;
            } catch (err) {
                console.error(err)
            }
        },

        async traitInfoQuery(traitName) {
            try {
                const resp = await http.post(`/traits/${ traitName }`);

                this.selectedTrait = resp.data;
            } catch (err) {
                console.error(err)
            }
        }
    }
})
