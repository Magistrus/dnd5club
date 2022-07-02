<template>
    <content-layout
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="onSearch"
        @update="classesQuery"
    >
        <div
            ref="classes"
            class="class-items"
        >
            <div
                v-for="(group, groupKey) in classes"
                :key="groupKey"
                class="class-items__group"
            >
                <div
                    v-if="group.group?.name"
                    class="class-items__group_name"
                >
                    {{ group.group.name }}
                </div>

                <div
                    v-masonry="'classes'"
                    class="class-items__group_list"
                    item-selector=".link-item-expand"
                    :transition-duration="0"
                    horizontal-order="true"
                    :stagger="0"
                    :destroy-delay="0"
                    :gutter="16"
                >
                    <class-link
                        v-for="el in group.list"
                        :key="el.url"
                        :after-search="!!search"
                        :class-item="el"
                        :to="{ path: el.url }"
                        :redraw-handler="redrawMasonry"
                    />
                </div>
            </div>
        </div>
    </content-layout>
</template>

<script>
    import { useClassesStore } from '@/store/Character/ClassesStore';
    import ContentLayout from '@/components/content/ContentLayout';
    import ClassLink from "@/views/Character/Classes/ClassLink";
    import sortBy from "lodash/sortBy";
    import groupBy from "lodash/groupBy";
    import debounce from "lodash/debounce";
    import { useUIStore } from "@/store/UI/UIStore";
    import { mapActions, mapState } from "pinia";

    export default {
        name: 'ClassesView',
        components: {
            ClassLink,
            ContentLayout,
        },
        async beforeRouteEnter(to, from, next) {
            const store = useClassesStore();

            await store.initFilter();
            await store.initClasses();

            next();
        },
        data: () => ({
            search: '',
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile', 'getFullscreen']),
            ...mapState(useClassesStore, ['getClasses', 'getFilter']),

            filter() {
                return this.getFilter || undefined;
            },

            classes() {
                const classes = this.getClasses || [];

                if (!classes?.length) {
                    return [];
                }

                const groups = sortBy(
                    Object.values(groupBy(
                        classes.filter(item => 'group' in item),
                        o => o.group.name
                    )).map(list => ({
                        group: list[0].group,
                        list: sortBy(list, [o => o.name.rus])
                    })),
                    [o => o.group.order]
                );

                return [{
                    list: sortBy(classes.filter(item => !('group' in item)), [o => o.name.rus])
                }, ...groups];
            },

            showRightSide() {
                return this.$route.name === 'classDetail'
            }
        },
        async mounted() {
            await this.initFilter();
            await this.initClasses();
        },
        beforeUnmount() {
            this.clearStore();
        },
        methods: {
            ...mapActions(useClassesStore, ['initFilter', 'initClasses', 'nextPage', 'clearStore']),

            // eslint-disable-next-line func-names
            redrawMasonry: debounce(function() {
                this.$redrawVueMasonry('classes')
            }, 50, { maxWait: 150 }),

            async classesQuery() {
                await this.initClasses();
            },

            // eslint-disable-next-line func-names
            onSearch: debounce(async function(e) {
                await this.classesQuery();

                this.search = e;
            }, 300)
        }
    }
</script>

<style lang="scss" scoped>
    .class-items {
        &__group {
            &_name {
                font-size: var(--h3-font-size);
                font-weight: 300;
                margin: 24px 0 16px 0;
                color: var(--text-color-title);
                position: relative;
                font-family: 'Lora';
            }

            &_list {
                display: flex;
            }

            &_col {
                flex: 1 1 100%;

                & + & {
                    margin-left: 16px;
                }
            }
        }
    }
</style>
