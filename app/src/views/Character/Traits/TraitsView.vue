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
                v-masonry="'trait-items'"
                class="trait-items"
                gutter="12"
                horizontal-order="false"
                item-selector=".trait-item"
                transition-duration="0.15s"
            >
                <trait-item
                    v-for="(el, key) in traitsStore.getTraits"
                    :key="key"
                    :to="{path: el.url}"
                    :trait-item="el"
                />
            </div>
        </template>
    </content-layout>
</template>

<script>
    import ListFilter from '@/components/filter/ListFilter';
    import ContentLayout from '@/components/content/ContentLayout';
    import TraitItem from "@/views/Character/Traits/TraitItem";
    import { useTraitsStore } from "@/store/CharacterStore/TraitsStore";

    export default {
        name: 'TraitsView',
        components: {
            TraitItem,
            ContentLayout,
            ListFilter
        },
        data: () => ({
            traitsStore: useTraitsStore(),
        }),
        computed: {
            filter() {
                return this.traitsStore.getFilter;
            },

            showRightSide() {
                return this.$route.name === 'traitDetail'
            },
        },
    }
</script>
