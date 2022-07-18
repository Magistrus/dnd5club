import { defineStore } from 'pinia';

// eslint-disable-next-line import/prefer-default-export
export const useNavStore = defineStore('NavStore', {
    state: () => ({
        items: [],
        opened: false
    }),

    getters: {
        getNavItems: state => state.items,
        getMenuState: state => state.opened
    },

    actions: {
        setNavItems() {
            this.items = [
                {
                    label: 'Персонаж',
                    icon: 'left-menu-character',
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
                            url: '/spells',
                            icon: 'left-menu-spells'
                        }
                    ]
                },
                {
                    label: 'Предметы',
                    icon: 'left-menu-inventory',
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
                    icon: 'workshop',
                    links: [
                        {
                            label: 'Бестиарий',
                            url: '/bestiary',
                            icon: 'left-menu-creatures'
                        },
                        {
                            label: 'Ширма',
                            url: '/screens',
                            icon: 'left-menu-screens'
                        }
                    ]
                },
                {
                    label: 'Инструменты',
                    icon: 'left-menu-tools',
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
                    icon: 'left-menu-wiki',
                    links: [
                        {
                            label: 'Боги',
                            url: '/gods',
                            icon: 'left-menu-gods'
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
                    icon: 'info',
                    links: [
                        {
                            label: 'Мы в Discord',
                            url: 'https://discord.gg/zqBnMJVf3z',
                            icon: 'menu-classes',
                            external: true
                        },
                        {
                            label: 'Мы в ВКонтакте',
                            url: 'https://vk.com/dnd5club',
                            icon: 'menu-races',
                            external: true
                        },
                        {
                            label: 'Мы на Boosty',
                            url: 'https://boosty.to/dnd5club',
                            icon: 'menu-races',
                            external: true
                        },
                        {
                            label: 'Наш бот для Telegram',
                            url: 'https://t.me/dnd5club_bot',
                            icon: 'menu-races',
                            external: true
                        },
                        {
                            label: 'Мастер на Boosty',
                            url: 'https://boosty.to/dnd5eclub',
                            icon: 'menu-races',
                            external: true
                        },
                        {
                            label: 'Старый сайт',
                            url: 'https://old.dnd5.club/',
                            icon: 'menu-races',
                            external: true
                        }
                    ]
                }
            ];
        },

        setMenuState(payload) {
            if (typeof payload !== 'boolean') {
                this.opened = false;

                return;
            }

            this.opened = payload;
        }
    }
});
