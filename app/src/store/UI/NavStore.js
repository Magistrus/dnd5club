import { defineStore } from 'pinia';

// eslint-disable-next-line import/prefer-default-export
export const useNavStore = defineStore('NavStore', {
    state: () => ({
        items: []
    }),

    getters: {
        getNavItems: state => state.items
    },

    actions: {
        setNavItems() {
            this.items = [
                {
                    label: 'Персонаж',
                    icon: 'menu-character',
                    links: [
                        {
                            label: 'Классы',
                            url: '/classes'
                        },
                        {
                            label: 'Расы',
                            url: '/races'
                        },
                        {
                            label: 'Черты',
                            url: '/traits'
                        },
                        {
                            label: 'Особенности классов',
                            url: '/options'
                        },
                        {
                            label: 'Предыстории',
                            url: '/backgrounds'
                        },
                        {
                            label: 'Заклинания',
                            url: '/spells'
                        }
                    ]
                },
                {
                    label: 'Предметы',
                    icon: 'menu-inventory',
                    links: [
                        {
                            label: 'Оружие',
                            url: '/weapons'
                        },
                        {
                            label: 'Доспехи',
                            url: '/armors'
                        },
                        {
                            label: 'Снаряжение',
                            url: '/items'
                        },
                        {
                            label: 'Драгоценности',
                            url: '/treasures'
                        },
                        {
                            label: 'Магические предметы',
                            url: '/items/magic'
                        }
                    ]
                },
                {
                    label: 'Мастерская',
                    icon: 'menu-workshop',
                    links: [
                        {
                            label: 'Бестиарий',
                            url: '/bestiary'
                        },
                        {
                            label: 'Ширма',
                            url: '/screens'
                        }
                    ]
                },
                {
                    label: 'Инструменты',
                    icon: 'menu-tools',
                    links: [
                        {
                            label: 'Торговец',
                            url: '/tools/trader'
                        },
                        {
                            label: 'Случайные столкновения',
                            url: '/tools/encounters'
                        },
                        {
                            label: 'Сокровищница',
                            url: '/tools/treasury'
                        },
                        {
                            label: 'Дикая магия',
                            url: '/tools/wildmagic'
                        },
                        {
                            label: 'Безумие',
                            url: '/tools/madness'
                        }
                    ]
                },
                {
                    label: 'База знаний',
                    icon: 'menu-wiki',
                    links: [
                        {
                            label: 'Боги',
                            url: '/gods'
                        },
                        {
                            label: 'Правила и термины',
                            url: '/rules'
                        },
                        {
                            label: 'Источники',
                            url: '/books'
                        }
                    ]
                },
                {
                    label: 'Информация',
                    icon: 'menu-information',
                    links: [
                        {
                            label: 'Мы в Discord',
                            url: 'https://discord.gg/zqBnMJVf3z',
                            external: true
                        },
                        {
                            label: 'Мы в ВКонтакте',
                            url: 'https://vk.com/dnd5club',
                            external: true
                        },
                        {
                            label: 'Мы на Boosty',
                            url: 'https://boosty.to/dnd5club',
                            external: true
                        },
                        {
                            label: 'Наш бот для Telegram',
                            url: 'https://t.me/dnd5club_bot',
                            external: true
                        },
                        {
                            label: 'Мастер на Boosty',
                            url: 'https://boosty.to/dnd5eclub',
                            external: true
                        },
                        {
                            label: 'Старый сайт',
                            url: 'https://old.dnd5.club/',
                            external: true
                        }
                    ]
                }
            ];
        }
    }
});
