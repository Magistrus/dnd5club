import { defineStore } from 'pinia';

// eslint-disable-next-line import/prefer-default-export
export const useNavStore = defineStore('NavStore', {
    state: () => ({
        items: [],
        opened: false,
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
                    links: [
                        {
                            label: 'Классы',
                            url: '/classes',
                            icon: 'menu-classes'
                        },
                        {
                            label: 'Расы',
                            url: '/races',
                            icon: 'menu-races'
                        },
                        {
                            label: 'Черты',
                            url: '/traits',
                            icon: 'menu-traits'
                        },
                        {
                            label: 'Особенности классов',
                            url: '/options',
                            icon: 'menu-traits'
                        },
                        {
                            label: 'Предыстории',
                            url: '/backgrounds',
                            icon: 'menu-traits'
                        },
                        {
                            label: 'Заклинания',
                            url: '/spells',
                            icon: 'menu-traits'
                        }
                    ]
                },
                {
                    label: 'Предметы',
                    links: [
                        {
                            label: 'Оружие',
                            url: '/weapons',
                            icon: 'menu-classes'
                        },
                        {
                            label: 'Доспехи',
                            url: '/armors',
                            icon: 'menu-races'
                        },
                        {
                            label: 'Снаряжение',
                            url: '/items',
                            icon: 'menu-traits'
                        },
                        {
                            label: 'Драгоценности',
                            url: '/treasures',
                            icon: 'menu-traits'
                        },
                        {
                            label: 'Магические предметы',
                            url: '/items/magic',
                            icon: 'menu-traits'
                        }
                    ]
                },
                {
                    label: 'Мастерская',
                    links: [
                        {
                            label: 'Бестиарий',
                            url: '/bestiary',
                            icon: 'menu-classes'
                        },
                        {
                            label: 'Ширма',
                            url: '/screens',
                            icon: 'menu-races'
                        }
                    ]
                },
                {
                    label: 'Инструменты',
                    links: [
                        {
                            label: 'Торговец',
                            url: '/tools/trader',
                            icon: 'menu-classes'
                        },
                        {
                            label: 'Случайные столкновения',
                            url: '/tools/encounters',
                            icon: 'menu-races'
                        },
                        {
                            label: 'Сокровищница',
                            url: '/tools/treasury',
                            icon: 'menu-races'
                        },
                        {
                            label: 'Дикая магия',
                            url: '/tools/wildmagic',
                            icon: 'menu-races'
                        },
                        {
                            label: 'Безумие',
                            url: '/tools/madness',
                            icon: 'menu-races'
                        }
                    ]
                },
                {
                    label: 'База знаний',
                    links: [
                        {
                            label: 'Боги',
                            url: '/gods',
                            icon: 'menu-classes'
                        },
                        {
                            label: 'Правила и термины',
                            url: '/rules',
                            icon: 'menu-races'
                        },
                        {
                            label: 'Источники',
                            url: '/books',
                            icon: 'menu-races'
                        }
                    ]
                },
                {
                    label: 'Информация',
                    links: [
                        {
                            label: 'Мы в Discord',
                            url: 'https://discord.gg/zqBnMJVf3z',
                            icon: 'menu-classes',
                            external: true,
                        },
                        {
                            label: 'Мы на ВКонтакте',
                            url: 'https://vk.com/dnd5club',
                            icon: 'menu-races',
                            external: true,
                        },
                        {
                            label: 'Мы на Boosty',
                            url: 'https://boosty.to/dnd5club',
                            icon: 'menu-races',
                            external: true,
                        },
                        {
                            label: 'Наш бот для Telegram',
                            url: 'https://t.me/dnd5club_bot',
                            icon: 'menu-races',
                            external: true,
                        },
                        {
                            label: 'Мастер на Boosty',
                            url: 'https://boosty.to/dnd5eclub',
                            icon: 'menu-races',
                            external: true,
                        },
                        {
                            label: 'Старый сайт',
                            url: 'https://old.dnd5.club/',
                            icon: 'menu-races',
                            external: true,
                        }
                    ]
                },
            ]
        },

        setMenuState(payload) {
            if (typeof payload !== 'boolean') {
                this.opened = false;

                return
            }

            this.opened = payload
        }
    }
})
