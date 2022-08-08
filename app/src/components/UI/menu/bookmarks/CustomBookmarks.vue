<template>
    <div class="custom-bookmarks">
        <div class="custom-bookmarks__header">
            <div class="custom-bookmarks__info">
                <span class="custom-bookmarks__info--title">Группы</span>
            </div>

            <form-button
                class="custom-bookmarks__new"
                type-link
            >
                <div class="custom-bookmarks__new--icon">
                    <svg-icon icon-name="plus"/>
                </div>

                <!-- <span>Добавить</span> -->
            </form-button>

            <!-- <label class="custom-bookmarks__search">
                <span class="custom-bookmarks__search--icon">
                    <svg-icon icon-name="search"/>
                </span>
            </label> -->
        </div>

        <div class="custom-bookmarks__body">
            <div
                v-for="(group, groupKey) in groupBookmarks"
                :key="group.name + group.order + groupKey"
                class="custom-bookmarks__group"
            >
                <div class="custom-bookmarks__group_label">
                    {{ group.name }}
                </div>

                <div class="custom-bookmarks__group_body">
                    <div
                        v-for="(category, catKey) in group.children"
                        :key="category.name + category.order + catKey"
                        class="custom-bookmarks__cat"
                    >
                        <div class="custom-bookmarks__cat_label">
                            {{ category.name }}
                        </div>

                        <div class="custom-bookmarks__cat_body">
                            <div
                                v-for="(bookmark, bookmarkKey) in category.children"
                                :key="bookmark.url + bookmark.order + bookmarkKey"
                                class="custom-bookmarks__bookmark"
                            >
                                <a
                                    :href="bookmark.url"
                                    class="custom-bookmarks__bookmark_label"
                                >{{ bookmark.name }}</a>

                                <div
                                    class="custom-bookmarks__bookmark_icon only-hover is-right"
                                >
                                    <svg-icon icon-name="close"/>
                                </div>
                            </div>
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
    import SvgIcon from "@/components/UI/SvgIcon";
    import FormButton from "@/components/form/FormButton";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import sortBy from "lodash/sortBy";

    export default {
        name: "CustomBookmarks",
        components: {
            SvgIcon,
            FormButton
        },
        computed: {
            ...mapState(useCustomBookmarkStore, ['getBookmarks']),

            groupBookmarks() {
                const list = this.getBookmarks;
                const groups = list.filter(group => !group.parentUUID);

                return sortBy(
                    groups.map(group => ({
                        ...group,
                        children: sortBy(
                            list
                                .filter(category => category.parentUUID && category.parentUUID === group.uuid)
                                .map(category => ({
                                    ...category,
                                    children: sortBy(
                                        list.filter(bookmark => (
                                            bookmark.parentUUID
                                            && bookmark.parentUUID === category.uuid
                                        )),
                                        [o => o.order]
                                    )
                                })),
                            [o => o.order]
                        )
                    })),
                    [o => o.order]
                );
            }
        },
        mounted() {
            this.generateBookmarks();
        },
        methods: {
            ...mapActions(useCustomBookmarkStore, ['generateBookmarks'])
        }
    };
</script>

<style lang="scss" scoped>
    .custom-bookmarks {
        background-color: var(--bg-sub-menu);
        width: 260px;

        &__header {
            padding: 8px 16px;
            border-bottom: 1px solid var(--hover);
            display: flex;
            align-items: center;
            position: sticky;
            top: 0;
            background-color: inherit;
        }

        &__body {
            padding: 16px 8px 8px;
            display: flex;
            flex-wrap: wrap;
            gap: 12px;

            @media (max-width: 550px) {
                padding: 16px 8px 8px;
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
            height: 32px;
            width: 32px;

            svg {
                color: var(--text-color);
            }

            &:hover {
                background-color: var(--hover) !important;
            }
        }

        // &__search {
        //     &--icon {
        //         width: 32px;
        //         height: 32px;
        //         padding: 4px;
        //         display: block;
        //     }
        // }

        &__group {
            display: flex;
            flex-direction: column;
            width: 100%;

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

        &__cat {
            margin-bottom: 6px;

            &_label {
                padding: 8px 8px 0px 8px;
                text-transform: uppercase;
                font-size: calc(var(--main-font-size) - 4px);
                color: var(--text-color-title);
            }
        }

        &__bookmark {
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
                .custom-bookmarks {
                    &__bookmark {
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
