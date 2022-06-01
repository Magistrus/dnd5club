<template>
    <router-link
        v-slot="{href, navigate, isActive}"
        :to="{ path: classItem.url }"
        custom
        v-bind="$props"
    >
        <div
            ref="classItem"
            v-masonry-tile
            :class="getClassList(isActive)"
            class="class-item"
            v-bind="$attrs"
        >
            <div class="class-item__content">
                <div class="class-item__main">
                    <a
                        :href="href"
                        class="class-item__link"
                        @click.left.prevent.exact="navigate()"
                    >
                        <span class="class-item__icon">
                            <svg-icon :icon-name="classItem.icon"/>
                        </span>

                        <span class="class-item__body">
                            <span class="class-item__body_row">
                                <span class="class-item__name">
                                    <span class="class-item__name--rus">
                                        {{ classItem.name.rus }}
                                    </span>

                                    <span class="class-item__name--eng">
                                        [{{ classItem.name.eng }}]
                                    </span>
                                </span>

                                <span
                                    v-tooltip="{ content: classItem.source.name }"
                                    class="class-item__book"
                                >
                                    {{ classItem.source.shortName }}
                                </span>
                            </span>

                            <span class="class-item__body_row">
                                <span class="class-item__dice">
                                    {{ classItem.dice }}
                                </span>
                            </span>
                        </span>
                    </a>

                    <button
                        v-if="hasArchetypes"
                        v-tooltip.left="{ content: 'Архетипы' }"
                        :class="{ 'is-active': submenu.show }"
                        class="class-item__toggle"
                        type="button"
                        @click.left.exact.prevent="toggleArch"
                    >
                        <svg-icon icon-name="expand-down"/>
                    </button>
                </div>

                <div
                    v-if="hasArchetypes"
                    :class="{ 'is-active': isOpenedArchetypes }"
                    class="class-item__arch-list"
                >
                    <div
                        v-for="(col, colKey) in classItem.archetypes"
                        :key="colKey"
                        class="class-item__arch-list_col"
                    >
                        <div
                            v-for="(group, groupKey) in col"
                            :key="groupKey"
                            class="class-item__arch-type"
                        >
                            <div class="class-item__arch-type_name">
                                {{ group.name }}
                            </div>

                            <div class="class-item__arch-type_items">
                                <router-link
                                    v-for="(arch, archKey) in group.list"
                                    :key="archKey"
                                    :to="{ path: arch.url }"
                                    class="class-item__arch-item"
                                >
                                    <span class="class-item__arch-item_name">{{ arch.name.rus }}</span>

                                    <span class="class-item__arch-item_book">
                                        <span v-tooltip="{ content: arch.source.name }">
                                            {{ arch.source.shortName }}
                                        </span>

                                        /

                                        <span>{{ arch.name.eng }}</span>
                                    </span>
                                </router-link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { useResizeObserver } from '@vueuse/core/index';
    import SvgIcon from '@/components/UI/SvgIcon';

    export default {
        name: 'ClassItem',
        components: { SvgIcon },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            classItem: {
                type: Object,
                default: () => null,
                required: true
            },
        },
        data() {
            return {
                submenu: {
                    show: false
                },
            }
        },
        computed: {
            isOpenedArchetypes() {
                if (this.$route.params?.className === this.classItem.url) {
                    return true
                }

                return this.submenu.show
            },

            hasArchetypes() {
                return !!this.classItem?.archetypes?.length
            }
        },
        watch: {
            isOpenedArchetypes() {
                this.updateGrid();
            },
        },
        mounted() {
            this.$nextTick(() => {
                useResizeObserver(this.$refs.classItem, this.updateGrid);
            });
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive
                        || this.$route.fullPath.match(new RegExp(`^${ this.classItem?.url }`)),
                    'is-class-selected': this.$route.name === 'classDetail',
                    'is-green': this.classItem?.source?.homebrew
                }
            },

            toggleArch() {
                this.submenu.show = !this.submenu.show;
            },

            updateGrid() {
                this.$nextTick(() => this.$redrawVueMasonry('classes-items'))
            },
        }
    }
