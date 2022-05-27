<template>
    <div class="home">
        <div class="home__header">
            <div class="home__search">
                <label class="home__search_input">
                    <span class="home__search_btn--search">
                        <svg-icon icon-name="search"/>
                    </span>

                    <input
                        v-model="search"
                        type="text"
                        name="search"
                        autocomplete="off"
                        spellcheck="false"
                        placeholder="Поиск по сайту"
                    >
                </label>

                <button
                    v-if="!!search"
                    type="button"
                    class="home__search_btn--reset"
                    @click.left.exact.prevent="search = ''"
                >
                    <svg-icon icon-name="close"/>
                </button>
            </div>

            <a
                v-tooltip.bottom-end="{ content: 'Наши друзья магазин миниатюр Bad Mimic' }"
                target="_blank"
                href="https://badmimic.com/?partner=dnd5.club"
                class="home__store"
            >
                <svg-icon icon-name="store"/>

                <span class="home__store_label">Магазин миниатюр</span>
            </a>
        </div>

        <div class="home__links">
            <router-link
                v-for="(link, index) in getMenuItems"
                :key="index"
                :to="{name: link.name}"
                class="home__link"
            >
                <div class="home__link_icon">
                    <svg-icon :icon-name="`home-menu-${link.name}`"/>
                </div>

                <div class="home__link_name">
                    {{ link.label }}
                </div>
            </router-link>
        </div>

        <div class="home__telegram">
            <div class="home__telegram_icon">
                <svg-icon icon-name="telegram-bot"/>
            </div>

            <div class="home__telegram_body">
                <h4>DnD5Club Telegram Bot</h4>

                <p>
                    Мы сделали бота в Telegram, чтобы вы могли быстро находить нужные вам заклинания в компактном виде
                    или бросить кубы, если вы забыли их дома.
                </p>

                <div class="home__telegram_footer">
                    <a href="/telegram">Описание</a>

                    <a
                        href="//t.me/dnd5club_bot"
                        target="_blank"
                    >Попробовать</a>
                </div>
            </div>
        </div>

        <div class="home__info">
            <h3 class="home__info_title">
                Мы в интернете
            </h3>

            <p>
                Самые свежие новости и обсуждения по сайту у нас в сетях. Не проходите мимо и принимайте участие.
            </p>

            <p>
                <a
                    href="//vk.com/dnd5club"
                    class="become-button is-vk"
                    target="_blank"
                >
                    <svg-icon
                        icon-name="vk"
                        :fill-enable="true"
                        :stroke-enable="false"
                    />

                    <span>ВКонтакте</span>
                </a>

                <a
                    href="//discord.gg/zqBnMJVf3z"
                    class="become-button is-discord"
                    target="_blank"
                >
                    <svg-icon
                        icon-name="discord"
                        :fill-enable="true"
                        :stroke-enable="false"
                    />

                    <span>Discord</span>
                </a>
            </p>
        </div>

        <div class="home__info">
            <h3 class="home__info_title">
                Поддержать
            </h3>

            <p>
                У нас есть <a
                    href="//boosty.to/dnd5club"
                    target="_blank"
                > <img
                    src="/app/img/boosty.png"
                    alt="Мы на Boosty"
                    style="height: 12px;"
                > Boosty</a>, а так-же вы
                можете поддержать нашего <a
                    href="//boosty.to/dnd5eclub"
                    target="_blank"
                >Мастер Ломастер</a> он создает потрясающие карты.
            </p>

            <a
                href="//discord.gg/zqBnMJVf3z"
                class="become-button is-boosty"
                target="_blank"
            >
                <svg-icon
                    icon-name="boosty"
                    :fill-enable="true"
                    :stroke-enable="false"
                />

                <span>Поддержать на Boosty</span>
            </a>
        </div>

        <div class="home__info">
            <h3 class="home__info_title">
                Наши друзья:
            </h3>

            <p class="home__info_friends">
                <a
                    v-for="(partner, key) in getPartners"
                    :key="key"
                    v-tooltip="{content: partner.tooltip, html: true}"
                    :href="partner.url"
                    class="home__info_friend"
                    target="_blank"
                >
                    <img
                        :src="`/app/img/partners/${partner.img.path}`"
                        :alt="partner.img.alt"
                    >

                    <span>{{ partner.name }}</span>
                </a>
            </p>
        </div>

        <div class="home__info">
            <h3 class="home__info_title">
                Отказ от ответственности
            </h3>

            <p>
                Материалы на данном сайте приведены исключительно в справочных и ознакомительных целях и не заменяют
                необходимость покупки официальных материалов по игре.
            </p>

            <p>
                Весь графический материал и система Dungeons&amp;Dragons является собственностью корпорации Wizards of
                the Coast.
            </p>
        </div>
    </div>
</template>

<script>
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useHomeStore } from '@/store/HomeStore/HomeStore';
    import { mapState } from 'pinia/dist/pinia';

    export default {
        name: 'HomeView',
        components: { SvgIcon },
        data() {
            return {
                search: '',
            }
        },
        computed: {
            ...mapState(useHomeStore, ['getMenuItems', 'getPartners']),
        }
    }
</script>

