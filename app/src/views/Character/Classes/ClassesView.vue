<template>
    <content-layout :show-right-side="showRightSide">
        <template
            v-if="filter"
            #filter
        >
            <list-filter :filter-instance="filter"/>
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
                    v-for="(el, key) in classesStore.getClasses"
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

            showRightSide() {
                return this.$route.name === 'classDetail'
            }
        },
    }
</script>
