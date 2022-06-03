<template>
    <content-layout
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="racesQuery"
        @update="racesQuery"
    >
        <div
            v-masonry="'race-items'"
            class="race-items"
            gutter="16"
            horizontal-order="false"
            item-selector=".race-item"
            transition-duration="0.15s"
        >
            <race-item
                v-for="(race, key) in races"
                :key="key"
                :race-item="race"
                :to="{path: race.url}"
            />
        </div>
    </content-layout>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import { useRacesStore } from "@/store/CharacterStore/RacesStore";
    import RaceItem from "@/views/Character/Races/RaceItem";

    export default {
        name: 'RacesView',
        components: {
            RaceItem,
            ContentLayout,
        },
        data: () => ({
            racesStore: useRacesStore(),
        }),
        computed: {
            filter() {
                return this.racesStore.getFilter || undefined;
            },

            races() {
                return this.racesStore.getRaces || [];
            },

            showRightSide() {
                return this.$route.name === 'raceDetail'
            },
        },
        async mounted() {
            await this.racesStore.initFilter();
            await this.racesStore.initRaces();
        },
        beforeUnmount() {
            this.racesStore.clearStore();
        },
        methods: {
            async racesQuery() {
                await this.racesStore.initRaces();
            },
        }
    }
</script>