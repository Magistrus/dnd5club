<template>
    <content-layout :show-right-side="showRightSide">
        <template
            v-if="filter"
            #filter
        >
            <list-filter
                :filter-instance="filter"
                @search="classesQuery"
                @update="classesQuery"
            />
        </template>

        <template #items>
            <div
                v-masonry="'classes-items'"
                class="class-items"
                gutter="16"
                horizontal-order="false"
                item-selector=".class-item"
                transition-duration="0.15s"
            >
                <class-item
                    v-for="(el, key) in classes"
                    :key="key"
                    :class-item="el"
                    :to="{ path: el.url }"
                />
            </div>
        </template>
    </content-layout>
</template>

<script>
    import { useClassesStore } from '@/store/CharacterStore/ClassesStore';
    import ClassItem from '@/views/Character/Classes/ClassItem';
    import ListFilter from '@/components/filter/ListFilter';
    import ContentLayout from '@/components/content/ContentLayout';

    export default {
        name: 'ClassesView',
        components: {
            ContentLayout,
            ListFilter,
            ClassItem,
        },
        data: () => ({
            classesStore: useClassesStore(),
        }),
        computed: {
            filter() {
                return this.classesStore.getFilter;
            },

            classes() {
                return this.classesStore.getClasses || [];
            },

            showRightSide() {
                return this.$route.name === 'classDetail'
            }
        },
        async mounted() {
            await this.classesStore.initFilter(this.storeKey);
            await this.classesStore.initClasses(this.url);
        },
        beforeUnmount() {
            this.classesStore.clearStore();
        },
        methods: {
            async classesQuery() {
                await this.classesStore.initClasses(this.url);
            },
        }
    }
</script>
