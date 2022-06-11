<template>
    <content-layout
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="classesQuery"
        @update="classesQuery"
    >
        <div
            v-masonry="'class-items'"
            class="class-items"
            gutter="16"
            horizontal-order="false"
            item-selector=".class-item"
            transition-duration="0.15s"
        >
            <class-link
                v-for="(el, key) in classes"
                :key="key"
                :class-item="el"
                :to="{ path: el.url }"
            />
        </div>
    </content-layout>
</template>

<script>
    import { useClassesStore } from '@/store/Character/ClassesStore';
    import ContentLayout from '@/components/content/ContentLayout';
    import ClassLink from "@/views/Character/Classes/ClassLink";

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
                return this.classesStore.getClasses || [];
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
