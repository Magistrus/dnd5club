<template>
    <div class="custom-bookmarks">
        <div class="custom-bookmarks__header">
            <div class="custom-bookmarks__info">
                <span class="custom-bookmarks__info--title">Группы</span>
            </div>

            <form-button
                class="custom-bookmarks__new"
                type-link
                is-small
            >
                <div class="custom-bookmarks__new--icon">
                    <svg-icon icon-name="plus"/>
                </div>

                <span>Группа</span>
            </form-button>

            <label class="custom-bookmarks__search">
                <span class="custom-bookmarks__search--icon">
                    <svg-icon icon-name="search"/>
                </span>
            </label>
        </div>

        <div class="custom-bookmarks__body">
            <div
                v-for="(group, groupKey) in getBookmarks"
                :key="group.name + group.order + groupKey"
                class="custom-bookmarks__group"
            >
                <div class="custom-bookmarks__group_label">
                    <div class="custom-bookmarks__group_label">
                        {{ group.name }}
                    </div>
                </div>

                <div class="custom-bookmarks__links">
                    <div
                        v-for="link in group.childList"
                        :key="link.url + group.order"
                        class="custom-bookmarks__link"
                    >
                        <a
                            :href="link.url"
                            :target="isExternal(link.url) ? '_blank' : '_self'"
                            class="custom-bookmarks__link_label"
                        >{{ link.name }}</a>

                        <div
                            class="custom-bookmarks__link_icon only-hover is-right"
                            @click.left.exact.stop.prevent="removeBookmark(link.url)"
                        >
                            <svg-icon icon-name="close"/>
                        </div>
                    </div>
                </div>
            </div>

            <div
                v-if="!getBookmarks?.length"
                class="custom-bookmarks__info"
            >
                <span class="custom-bookmarks__info--desc">Здесь пока пусто</span>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapActions, mapState } from "pinia";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import SvgIcon from "@/components/UI/SvgIcon";
    import FormButton from "@/components/form/FormButton";

    export default {
        name: "CustomBookmarks",
        components: {
            SvgIcon,
            FormButton
        },
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
    .custom-bookmarks {
        background-color: var(--bg-sub-menu);

        &__header {
            padding: 16px;
            border-bottom: 1px solid var(--hover);
            display: flex;
            align-items: center;
        }

        &__body {
            padding: 16px 16px 8px;
            display: flex;
            flex-wrap: wrap;
            gap: 12px;

            @media (max-width: 550px) {
                padding: 16px 8px 0;
            }
        }

        &__info {
            display: flex;
            justify-content: center;
            align-items: center;

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

        &__new {
            margin-left: auto;
        }

        &__search {
            &--icon {
                width: 32px;
                height: 32px;
                padding: 4px;
                display: block;
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
                .custom-bookmarks {
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
