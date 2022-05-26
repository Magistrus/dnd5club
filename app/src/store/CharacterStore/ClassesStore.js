import { defineStore } from 'pinia';
import _ from 'lodash';
import HTTPService from '@/utils/HTTPService';
import { useFilterStore } from '@/store/FilterStore/FilterStore';

const http = new HTTPService();

// eslint-disable-next-line import/prefer-default-export
export const useClassesStore = defineStore('ClassesStore', {
    state: () => ({
        classes: [],
        selectedClass: undefined,
        currentArchetypes: [],
    }),

    getters: {
        getClasses: state => state.classes,
        getCurrentClass: state => state.selectedClass,
        getCurrentArchetypes: state => state.currentArchetypes
    },

    actions: {
        async classesQuery() {
            try {
                const filterStore = useFilterStore();

                await filterStore.initFilter('classes');

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

                const res = await http.post('/classes', apiOptions);

                if (res.status !== 200) {
                    console.error(res.statusText);

                    return;
                }

                const result = [];
                const sort = list => {
                    const types = list.map(arch => arch.type);
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
                            list: list.filter(arch => arch.type.name === typesSorted[i].name)
                        });
                    }

                    return formatted
                }

                for (let i = 0; i < res.data.length; i++) {
                    result.push({
                        ...res.data[i],
                        archetypes: sort(res.data[i].archetypes),
                    })
                }

                this.classes = result;
            } catch (err) {
                console.error(err);
            }
        },

        async classInfoQuery(className, archName) {
            try {
                let url = `/classes/${ className }`;

                const getArchetypes = list => {
                    const sorted = [];

                    for (let i = 0; i < list.length; i++) {
                        const el = list[i];

                        if (Array.isArray(el) && el.length) {
                            sorted.push(...el);
                        }
                    }

                    return sorted.map(el => ({
                        group: el.name,
                        list: el.list.map(arch => ({
                            name: arch.name.rus,
                            source: arch.source.shortName,
                            url: arch.url
                        }))
                    }));
                }

                const classLink = this.classes.find(classItem => classItem.url === url);

                this.currentArchetypes = getArchetypes(classLink.archetypes);

                if (archName) {
                    url += `/${ archName }`;
                }

                const res = await http.post(url);

                if (res.status !== 200) {
                    console.error(res.statusText);

                    return;
                }

                this.selectedClass = {
                    ...res.data,
                    tabs: _.sortBy(res.data.tabs, ['order'])
                        .map((tab, index) => ({
                            ...tab,
                            active: index === 0
                        }))
                };
            } catch (err) {
                console.error(err);
            }
        },

        deselectClass() {
            this.selectedClass = undefined;
        }
    }
});
