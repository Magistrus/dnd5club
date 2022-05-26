<template>
    <content-layout :show-right-side="showRightSide">
        <template #filter>
            <list-filter/>
        </template>

        <template #items>
            <div
                v-masonry="'classes-items'"
                transition-duration="0.15s"
                class="class-items"
                item-selector=".class-item"
                gutter="16"
                horizontal-order="false"
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
    import ClassItem from '@/views/CharacterViews/Classes/ClassItem';
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
            showRightSide() {
                return this.$route.name === 'classDetail'
            }
        },
    }
</script>
