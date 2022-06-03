import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        name: 'classes',
        path: '/classes',
        component: () => import('@/views/Character/Classes/ClassesView'),
        children: [
            {
                name: 'classDetail',
                path: ':className/:classArchetype?',
                component: () => import('@/views/Character/Classes/ClassDetail'),
            }
        ]
    },
    {
        name: 'races',
        path: '/races',
        component: () => import('@/views/Character/Races/RacesView'),
        children: [
            {
                name: 'raceDetail',
                path: ':raceName/:subrace?',
                component: () => import('@/views/Character/Races/RaceDetail'),
            }
        ]
    },
    {
        name: 'traits',
        path: '/traits',
        component: () => import('@/views/Character/Traits/TraitsView'),
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Traits/TraitDetail'),
        //     }
        // ]
    },
    {
        name: 'backgrounds',
        path: '/backgrounds',
        component: () => import('@/views/Character/Backgrounds/BackgroundsView'),
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Backgrounds/BackgroundDetail'),
        //     }
        // ]
    },
    {
        name: 'options',
        path: '/options',
        component: () => import('@/views/Character/Options/OptionsView'),
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Options/OptionDetail'),
        //     }
        // ]
    },
    {
        name: 'spells',
        path: '/spells',
        component: () => import('@/views/Spells/SpellsView'),
        children: [
            {
                name: 'spellDetail',
                path: ':spellName',
                component: () => import('@/views/Spells/SpellDetail'),
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
