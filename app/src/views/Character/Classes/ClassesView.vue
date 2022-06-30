<template>
    <content-layout
        :show-right-side="showRightSide"
        :filter-instance="filter"
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

                <div class="class-items__group_list">
                    <div
                        v-for="(column, columnKey) in group.list"
                        :key="columnKey"
                        class="class-items__group_col"
                    >
                        <class-link
                            v-for="el in column"
                            :key="el.url"
                            :class-item="el"
                            :to="{ path: el.url }"
                            :after-search="!!search"
                        />
                    </div>
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
    import { mapState, mapActions } from "pinia";
    import { useResizeObserver } from "@vueuse/core/index";

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
            cols: 1
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
                        list: this.getSorted(sortBy(list, [o => o.name.rus]))
                    })),
                    [o => o.group.order]
                );

                return [{
                    list: this.getSorted(sortBy(classes.filter(item => !('group' in item)), [o => o.name.rus]))
                }, ...groups];
            },

            showRightSide() {
                return this.$route.name === 'classDetail'
            }
        },
        watch: {
            showRightSide() {
                this.resizeHandler();
            },
        },
        async mounted() {
            this.resizeHandler();

            await this.initFilter();
            await this.initClasses();

            useResizeObserver(this.$refs.classes, this.resizeHandler);
        },
        beforeUnmount() {
            this.clearStore();
        },
        methods: {
            ...mapActions(useClassesStore, ['initFilter', 'initClasses', 'nextPage', 'clearStore']),

            resizeHandler() {
                const getSelectedCols = () => {
                    if (window.innerWidth >= 1400) {
                        return this.showRightSide ? 1 : 3;
                    }

                    if (window.innerWidth >= 992) {
                        return this.showRightSide ? 1 : 2;
                    }

                    return 1;
                }

                if (window.innerWidth >= 1400) {
                    this.cols = this.getFullscreen ? 3 : getSelectedCols();

                    return;
                }

                if (window.innerWidth >= 992) {
                    this.cols = this.getFullscreen ? 2 : getSelectedCols();

                    return;
                }

                this.cols = 1;
            },

            getSorted(list) {
                const classes = [];

                if (!list.length) {
                    return classes;
                }

                for (let i = 0; i < list.length; i++) {
                    const col = i % this.cols;

                    if (!classes[col]) {
                        classes.push([]);
                    }

                    classes[col].push(list[i]);
                }

                return classes;
            },

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
