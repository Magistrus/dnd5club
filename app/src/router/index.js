import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        name: 'classes',
        path: '/classes',
        component: () => import('@/views/Character/Classes/ClassesView.vue'),
        children: [
            {
                name: 'classDetail',
                path: ':className/:classArchetype?',
                component: () => import('@/views/Character/Classes/ClassDetail.vue'),
            }
        ]
    },
    {
        name: 'races',
        path: '/races',
        component: () => import('@/views/Character/Races/RacesView.vue'),
        children: [
            {
                name: 'raceDetail',
                path: ':raceName/:subrace?',
                component: () => import('@/views/Character/Races/RaceDetail.vue'),
            }
        ]
    },
    {
        name: 'traits',
        path: '/traits',
        component: () => import('@/views/Character/Traits/TraitsView.vue'),
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Traits/TraitDetail.vue'),
        //     }
        // ]
    },
    {
        name: 'backgrounds',
        path: '/backgrounds',
        component: () => import('@/views/Character/Backgrounds/BackgroundsView.vue'),
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Traits/TraitDetail.vue'),
        //     }
        // ]
    },
    {
        name: 'options',
        path: '/options',
        component: () => import('@/views/Character/Options/OptionsView.vue'),
        // children: [
        //     {
        //         name: 'traitDetail',
        //         path: ':traitName',
        //         component: () => import('@/views/Character/Traits/TraitDetail.vue'),
        //     }
        // ]
    },
    {
        name: 'spells',
        path: '/spells',
        component: () => import('@/views/Spells/SpellsView.vue'),
        children: [
            {
                name: 'spellDetail',
                path: ':spellName',
                component: () => import('@/views/Spells/SpellDetail.vue'),
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
