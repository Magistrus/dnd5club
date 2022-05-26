import { defineStore } from 'pinia';
import _ from 'lodash';
import HTTPService from '@/utils/HTTPService';
import { useFilterStore } from '@/store/FilterStore/FilterStore';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useRacesStore = defineStore('RacesStore', {
    state: () => ({
        races: [],
        selectedRace: undefined
    }),

    getters: {
        getRaces: state => state.races,
        getCurrentRace: state => state.selectedRace
    },

    actions: {
        async racesQuery() {
            try {
                const filterStore = useFilterStore();

                await filterStore.initFilter('races');

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

                if (filterStore.getFilter) {
                    apiOptions.filter = filterStore.getFilter;
                }

                const res = await http.post('/races', apiOptions);

                if (res.status !== 200) {
                    console.error(res.statusText);

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
                console.error(err);
            }
        },

        async raceInfoQuery(raceName, subrace) {
            try {
                let url = `/races/${ raceName }`;

                if (subrace) {
                    url += `/${ subrace }`;
                }

                const res = await http.post(url);

                if (res.status !== 200) {
                    console.error(res.statusText);

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
                console.error(err);
            }
        },

        deselectRace() {
            this.selectedRace = undefined;
        }
    }
});
