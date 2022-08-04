import {
    createRouter, createWebHistory
} from 'vue-router';
import { useUserStore } from '@/store/UI/UserStore';
import { useDefaultBookmarkStore } from '@/store/UI/bookmarks/DefaultBookmarkStore';

const routes = [
    {
        name: 'classes',
        path: '/classes',
        meta: {
            bookmark: 'classes'
        },
        component: () => import('@/views/Character/Classes/ClassesView'),
        children: [
            {
                name: 'classDetail',
                path: ':className/:classArchetype?',
                component: () => import('@/views/Character/Classes/ClassDetail')
            }
        ]
    },
    {
        name: 'races',
        path: '/races',
        meta: {
            bookmark: 'races'
        },
        component: () => import('@/views/Character/Races/RacesView'),
        children: [
            {
                name: 'raceDetail',
                path: ':raceName/:subrace?',
                component: () => import('@/views/Character/Races/RaceDetail')
            }
        ]
    },
    {
        name: 'traits',
        path: '/traits',
        meta: {
            bookmark: 'traits'
        },
        component: () => import('@/views/Character/Traits/TraitsView'),
        children: [
            {
                name: 'traitDetail',
                path: ':traitName',
                component: () => import('@/views/Character/Traits/TraitDetail')
            }
        ]
    },
    {
        name: 'backgrounds',
        path: '/backgrounds',
        meta: {
            bookmark: 'backgrounds'
        },
        component: () => import('@/views/Character/Backgrounds/BackgroundsView'),
        children: [
            {
                name: 'backgroundDetail',
                path: ':backgroundName',
                component: () => import('@/views/Character/Backgrounds/BackgroundDetail')
            }
        ]
    },
    {
        name: 'options',
        path: '/options',
        meta: {
            bookmark: 'options'
        },
        component: () => import('@/views/Character/Options/OptionsView'),
        children: [
            {
                name: 'optionDetail',
                path: ':optionName',
                component: () => import('@/views/Character/Options/OptionDetail')
            }
        ]
    },
    {
        name: 'spells',
        path: '/spells',
        meta: {
            bookmark: 'spells'
        },
        component: () => import('@/views/Spells/SpellsView'),
        children: [
            {
                name: 'spellDetail',
                path: ':spellName',
                component: () => import('@/views/Spells/SpellDetail')
            }
        ]
    },
    {
        name: 'weapons',
        path: '/weapons',
        meta: {
            bookmark: 'weapons'
        },
        component: () => import('@/views/Inventory/Weapons/WeaponsView'),
        children: [
            {
                name: 'weaponDetail',
                path: ':weaponName',
                component: () => import('@/views/Inventory/Weapons/WeaponDetail')
            }
        ]
    },
    {
        name: 'armors',
        path: '/armors',
        meta: {
            bookmark: 'armors'
        },
        component: () => import('@/views/Inventory/Armors/ArmorsView'),
        children: [
            {
                name: 'armorDetail',
                path: ':armorName',
                component: () => import('@/views/Inventory/Armors/ArmorDetail')
            }
        ]
    },
    {
        name: 'magicItems',
        path: '/items/magic',
        meta: {
            bookmark: 'magicItems'
        },
        component: () => import('@/views/Treasures/MagicItems/MagicItemsView'),
        children: [
            {
                name: 'magicItemDetail',
                path: ':magicItemName',
                component: () => import('@/views/Treasures/MagicItems/MagicItemDetail')
            }
        ]
    },
    {
        name: 'items',
        path: '/items',
        meta: {
            bookmark: 'items'
        },
        component: () => import('@/views/Inventory/Items/ItemsView'),
        children: [
            {
                name: 'itemDetail',
                path: ':itemName',
                component: () => import('@/views/Inventory/Items/ItemDetail')
            }
        ]
    },
    {
        name: 'bestiary',
        path: '/bestiary',
        meta: {
            bookmark: 'bestiary'
        },
        component: () => import('@/views/Bestiary/BestiaryView'),
        children: [
            {
                name: 'creatureDetail',
                path: ':creatureName',
                component: () => import('@/views/Bestiary/CreatureDetail')
            }
        ]
    },
    {
        name: 'gods',
        path: '/gods',
        meta: {
            bookmark: 'gods'
        },
        component: () => import('@/views/Wiki/Gods/GodsView'),
        children: [
            {
                name: 'godDetail',
                path: ':godName',
                component: () => import('@/views/Wiki/Gods/GodDetail')
            }
        ]
    },
    {
        name: 'rules',
        path: '/rules',
        meta: {
            bookmark: 'rules'
        },
        component: () => import('@/views/Wiki/Rules/RulesView'),
        children: [
            {
                name: 'ruleDetail',
                path: ':ruleName',
                component: () => import('@/views/Wiki/Rules/RuleDetail')
            }
        ]
    },
    {
        name: 'books',
        path: '/books',
        meta: {
            bookmark: 'books'
        },
        component: () => import('@/views/Wiki/Books/BooksView'),
        children: [
            {
                name: 'bookDetail',
                path: ':bookName',
                component: () => import('@/views/Wiki/Books/BookDetail')
            }
        ]
    },
    {
        name: 'screens',
        path: '/screens',
        meta: {
            bookmark: 'screens'
        },
        component: () => import('@/views/Screens/ScreensView'),
        children: [
            {
                name: 'screenDetail',
                path: ':screenName',
                component: () => import('@/views/Screens/ScreenDetail')
            }
        ]
    },
    {
        name: 'treasures',
        path: '/treasures',
        component: () => import('@/views/Treasures/Treasures/TreasuresView')
    },
    {
        name: 'trader',
        path: '/tools/trader',
        component: () => import('@/views/Tools/TraderView')
    },
    {
        name: 'treasury',
        path: '/tools/treasury',
        component: () => import('@/views/Tools/TreasuryView')
    },
    {
        name: 'wild-magic',
        path: '/tools/wildmagic',
        component: () => import('@/views/Tools/WildMagicView')
    },
    {
        name: 'madness',
        path: '/tools/madness',
        component: () => import('@/views/Tools/MadnessView')
    },
    {
        name: 'encounters',
        path: '/tools/encounters',
        component: () => import('@/views/Tools/EncountersView')
    }
];
const router = createRouter({
    history: createWebHistory('/'),
    routes
});

router.afterEach(async to => {
    const userStore = useUserStore();

    if (userStore.isAuthorized) {
        await userStore.updateUserFromSession();
    }

    const bookmarksStore = useDefaultBookmarkStore();

    bookmarksStore.setSection(to.meta?.bookmark || 'none');
});

export default router;
