<template>
    <content-layout
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="classesQuery"
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
                transition-duration="0.15s"
            >
                <class-link
                    v-for="(el, elKey) in group.list"
                    :key="elKey"
                    :class-item="el"
                    :to="{ path: el.url }"
                />
            </div>
        </div>
    </content-layout>
</template>

<script>
    import { useClassesStore } from '@/store/Character/ClassesStore';
    import ContentLayout from '@/components/content/ContentLayout';
    import ClassLink from "@/views/Character/Classes/ClassLink";
    import _ from "lodash";

    export default {
        name: 'ClassesView',
        components: {
            ClassLink,
            ContentLayout,
        },
        data: () => ({
            classesStore: useClassesStore(),
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

                const groups = _.chain(classes.filter(item => 'group' in item))
                    .groupBy(o => o.group.name)
                    .map(list => ({
                        group: list[0].group,
                        list
                    }))
                    .sortBy(o => o.group.order)
                    .value();

                return [{
                    list: classes.filter(item => !('group' in item))
                }, ...groups];
            },

            showRightSide() {
                return this.$route.name === 'classDetail'
            }
        },
        async mounted() {
            await this.classesStore.initFilter();
            await this.classesStore.initClasses();
        },
        beforeUnmount() {
            this.classesStore.clearStore();
        },
        methods: {
            async classesQuery() {
                await this.classesStore.initClasses();
            },
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
