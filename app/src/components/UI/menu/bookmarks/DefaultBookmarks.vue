<template>
    <div class="default-bookmarks">
        <div class="default-bookmarks__header">
            <div class="default-bookmarks__info">
                <span class="default-bookmarks__info--title">Закладки</span>
            </div>
        </div>

        <div class="default-bookmarks__body">
            <div
                v-for="(group, groupKey) in getBookmarks"
                :key="group.name + group.order + groupKey"
                class="default-bookmarks__group"
            >
                <div class="default-bookmarks__group_label">
                    <div class="default-bookmarks__group_label">
                        {{ group.name }}
                    </div>
                </div>

                <div class="default-bookmarks__links">
                    <div
                        v-for="link in group.childList"
                        :key="link.url + group.order"
                        class="default-bookmarks__link"
                    >
                        <a
                            :href="link.url"
                            :target="isExternal(link.url) ? '_blank' : '_self'"
                            class="default-bookmarks__link_label"
                        >{{ link.name }}</a>

                        <div
                            class="default-bookmarks__link_icon only-hover is-right"
                            @click.left.exact.stop.prevent="removeBookmark(link.url)"
                        >
                            <svg-icon icon-name="close"/>
                        </div>
                    </div>
                </div>
            </div>

            <div
                v-if="!getBookmarks?.length"
                class="default-bookmarks__info"
            >
                <span class="default-bookmarks__info--desc">Здесь пока пусто</span>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapActions, mapState } from "pinia";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";

    export default {
        name: "DefaultBookmarks",
        computed: {
            ...mapState(useDefaultBookmarkStore, ['getBookmarks'])
        },
        methods: {
            ...mapActions(useDefaultBookmarkStore, ['removeBookmark']),

            isExternal(url) {
                return url.startsWith('http');
            }
        }
    };
</script>

<style lang="scss" scoped>
    .default-bookmarks {
        &__header {
            padding: 16px;
            border-bottom: 1px solid var(--hover);
            display: flex;
            align-items: center;
            position: sticky;
            top: 0;
            background-color: var(--bg-secondary);
            z-index: 1;
        }

        &__body {
            padding: 16px 0px 8px;
            display: flex;
            flex-wrap: wrap;
            gap: 12px;

            @media (max-width: 550px) {
                padding: 16px 0px 8px;
            }
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
                font-weight: 600;
            }

            &--desc,
            &--title {
                color: var(--text-b-color);
            }
        }

        &__links {
            padding: 8px 8px 0;
        }

        &__group {
            display: flex;
            flex-direction: column;
            width: 240px;

            @media (max-width: 550px) {
                width: 100%;
            }

            &_label {
                padding: 0 20px 0 8px;
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
                padding: 6px 8px;
                width: 100%;
                display: flex;
                border-radius: 6px;
            }

            &_icon {
                @include css_anim();

                width: 28px;
                height: 28px;
                padding: 4px;
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
                .default-bookmarks {
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
