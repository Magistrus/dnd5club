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
                props: {
                    weapon: {
                        name: {
                            rus: 'Боевая коса',
                            eng: 'Battle scythe',
                        },
                        type: 'Простое рукопашное',
                        source: {
                            homebrew: true,
                            name: 'EEW',
                            shortName: 'Расширенное и экзотическое оружие'
                        },
                        price: '1 зм',
                        damage: {
                            dice: '1к8',
                            type: 'рубящий'
                        },
                        weight: '6.0',
                        properties: [
                            {
                                url: '/screens/Equipment#Versatile',
                                name: 'Универсальное',
                                twoHandDice: '1к10',
                                description: 'Описание для универсального оружия'
                            },
                            {
                                url: '/screens/Equipment#Reach',
                                name: 'Досягаемость',
                                distance: '10 фт.',
                                description: 'Описание для досягаемого оружия'
                            },
                        ],
                        description: '<p>Боевая коса — это древковое режущее оружие пехоты, которое представляло'
                            + ' собой древко или шест, с насаженной на него хозяйственной косой, шинковочным ножом или'
                            + ' специально изготовленным двулезвийным клинком, чаще дугообразной формы.</p>',
                        special: '<span>Когда Вы атакуете не ожидающего нападения гуманоида этим оружием и'
                            + ' наносите урон, вы можете попытаться ударом лишить цель сознания. После того как атака'
                            + ' попала, вы можете выбрать кинуть <dice-roller :formula="\'4к4\'">4к4</dice-roller>.'
                            + ' Если количество хитов у цели меньше или равно результату'
                            + ' броска, цель падает без сознания. Если у цели остается больше хитов, чем результат,'
                            + ' цель остается в сознании, и бросок не имеет никакого эффекта.</span>'
                    }
                }
            }
        ]
    }
];

const router = createRouter({
    history: createWebHistory('/'),
    routes,
});

router.beforeEach((to, from, next) => {
    next();
})

export default router;
