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
        children: [
            {
                name: 'traitDetail',
                path: ':traitName',
                component: () => import('@/views/Character/Traits/TraitDetail'),
            }
        ]
    },
    {
        name: 'backgrounds',
        path: '/backgrounds',
        component: () => import('@/views/Character/Backgrounds/BackgroundsView'),
        children: [
            {
                name: 'backgroundDetail',
                path: ':backgroundName',
                component: () => import('@/views/Character/Backgrounds/BackgroundDetail'),
            }
        ]
    },
    {
        name: 'options',
        path: '/options',
        component: () => import('@/views/Character/Options/OptionsView'),
        children: [
            {
                name: 'optionDetail',
                path: ':optionName',
                component: () => import('@/views/Character/Options/OptionDetail'),
            }
        ]
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
    {
        name: 'weapons',
        path: '/weapons',
        component: () => import('@/views/Inventory/Weapons/WeaponsView'),
        children: [
            {
                name: 'weaponDetail',
                path: ':weaponName',
                component: () => import('@/views/Inventory/Weapons/WeaponDetail'),
            }
        ]
    },
    {
        name: 'armors',
        path: '/armors',
        component: () => import('@/views/Inventory/Armors/ArmorsView'),
        children: [
            {
                name: 'armorDetail',
                path: ':armorName',
                component: () => import('@/views/Inventory/Armors/ArmorDetail'),
            }
        ]
    },
    {
        name: 'magicItems',
        path: '/items/magic',
        component: () => import('@/views/Treasures/MagicItems/MagicItemsView'),
        children: [
            {
                name: 'magicItemDetail',
                path: ':magicItemName',
                component: () => import('@/views/Treasures/MagicItems/MagicItemDetail'),
            }
        ]
    },
    {
        name: 'items',
        path: '/items',
        component: () => import('@/views/Inventory/Items/ItemsView'),
        children: [
            {
                name: 'itemDetail',
                path: ':itemName',
                component: () => import('@/views/Inventory/Items/ItemDetail'),
            }
        ]
    },
    {
        name: 'bestiary',
        path: '/bestiary',
        component: () => import('@/views/Bestiary/BestiaryView'),
        children: [
            {
                name: 'creatureDetail',
                path: ':creatureName',
                component: () => import('@/views/Bestiary/CreatureDetail'),
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