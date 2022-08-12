<template>
    <nav-popover
        :model-value="menu"
        is-menu
        is-left
        @close="menu = false"
    >
        <template #trigger="{ setRef, isActive }">
            <div
                :ref="el => setRef(el)"
                :class="{ 'is-active': isActive }"
                class="navbar__btn hamburger"
                @click.left.exact.prevent="menu = !menu"
            >
                <span class="line"/>
                <span class="line"/>
                <span class="line"/>
            </div>
        </template>

        <template #default>
            <div class="nav-menu">
                <div class="nav-menu__header">
                    <a
                        class="nav-menu__logo"
                        href="/"
                    >
                        <site-logo/>
                    </a>

                    <div class="nav-menu__info">
                        <span class="nav-menu__info--desc">Онлайн справочник по D&D 5e</span>

                        <span class="nav-menu__info--title">DnD5 Club</span>
                    </div>
                </div>

                <div class="nav-menu__body">
                    <div
                        v-for="(group, groupKey) in getNavItems"
                        :key="group.label + groupKey"
                        class="nav-menu__group"
                    >
                        <div class="nav-menu__group_label">
                            <div
                                v-if="group.icon"
                                class="nav-menu__group_icon"
                            >
                                <svg-icon :icon-name="group.icon"/>
                            </div>

                            <div class="nav-menu__group_label">
                                {{ group.label }}
                            </div>
                        </div>

                        <div class="nav-menu__links">
                            <div
                                v-for="link in group.links"
                                :key="link.url"
                                class="nav-menu__link"
                            >
                                <div
                                    class="nav-menu__link_icon only-hover"
                                    :class="{'is-active': isBookmarkSaved(link.url)}"
                                    @click.left.exact.stop.prevent="updateBookmark(
                                        link.url,
                                        link.label,
                                        'menu'
                                    )"
                                >
                                    <svg-icon
                                        :icon-name="isBookmarkSaved(link.url)
                                            ? 'bookmark-dot-filled'
                                            : 'bookmark-dot'
                                        "
                                        :stroke-enable="false"
                                        fill-enable
                                    />
                                </div>

                                <a
                                    :href="link.url"
                                    :target="link.external ? '_blank' : '_self'"
                                    class="nav-menu__link_label"
                                >{{ link.label }}</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </template>
    </nav-popover>
</template>

<script>
    import { mapActions, mapState } from "pinia";
    import { useNavStore } from "@/store/UI/NavStore";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import NavPopover from "@/components/UI/menu/NavPopover";
    import SvgIcon from "@/components/UI/SvgIcon";
    import SiteLogo from "@/components/UI/SiteLogo";

    export default {
        name: "NavMenu",
        components: {
            NavPopover,
            SvgIcon,
            SiteLogo
        },
        data: () => ({
            menu: false
        }),
        computed: {
            ...mapState(useNavStore, ['getNavItems']),
            ...mapState(useDefaultBookmarkStore, ['isBookmarkSaved'])
        },
        created() {
            this.setNavItems();
        },
        methods: {
            ...mapActions(useNavStore, ['setNavItems']),
            ...mapActions(useDefaultBookmarkStore, ['updateBookmark'])
        }
    };
</script>

<style lang="scss" scoped>
    .nav-menu {
        padding: 16px 16px 8px;

        @media (max-width: 550px) {
            padding: 16px 8px 0;
        }

        &__header {
            padding: 32px 16px 16px 16px;
            border-bottom: 1px solid var(--hover);
            display: flex;
            align-items: center;
        }

        &__logo {
            margin-right: 12px;
            width: 70px;
            height: 70px;
            flex-shrink: 0;
        }

        &__info {
            display: flex;
            flex-direction: column;
            justify-content: center;

            &--desc {
                font-size: var(--h5-font-size);
                margin-bottom: 4px;
            }

            &--title {
                font-size: var(--h3-font-size);
                font-weight: 600;
            }

            &--desc,
            &--title {
                color: var(--text-b-color);
            }
        }

        &__body {
            padding: 8px 8px 0 8px;
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
        }

        &__links {
            padding: 0 8px;
        }

        &__group {
            display: flex;
            flex-direction: column;
            width: 240px;
            margin: 8px 0;

            @media (max-width: 550px) {
                width: 100%;
            }

            &_label {
                padding: 8px 20px 4px 8px;
                display: flex;
                opacity: 0.8;
                color: var(--text-g-color);
                font-size: inherit;
                font-weight: normal;
                letter-spacing: 0.75px;
            }

            &_icon {
                width: 32px;
                height: 32px;
                padding: 4px;
                flex-shrink: 0;
            }
        }

        &__link {
            @include css_anim();

            display: flex;
            border-radius: 6px;

            &_label {
                @include css_anim();

                color: var(--text-color);
                font-weight: 400;
                padding: 8px;
                width: 100%;
                display: flex;
                border-radius: 6px;
            }

            &_icon {
                @include css_anim();

                width: 32px;
                height: 32px;
                padding: 8px;
                flex-shrink: 0;

                &.only-hover {
                    &:not(.is-active) {
                        opacity: 0;
                    }
                }

                svg {
                    stroke: var(--text-color) !important;
                }
            }

            &:hover {
                .nav-menu {
                    &__link {
                        &_icon {
                            &.only-hover {
                                opacity: 1;
                            }
                        }

                        &_label,
                        &_icon {
                            cursor: pointer;
                            color: var(--text-btn-color);
                        }

                        &_label {
                            background-color: var(--primary-hover);
                        }
                    }
                }
            }
        }
    }
</style>
