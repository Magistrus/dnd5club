import { defineStore } from 'pinia';
import _ from 'lodash';
import HTTPService from '@/services/HTTPService';
import FilterService from '@/services/FilterService';
import errorHandler from '@/helpers/errorHandler';

const DB_NAME = 'races';
const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useRacesStore = defineStore('RacesStore', {
    state: () => ({
        races: [],
        selectedRace: undefined,
        filter: undefined
    }),

    getters: {
        getFilter: state => state.filter,
        getRaces: state => state.races,
        getCurrentRace: state => state.selectedRace
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
                    url: '/filters/races'
                });
            } catch (err) {
                errorHandler(err);
            }
        },

        /**
         * @param {{searchStr: string, url: string}} options
         * @returns {Promise<void>}
         */
        async racesQuery(options) {
            const opts = {
                searchStr: '',
                url: '/races',
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

                if (this.filter && this.filter.getFilterState && this.filter.isCustomized) {
                    apiOptions.filter = this.filter.getQueryParams;
                }

                const res = await http.post(opts.url, apiOptions);

                if (res.status !== 200) {
                    errorHandler(res.statusText);

                    return;
                }

                const result = [];
                const sort = list => {
                    const types = list.map(subrace => subrace.type);
                    const typesSorted = _.uniqWith(_.sortBy(types, ['order']), _.isEqual);
                    const formatted = [];

                    let index = 0;

                    for (let i = 0; i < typesSorted.length; i++) {
                        if (i === 0 || i % 2 === 0) {
                            formatted.push([]);

                            index++;
                        }

                        formatted[index - 1].push({
                            name: typesSorted[i].name,
                            list: list.filter(subrace => subrace.type.name === typesSorted[i].name)
                        });
                    }

                    return formatted
                }

                for (let i = 0; i < res.data.length; i++) {
                    if ('subraces' in res.data[i]) {
                        result.push({
                            ...res.data[i],
                            subraces: sort(res.data[i].subraces),
                        });

                        continue;
                    }

                    result.push({ ...res.data[i] })
                }

                this.races = result;
            } catch (err) {
                errorHandler(err);
            }
        },

        async raceInfoQuery(url) {
            try {
                const res = await http.post(url);

                if (res.status !== 200) {
                    errorHandler(res.statusText);

                    return;
                }

                this.$patch({
                    selectedRace: {
                        ...res.data,
                        tabs: _.sortBy(res.data.tabs, ['order'])
                            .map((tab, index) => ({
                                ...tab,
                                active: index === 0
                            }))
                    }
                });
            } catch (err) {
                errorHandler(err);
            }
        },

        deselectRace() {
            this.selectedRace = undefined;
        }
    }
});
