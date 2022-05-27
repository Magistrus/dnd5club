import { defineStore } from 'pinia';
import HTTPService from '@/utils/HTTPService';
import { useFilterStore } from '@/store/FilterStore/FilterStore';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useOptionsStore = defineStore('OptionsStore', {
    state: () => ({
        options: [],
        selectedOption: undefined
    }),

    getters: {
        getOptions: state => state.options,
        getCurrentOption: state => state.selectedOption
    },

    actions: {
        async optionsQuery() {
            try {
                const filterStore = useFilterStore();

                await filterStore.initFilter('options');

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

                const resp = await http.post('/options', apiOptions);

                this.options = resp.data;
            } catch (err) {
                console.error(err)
            }
        },

        async optionInfoQuery(spellName) {
            try {
                const resp = await http.post(`/options/${ spellName }`);

                this.selectedOption = resp.data;
            } catch (err) {
                console.error(err)
            }
        }
    }
})
