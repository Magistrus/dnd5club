import { defineStore } from 'pinia';
import HTTPService from '@/utils/HTTPService';
import { useFilterStore } from '@/store/FilterStore/FilterStore';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useBackgroundsStore = defineStore('BackgroundsStore', {
    state: () => ({
        backgrounds: [],
        selectedBackground: undefined
    }),

    getters: {
        getBackgrounds: state => state.backgrounds,
        getCurrentBackground: state => state.selectedBackground
    },

    actions: {
        async backgroundsQuery() {
            try {
                const filterStore = useFilterStore();

                await filterStore.initFilter('backgrounds');

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

                const resp = await http.post('/backgrounds', apiOptions);

                this.backgrounds = resp.data;
            } catch (err) {
                console.error(err)
            }
        },

        async backgroundInfoQuery(spellName) {
            try {
                const resp = await http.post(`/backgrounds/${ spellName }`);

                this.selectedBackground = resp.data;
            } catch (err) {
                console.error(err)
            }
        }
    }
})
