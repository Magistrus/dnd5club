<template>
    <content-layout
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="onSearch"
        @update="classesQuery"
    >
        <div
            v-for="(group, groupKey) in classes"
            :key="groupKey"
            class="class-group"
        >
            <div
                v-if="group.group?.name"
                class="class-group__name"
            >
                {{ group.group.name }}
            </div>

            <div
                v-masonry="'class-items'"
                class="class-group__list"
                gutter="16"
                horizontal-order="false"
                item-selector=".link-item-expand"
                transition-duration="0s"
                stagger="0s"
            >
                <class-link
                    v-for="el in group.list"
                    ref="class"
                    :key="el.url"
                    :class-item="el"
                    :to="{ path: el.url }"
                    :after-search="!!search"
                    @resize="redrawMasonryOnResize"
                    @submenu-toggled="redrawMasonry"
                />
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
            classesStore: useClassesStore(),
            search: ''
        }),
        computed: {
            filter() {
                return this.classesStore.getFilter || undefined;
            },

            classes() {
                const classes = this.classesStore.getClasses || [];

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
        watch: {
            classes: {
                deep: true,
                handler() {
                    this.redrawMasonry();
                },
            }
        },
        beforeUnmount() {
            this.classesStore.clearStore();
        },
        methods: {
            async classesQuery() {
                await this.classesStore.initClasses();
            },

            // eslint-disable-next-line func-names
            onSearch: debounce(async function(e) {
                await this.classesQuery();

                this.search = e;
            }, 300),

            // eslint-disable-next-line func-names
            redrawMasonryOnResize: debounce(function() {
                this.redrawMasonry();
            }, 100, { maxWait: 300 }),

            redrawMasonry() {
                this.$nextTick(() => {
                    this.$redrawVueMasonry('class-items');
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
        .class-group {
            &__name {
            font-size: var(--h3-font-size);
            font-weight: 300;
            margin: 24px 0 16px 0;
            color: var(--text-color-title);
            position: relative;
            font-family: 'Lora';
            }
        }
</style>
