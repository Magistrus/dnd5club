<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: book.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="bookItem"
            :class="getClassList(isActive)"
            :href="href"
            class="book-link"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="book-link__body">
                <div class="book-link__name">
                    <div class="book-link__name--rus">
                        {{ book.name.rus }}
                    </div>

                    <div class="book-link__name--eng">
                        [{{ book.name.eng }}]
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useBooksStore } from "@/store/Wiki/BooksStore";

    export default {
        name: 'BookLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            book: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            booksStore: useBooksStore(),
            modal: {
                show: false,
                data: undefined
            }
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-book-selected': this.$route.name === 'bookDetail',
                    'is-green': this.book?.homebrew
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.booksStore.bookInfoQuery(this.book.url)
                    .then(spell => {
                        this.modal = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .book-link {
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--bg-table-list);
        width: 100%;
        margin-bottom: 12px;
        display: block;

        &.is-green {
            .book-link {
                &__body {
                    background-color: var(--bg-homebrew-gradient-left);
                }
            }
        }

        &__body {
            display: flex;
            flex-direction: row;
            align-books: center;
            padding: 8px 10px;
        }

        &__name {
            display: block;
            font-size: var(--main-font-size);
            font-weight: 500;

            &--rus,
            &--eng {
                display: inline;
                line-height: normal;
            }

            &--rus {
                color: var(--text-color-title);
            }

            &--eng {
                color: var(--text-g-color);
            }
        }

        &:hover {
            .book-link {
                &__body {
                    background-color: var(--hover);
                }
            }
        }

        &.router-link-active {
            width: 100%;

            .book-link {
                &__body {
                    background-color: var(--primary-active);
                }

                &__name {
                    &--rus,
                    &--eng {
                        color: var(--text-btn-color);
                    }
                }
            }
        }
    }
</style>