</script>

<style lang="scss" scoped>
    .class-item {
        width: 100%;
        margin-bottom: 16px;

        @include media-min($md) {
            width: calc(50% - 8px);
        }

        @include media-min($xxl) {
            width: calc(100% / 3 - 16px * 2 / 3);
        }

        &.is-class-selected {
            width: 100%;
        }

        &.is-green {
            .class-item {
                &__content {
                    background-color: var(--bg-homebrew-gradient-left);
                }
            }
        }

        &__content {
            background-color: var(--bg-table-list);
            border: 1px solid var(--bg-secondary);
            border-radius: 16px;
            overflow: hidden;
            width: 100%;
        }

        &__main {
            width: 100%;
            display: flex;
        }

        &__link {
            width: 100%;
            display: flex;
        }

        &__icon {
            padding: 10px 0 10px 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;

            ::v-deep(> svg) {
                width: 42px;
                height: 42px;
                color: var(--primary);
            }

            & + .class-item {
                &__body {
                    padding-left: 16px;
                }
            }
        }

        &__body {
            padding: 10px 12px 10px 16px;
            width: 100%;
            display: flex;
            flex-direction: column;
            justify-content: center;

            &_row {
                display: flex;
            }
        }

        &__name {
            padding-right: 8px;

            &--rus,
            &--eng {
                display: inline;
                font-size: var(--h5-font-size);;
                font-weight: 500;
                color: var(--text-color-title);
                line-height: normal;
            }

            &--eng {
                color: var(--text-g-color);
            }
        }

        &__book {
            color: var(--text-g-color);
            margin-left: auto;
            line-height: normal;
            font-size: var(--main-font-size);
        }

        &__dice {
            color: var(--text-g-color);
            line-height: normal;
            font-size: calc(var(--h5-font-size) - 1px);
        }

        &__toggle {
            color: var(--primary);
            width: 32px;
            padding: 0;
            flex-shrink: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: var(--bg-sub-menu);

            @include media-min($md) {
                &:hover {
                    background-color: var(--hover);
                }
            }

            svg {
                @include css_anim();

                width: 24px;
                height: 24px;
            }

            &.is-active {
                svg {
                    transform: rotate(-180deg);
                }
            }
        }

        &__arch {
            &-list {
                padding: 0 16px 16px 62px;
                display: none;
                flex-direction: column;
                gap: 16px;

                &_col {
                    flex: 1;
                }

                &.is-active {
                    display: flex;
                }
            }

            &-type {
                &:nth-child(n+2) {
                    margin-top: 16px;
                }

                &_items {
                    display: flex;
                    flex-direction: column;
                    align-items: flex-start;

                    @include media-min($sm) {
                        min-width: 216px;
                        max-width: 100%;
                    }
                }

                &_name {
                    font: {
                        size: calc(var(--h5-font-size) + 2px);
                        family: "Lora", serif;
                        weight: 300;
                    };
                    color: var(--text-color-title);
                    padding: 0 8px;
                }
            }

            &-item {
                display: inline-block;
                padding: 4px 8px;
                border-radius: 8px;
                color: var(--text-color);
                font-size: var(--main-font-size);
                margin-top: 4px;

                &_book {
                    margin-left: 4px;
                    color: var(--text-g-color);
                    font-size: calc(var(--main-font-size) - 2px);
                }

                @include media-min($md) {
                    &:hover {
                        background-color: var(--hover);
                    }
                }

                &.router-link-active {
                    background-color: var(--primary-active);

                    .class-item__arch-item {
                        &_name,
                        &_book {
                            color: var(--text-btn-color);
                        }
                    }
                }
            }
        }

        @include media-min($md) {
            &:hover {
                .class-item {
                    &__content {
                        background-color: var(--bg-sub-menu)
                    }
                }
            }
        }

        &.router-link-active {
            .class-item {
                &__content {
                    border-color: var(--primary);
                }

                &__toggle {
                    display: none;
                }

                &__arch-list {
                    display: flex;
                }
            }
        }
    }
</style>
