<template>
    <content-layout :show-right-side="showRightSide">
        <template #filter>
            <list-filter/>
        </template>

        <template #items>
            <div
                v-masonry="'background-items'"
                class="background-items"
                gutter="12"
                horizontal-order="false"
                item-selector=".background-item"
                transition-duration="0.15s"
            >
                <background-item
                    v-for="(el, key) in backgroundsStore.getBackgrounds"
                    :key="key"
                    :background-item="el"
                    :to="{path: el.url}"
                />
            </div>
        </template>
    </content-layout>
</template>

<script>
    import ListFilter from '@/components/filter/ListFilter';
    import ContentLayout from '@/components/content/ContentLayout';
    import BackgroundItem from "@/views/Character/Backgrounds/BackgroundItem";
    import { useBackgroundsStore } from "@/store/CharacterStore/BackgroundsStore";

    export default {
        name: 'BackgroundsView',
        components: {
            BackgroundItem,
            ContentLayout,
            ListFilter
        },
        data: () => ({
            backgroundsStore: useBackgroundsStore(),
        }),
        computed: {
            showRightSide() {
                return this.$route.name === 'backgroundDetail'
            },
        },
    }
</script>
