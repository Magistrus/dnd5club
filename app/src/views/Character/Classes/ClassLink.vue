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
            class="link-item-expand"
            v-bind="$attrs"
        >
            <div class="link-item-expand__content">
                <img
                    v-lazy="`/assets/img/classes/${classItem.image}.webp`"
                    alt="img-bg"
                    class="link-item-expand__content__img-bg"
                >

                <div class="link-item-expand__content__gradient"/>

                <div class="link-item-expand__main">
                    <a
                        :href="href"
                        class="link-item-expand__link"
                        @click.left.prevent.exact="selectClass(navigate)"
                    >

                        <span class="link-item-expand__body">
                            <span class="link-item-expand__body_row">

                                <span
                                    v-if="classItem.icon"
                                    class="link-item-expand__icon"
                                >
                                    <svg-icon
                                        :icon-name="classItem.image"
                                        :stroke-enable="false"
                                        fill-enable
                                    />
                                </span>

                                <span class="link-item-expand__name">
                                    <span class="link-item-expand__name--rus">
                                        {{ classItem.name.rus }}
                                    </span>

                                    <span class="link-item-expand__name--eng">
                                        {{ classItem.name.eng }}
                                    </span>

                                </span>
                            </span>

                            <span class="link-item-expand__body_row">
                                <span class="link-item-expand__dice">
                                    {{ classItem.dice }}
                                </span>

                                <span
                                    v-tippy="{ content: classItem.source.name }"
                                    class="link-item-expand__book"
                                >
                                    {{ classItem.source.shortName }}
                                </span>
                            </span>
                        </span>

                    </a>

                    <button
                        v-if="hasArchetypes"
                        v-tippy="{ content: 'Архетипы', placement: 'left' }"
                        class="link-item-expand__toggle"
                        type="button"
                        @click.left.exact.prevent="toggleArch"
                    >
                        <svg-icon :icon-name="submenu ? 'minus' : 'plus'"/>
                    </button>
                </div>

                <div
                    v-if="hasArchetypes"
                    :class="{ 'is-active': submenu }"
                    class="link-item-expand__arch-list"
                >
                    <div
                        v-for="(col, colKey) in classItem.archetypes"
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
                                    v-for="(arch, archKey) in group.list"
                                    :key="archKey"
                                    :to="{ path: arch.url }"
                                    class="link-item-expand__arch-item"
                                >
                                    <span class="link-item-expand__arch-item_name">{{ arch.name.rus }}</span>

                                    <span class="link-item-expand__arch-item_book">
                                        <span v-tippy="{ content: arch.source.name }">
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
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useResizeObserver } from "@vueuse/core/index";

    export default {
        name: 'ClassLink',
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
                submenu: false,
            }
        },
        computed: {
            hasArchetypes() {
                return !!this.classItem?.archetypes?.length
            },
        },
        watch: {
            submenu() {
                this.$emit('submenu-toggled');
            },
        },
        mounted() {
            this.$nextTick(() => useResizeObserver(this.$refs.classItem, () => this.$emit('resize')));
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive
                        || this.$route.params.className === this.$router.resolve(this.classItem.url)?.params?.className,
                    'is-selected': this.$route.name === 'classDetail',
                    'is-green': this.classItem?.source?.homebrew
                }
            },

            toggleArch() {
                this.submenu = !this.submenu;
            },

            selectClass(callback) {
                this.submenu = true;

                callback();
            },
        }
    }
</script>

<style lang="scss" scoped>
    @import "../../../assets/styles/link-item-expand";

    .link-item-expand {}
</style>
