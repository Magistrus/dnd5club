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
                v-masonry="'races-items'"
                class="race-items"
                gutter="16"
                horizontal-order="true"
                item-selector=".race-item"
                transition-duration="0.15s"
            >
                <race-item
                    v-for="(el, key) in racesStore.getRaces"
                    :key="key"
                    :race-item="el"
                    :to="{ path: el.url }"
                />
            </div>
        </template>
    </content-layout>
</template>

<script>
    import ListFilter from '@/components/filter/ListFilter';
    import { useRacesStore } from '@/store/CharacterStore/RacesStore';
    import ContentLayout from '@/components/content/ContentLayout';
    import RaceItem from '@/views/Character/Races/RaceItem';

    export default {
        name: 'RacesView',
        components: {
            RaceItem,
            ContentLayout,
            ListFilter,
        },
        data: () => ({
            racesStore: useRacesStore(),
        }),
        computed: {
            filter() {
                return this.racesStore.getFilter;
            },

            showRightSide() {
                return this.$route.name === 'raceDetail'
            },
        },
    }
</script>