<style lang="scss" scoped>
    .home {
        padding: 16px;
        width: 100%;
        height: 100%;
        overflow: hidden auto;
        z-index: 1;

        &__header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 100%;
        }

        &__search {
            flex: 1;
            border: 1px solid var(--border);
            border-radius: 50px;
            overflow: hidden;
            height: 48px;
            display: flex;
            align-items: center;
            background-color: var(--bg-secondary);

            @include media-min($md) {
                max-width: 40%;
            }

            &_btn {
                &--search,
                &--reset {
                    @include css_anim();

                    display: flex;
                    align-items: center;
                    justify-content: center;
                    height: 46px;
                    width: 46px;
                    padding: 0;
                    color: var(--text-color-title);
                    flex-shrink: 0;
                    background-color: transparent;
                }

                &--search {
                    svg {
                        width: 24px;
                        height: 24px;
                    }
                }

                &--reset {
                    @include media-min($md) {
                        &:hover {
                            color: var(--primary);
                        }
                    }

                    svg {
                        width: 24px;
                        height: 24px;
                    }
                }
            }

            &_input {
                flex: 1;
                height: 100%;
                display: flex;
                cursor: text;
                align-items: center;

                input {
                    width: 100%;
                    height: 100%;
                    border: 0;
                    background-color: transparent;
                    color: var(--text-color);
                    padding: 0;
                    font: {
                        weight: 300;
                        size: 16px;
                    };
                }
            }
        }

        &__store {
            @include css_anim();

            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            border: 1px solid var(--border);
            width: 48px;
            height: 48px;
            background-color: var(--hover);
            flex-shrink: 0;
            margin-left: 16px;
            color: var(--primary);

            @include media-min($md) {
                width: auto;
                border-radius: 32px;
                padding: 8px 16px;
            }

            svg {
                display: block;
                width: 28px;
                height: 28px;
            }

            &_label {
                @include css_anim();

                display: none;
                color: var(--text-color-title);
                font-weight: 300;
                font-size: 16px;
                margin-left: 8px;

                @include media-min($md) {
                    display: inline-block;
                }
            }

            @include media-min($md) {
                &:hover {
                    @include css_anim();

                    color: var(--text-btn-color);
                    background-color: var(--primary-hover);
                    border-color: var(--primary-hover);

                    .home {
                        &__store {
                            &_label {
                                color: var(--text-btn-color);
                            }
                        }
                    }
                }
            }
        }

        &__links {
            display: grid;
            grid-template-columns: 1fr 1fr;
            grid-gap: 16px;
            margin-top: 32px;

            @include media-min($sm) {
                grid-template-columns: repeat(3, 1fr);
            }

            @include media-min($md) {
                grid-template-columns: repeat(4, 1fr);
            }

            @include media-min($xl) {
                display: flex;
                flex-wrap: wrap;
                grid-template-columns: initial;
            }
        }

        &__link {
            @include css_anim();

            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 12px;
            border-radius: 16px;
            background-color: var(--bg-secondary);
            text-decoration: none;
            color: var(--text-color);

            @include media-min($xl) {
                min-width: calc((100% - 16px * 7) / 8);
                max-width: calc((100% - 16px * 3) / 4);
                flex: 1;
            }

            &_icon {
                @include css_anim();

                display: flex;
                justify-content: center;
                align-items: center;
                width: 40px;
                height: 40px;
                color: var(--primary);

                @include media-min($sm) {
                    width: 50px;
                    height: 50px;
                }
            }

            &_name {
                font: {
                    weight: 300;
                    family: "Open Sans", serif;
                    size: calc(var(--h4-font-size) - 6px);
                };
                line-height: 16px;
                text-align: center;
                margin-top: 8px;
            }

            @include media-min($md) {
                &:hover {
                    color: var(--text-color-active);
                    background-color: var(--bg-sub-menu);

                    .home {
                        &__link {
                            &_icon {
                            }
                        }
                    }
                }
            }
        }

        &__telegram {
            max-width: 640px;
            display: flex;
            padding: 16px;
            border-radius: 16px;
            background-color: var(--hover);
            margin-top: 16px;

            &_icon {
                width: 48px;
                height: 48px;
                flex-shrink: 0;
                color: var(--primary);
            }

            &_body {
                margin-left: 16px;

                p {
                    margin-top: 8px;
                }
            }

            &_footer {
                a {
                    font-weight: 600;

                    &:nth-child(n+2) {
                        margin-left: 8px;
                    }
                }
            }
        }

        &__info {
            margin-top: 48px;

            p {
                margin-top: 8px;
            }

            &_firends {
                display: flex;
                flex-wrap: wrap;
            }

            &_friend {
                @include css_anim();

                display: inline-flex;
                align-items: center;
                justify-content: flex-start;
                margin: 8px 8px 0 0;
                padding: 8px;
                border-radius: 8px;
                background-color: var(--bg-secondary);
                color: var(--text-color);
                font-weight: 400;
                text-decoration: none;

                img {
                    width: 20px;
                    height: 20px;
                    overflow: hidden;
                    display: inline-block;
                    border-radius: 50%;
                    border: 1px solid var(--border);
                    object-fit: cover;

                    & + span {
                        margin-left: 4px;
                    }
                }

                @include media-min($md) {
                    &:hover {
                        @include css_anim();

                        color: var(--text-btn-color);
                        background-color: var(--primary-active);
                    }
                }
            }
        }
    }
</style>
