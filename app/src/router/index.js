import { createRouter, createWebHistory } from 'vue-router';
import { useClassesStore } from '@/store/CharacterStore/ClassesStore';
import { useRacesStore } from '@/store/CharacterStore/RacesStore';
import { useSpellsStore } from '@/store/SpellsStore/SpellsStore';
import { useTraitsStore } from '@/store/CharacterStore/TraitsStore';
import { useBackgroundsStore } from '@/store/CharacterStore/BackgroundsStore';
import { useOptionsStore } from '@/store/CharacterStore/OptionsStore';

const routes = [
    {
        name: 'classes',
        path: '/classes',
        component: () => import('@/views/Character/Classes/ClassesView.vue'),
        beforeEnter: async (to, from, next) => {
            const store = useClassesStore();

            await store.initFilter();
            await store.classesQuery();

            next();
        },
        children: [
            {
                name: 'classDetail',
                path: ':className/:classArchetype?',
                component: () => import('@/views/Character/Classes/ClassDetail.vue'),
                beforeEnter: async (to, from, next) => {
                    const store = useClassesStore();

                    await store.classInfoQuery(to.params.className, to.params.classArchetype);

                    next()
                }
            }
        ]
    },
    {
        name: 'races',
        path: '/races',
        component: () => import('@/views/Character/Races/RacesView.vue'),
        beforeEnter: async (to, from, next) => {
            const store = useRacesStore();

            await store.initFilter();
            await store.racesQuery();

            next();
        },
        children: [
            {
                name: 'raceDetail',
                path: ':raceName/:subrace?',
                component: () => import('@/views/Character/Races/RaceDetail.vue'),
                beforeEnter: async (to, from, next) => {
                    const store = useRacesStore();

                    await store.initFilter();
                    await store.raceInfoQuery(to.params.raceName, to.params.subrace);

                    next()
                },
            }
        ]
    },
    {
        name: 'traits',
        path: '/traits',
        component: () => import('@/views/Character/Traits/TraitsView.vue'),
        beforeEnter: async (to, from, next) => {
            const store = useTraitsStore();

            await store.initFilter();
            await store.traitsQuery();

            next();
        },
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Traits/TraitDetail.vue'),
        //         beforeEnter: async (to, from, next) => {
        //             const store = useTraitsStore();
        //
        //             await store.traitInfoQuery(to.params.traitName);
        //
        //             next()
        //         },
        //     }
        // ]
    },
    {
        name: 'backgrounds',
        path: '/backgrounds',
        component: () => import('@/views/Character/Backgrounds/BackgroundsView.vue'),
        beforeEnter: async (to, from, next) => {
            const store = useBackgroundsStore();

            await store.initFilter();
            await store.backgroundsQuery();

            next();
        },
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Traits/TraitDetail.vue'),
        //         beforeEnter: async (to, from, next) => {
        //             const store = useTraitsStore();
        //
        //             await store.traitInfoQuery(to.params.traitName);
        //
        //             next()
        //         },
        //     }
        // ]
    },
    {
        name: 'options',
        path: '/options',
        component: () => import('@/views/Character/Options/OptionsView.vue'),
        beforeEnter: async (to, from, next) => {
            const store = useOptionsStore();

            await store.initFilter();
            await store.optionsQuery();

            next();
        },
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Traits/TraitDetail.vue'),
        //         beforeEnter: async (to, from, next) => {
        //             const store = useTraitsStore();
        //
        //             await store.traitInfoQuery(to.params.traitName);
        //
        //             next()
        //         },
        //     }
        // ]
    },
    {
        name: 'spells',
        path: '/spells',
        component: () => import('@/views/Spells/SpellsView.vue'),
        beforeEnter: async (to, from, next) => {
            const store = useSpellsStore();

            await store.initFilter();
            await store.spellsQuery();

            next()
        },
        children: [
            {
                name: 'spellDetail',
                path: ':spellName',
                component: () => import('@/views/Spells/SpellDetail.vue'),
                beforeEnter: async (to, from, next) => {
                    const store = useSpellsStore();

                    await store.spellInfoQuery(to.params.spellName);

                    next()
                },
            }
        ]
    },
];

const router = createRouter({
    history: createWebHistory('/'),
    routes,
});

router.beforeEach((to, from, next) => {
    next();
})

export default router;
