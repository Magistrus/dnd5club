<template>
    <router-link
        v-slot="{href, navigate, isActive}"
        :to="{ path: raceItem.url }"
        custom
        v-bind="$props"
    >
        <div
            ref="raceItem"
            v-masonry-tile
            :class="getParentClasses(isActive)"
            class="link-item-expand"
            v-bind="$attrs"
        >
            <div class="link-item-expand__content">
                <img
                    :src="`/assets/img/races/${raceItem.icon}.webp`"
                    alt="img-bg"
                    class="link-item-expand__content__img-bg"
                >

                <div class="link-item-expand__content__gradient"/>

                <div class="link-item-expand__main">
                    <a
                        :href="href"
                        class="link-item-expand__link"
                        @click.left.prevent.exact="selectRace(navigate)"
                    >

                        <span class="link-item-expand__body">
                            <span class="link-item-expand__body_row">
                                <span class="link-item-expand__name">
                                    <span class="link-item-expand__name--rus">
                                        {{ raceItem.name.rus }}
                                    </span>

                                    <span class="link-item-expand__name--eng">
                                        {{ raceItem.name.eng }}
                                    </span>
                                </span>
                            </span>

                            <span class="link-item-expand__body_row">
                                <span class="link-item-expand__abilities">
                                    {{ abilities }}
                                </span>

                                <span
                                    v-tooltip="{ content: raceItem.source.name }"
                                    class="link-item-expand__book"
                                >
                                    {{ raceItem.source.shortName }}
                                </span>
                            </span>
                        </span>
                    </a>

                    <button
                        v-if="hasSubraces"
                        v-tooltip.left="{ content: 'Разновидности' }"
                        :class="{ 'is-active': submenu.show }"
                        class="link-item-expand__toggle"
                        type="button"
                        @click.left.exact.prevent="toggleSubrace"
                    >
                        <svg-icon :icon-name="submenu.show ? 'minus' : 'plus'"/>
                    </button>
                </div>

                <div
                    v-if="hasSubraces"
                    :class="{ 'is-active': submenu.show }"
                    class="link-item-expand__arch-list"
                >
                    <div
                        v-for="(col, colKey) in raceItem.subraces"
                        :key="colKey"
                        class="link-item-expand__arch-list_col"
                    >
                        <div
                            v-for="(group, groupKey) in col"
                            :key="groupKey"
                            class="link-item-expand__arch-type"
                        >
                            <div class="link-item-expand__arch-type_name">
                                {{ group.name }}
                            </div>

                            <div class="link-item-expand__arch-type_items">
                                <router-link
                                    v-for="(subrace, subraceKey) in group.list"
                                    :key="subraceKey"
                                    :to="{ path: subrace.url }"
                                    class="link-item-expand__arch-item"
                                >
                                    <span class="link-item-expand__arch-item_name">{{ subrace.name.rus }}</span>

                                    <span class="link-item-expand__arch-item_book">
                                        <span v-tooltip="{ content: subrace.source.name }">
                                            {{ subrace.source.shortName }}
                                        </span>

                                        /

                                        <span>{{ subrace.name.eng }}</span>
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
        name: 'RaceLink',
        components: { SvgIcon },
        inheritAttrs: false,
        props: {
            raceItem: {
                type: Object,
                default: () => null,
                required: true
            },
            ...RouterLink.props
        },
        data() {
            return {
                submenu: {
                    show: false
                },
            }
        },
        computed: {
            hasSubraces() {
                return !!this.raceItem?.subraces?.length
            },

            abilities() {
                if (!this.raceItem.abilities?.length) {
                    return ''
                }

                const abilities = [];

                for (const ability of this.raceItem.abilities) {
                    abilities.push(
                        ability.value
                            ? `${ ability.shortName } ${ ability.value > 0 ? `+${ ability.value }` : ability.value }`
                            : ability.name
                    )
                }

                return abilities.join(', ')
            },
        },
        watch: {
            submenu: {
                deep: true,
                handler() {
                    this.updateGrid();
                }
            },
        },
        mounted() {
            this.$nextTick(() => {
                useResizeObserver(this.$refs.raceItem, this.updateGrid);
            });
        },
        methods: {
            getParentClasses(isActive) {
                return {
                    'router-link-active': isActive || this.$route.path.match(new RegExp(`^${ this.raceItem.url }`)),
                    'is-selected': this.$route.name === 'raceDetail',
                    'is-green': this.raceItem.type?.name.toLowerCase() === 'homebrew'
                }
            },

            toggleSubrace() {
                this.submenu.show = !this.submenu.show;
            },

            updateGrid() {
                this.$nextTick(() => this.$redrawVueMasonry('race-items'))
            },

            selectRace(callback) {
                this.submenu.show = true;

                callback();
            },
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/styles/link-item-expand";

    .link-item-expand {
        width: 100%;

        &.is-selected {
            width: calc(100% / 2 - 16px / 2);
        }

        &__body {
            &_row {
                &:first-child {
                    margin-bottom: 20px;

                    @include media-min($sm) {
                        margin-bottom: 110px;
                    }
                }
            }
        }

        @include media-min($sm) {
            width: calc(100% / 2 - 16px / 2);
        }

        @include media-min($lg) {
            width: calc(100% / 4 - 16px * 3 / 4);
        }

        @include media-min($xxl) {
            width: calc(100% / 5 - 16px * 4 / 5);
        }
    }
</style>
