import { defineStore } from 'pinia';

// eslint-disable-next-line import/prefer-default-export
export const useHomeStore = defineStore('HomeStore', {
    state: () => ({
        menuItems: [
            {
                label: 'Классы',
                name: 'classes',
            }, {
                label: 'Расы',
                name: 'races',
            }, {
                label: 'Черты',
                name: 'traits',
            }, {
                label: 'Особенности классов',
                name: 'options',
            }, {
                label: 'Предыстории',
                name: 'backgrounds',
            }, {
                label: 'Заклинания',
                name: 'spells',
            }, {
                label: 'Оружие',
                name: 'weapons',
            }, {
                label: 'Доспехи',
                name: 'armors',
            }, {
                label: 'Снаряжение',
                name: 'items',
            }, {
                label: 'Бестиарий',
                name: 'creatures',
            }, {
                label: 'Магические предметы',
                name: 'magic-items',
            }, {
                label: 'Ширма',
                name: 'screens',
            },
        ],
        partners: [{
            name: '«Онлайн ширма»',
            url: '//www.youtube.com/channel/UCMfMw5ZX4_rZ4rv4TlY8j8g?partner=dnd5.club',
            // eslint-disable-next-line max-len
            tooltip: 'Канал расскжет вам о тонкостях и нюансах игры в онлайне. Поможет разобраться с виртуальными столами.<br/> Упростит подготовку к вашей игре. Автоматизирует максимум процессов игровой механики. <br />Всё, чтобы игра стала атмосфернее и интереснее.',
            img: {
                path: 'koroz.png',
                alt: 'Онлайн ширма'
            },
        }, {
            name: 'Empire Tavern',
            url: '//discord.gg/Vxu2Cu9mbM?partner=dnd5.club',
            // eslint-disable-next-line max-len
            tooltip: 'Сервер ставит перед собой цель помочь новичкам, интересующимся D&D в Discord сегменте, <br/>а также стремится стать надежной базой для Discord-комьюнити системы D&D.',
            img: {
                path: 'empire-tavern.png',
                alt: 'Empire Tavern'
            },
        }, {
            name: 'Студия «Ravenheart»',
            url: '//vk.com/ravenheart_studio?partner=dnd5.club',
            // eslint-disable-next-line max-len
            tooltip: 'Это клуб ролевых игр Великого Новгорода Студия «Ravenheart». <br />Если ты хочешь круто провести время, то ты пришел по адресу!',
            img: {
                path: 'ravenheart-studio.jpg',
                alt: 'Студия «Ravenheart»'
            },
        }, {
            name: 'ANY KEY',
            url: '//vk.com/anykeyspb?partner=dnd5.club',
            // eslint-disable-next-line max-len
            tooltip: 'Сообщество опытного мастера Andy по настольным и ролевым играм, <br />который хочет сделать ваш досуг максимально интересным и доступным.',
            img: {
                path: 'any-key.png',
                alt: 'ANY KEY'
            },
        }, {
            name: 'Random Rules',
            url: '//vk.com/rrules?partner=dnd5.club',
            // eslint-disable-next-line max-len
            tooltip: 'Random Rules это канал про настольные ролевые игры. <br />Любите проводить время с друзьями, играя в Dungeons & Dragons? Вам сюда!',
            img: {
                path: 'random-rules.jpg',
                alt: 'Random Rules'
            },
        }, {
            name: 'DiceHead | D&amp;D',
            url: '//vk.com/dicehead_dnd?partner=dnd5.club',
            // eslint-disable-next-line max-len
            tooltip: 'DiceHead собирает множество информации по Dungeons & Dragons, <br />выкладывает материалы для вдохновения и стремиться поддерживать сообщество НРИ в России.',
            img: {
                path: 'dicehead.jpg',
                alt: 'DiceHead | D&amp;D'
            },
        }, {
            name: 'Заметки упыря',
            url: '//vk.com/ngdnd?partner=dnd5.club',
            tooltip: 'Короткие авторские приключения для D&amp;D 5e и других систем.',
            img: {
                path: 'ngdnd.jpg',
                alt: 'Заметки упыря | Короткие приключения | D&amp;D'
            },
        }]
    }),

    getters: {
        getMenuItems: state => state.menuItems,
        getPartners: state => state.partners
    },

    actions: {
    }
});
