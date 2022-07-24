<template>
    <nav-popover v-model="opened">
        <template #trigger="{ setRef }">
            <div
                :ref="el => setRef(el)"
                class="navbar__btn"
                @click.left.exact.prevent="opened = !opened"
            >
                <svg-icon
                    :icon-name="getItems?.length ? 'bookmark-filled' : 'bookmark'"
                    :stroke-enable="false"
                    fill-enable
                />
            </div>
        </template>

        <template #default>
            <div class="bookmarks__header">
                <div class="bookmarks__info">
                    <span class="bookmarks__info--title">Закладки</span>
                </div>
            </div>

            <div class="bookmarks">
                <div
                    v-for="(group, groupKey) in getItems"
                    :key="group.label + groupKey"
                    class="bookmarks__group"
                >
                    <div class="bookmarks__group_label">
                        <div
                            v-if="isEdit"
                            class="bookmarks__group_icon is-left"
                        >
                            <svg-icon icon-name="sandwich"/>
                        </div>

                        <div class="bookmarks__group_label">
                            {{ group.label }}
                        </div>

                        <div
                            v-if="isEdit"
                            class="bookmarks__group_icon is-right"
                        >
                            <svg-icon icon-name="close"/>
                        </div>
                    </div>

                    <div class="bookmarks__links">
                        <div
                            v-for="link in group.links"
                            :key="link.url"
                            class="bookmarks__link"
                        >
                            <div
                                v-if="isEdit"
                                class="bookmarks__link_icon only-hover is-left"
                            >
                                <svg-icon icon-name="sandwich"/>
                            </div>

                            <a
                                :href="link.url"
                                :target="isExternal(link.url) ? '_blank' : '_self'"
                                class="bookmarks__link_label"
                            >{{ link.label }}</a>

                            <div
                                class="bookmarks__link_icon only-hover is-right"
                                @click.left.exact.stop.prevent="removeBookmark(link.url)"
                            >
                                <svg-icon icon-name="close"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div
                    v-if="!getItems?.length"
                    class="bookmarks__info"
                >
                    <span class="bookmarks__info--desc">Здесь пока пусто</span>
                </div>
            </div>
        </template>
    </nav-popover>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import NavPopover from "@/components/UI/menu/NavPopover";
    import { mapActions, mapState } from "pinia";
    import { useBookmarkStore } from "@/store/UI/BookmarkStore";

    export default {
        name: "NavBookmarks",
        components: {
            NavPopover,
            SvgIcon
        },
        data: () => ({
            opened: false,
            isEdit: false
        }),
        computed: {
            ...mapState(useBookmarkStore, ['getItems'])
        },
        async beforeMount() {
            await this.restoreItems();
        },
        methods: {
            ...mapActions(useBookmarkStore, [
                'setItems',
                'removeBookmark',
                'restoreItems'
            ]),

            isExternal(url) {
                return url.startsWith('http');
            }
        }
    };
</script>

<style lang="scss" scoped>
    .bookmarks {
        padding: 0;
        display: flex;
        flex-wrap: wrap;
        gap: 8px;

        &__header {
            padding: 0 16px 16px 16px;
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
                width: 200px;
                padding: 16px 16px;
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
                  stroke: var(--text-color);
                }
            }

            &:hover {
                .bookmarks {
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
