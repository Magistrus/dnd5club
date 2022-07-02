<template>
    <content-layout
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="racesQuery"
        @update="racesQuery"
        @list-end="nextPage"
    >
        <div
            ref="races"
            v-masonry="'races'"
            class="race-items"
            item-selector=".link-item-expand"
            :transition-duration="0"
            horizontal-order="true"
            :stagger="0"
            :destroy-delay="0"
            :gutter="16"
        >
            <race-link
                v-for="race in getRaces"
                :key="race.url"
                :race-item="race"
                :to="{path: race.url}"
                :redraw-handler="redrawMasonry"
            />
        </div>
    </content-layout>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import { useRacesStore } from "@/store/Character/RacesStore";
    import RaceLink from "@/views/Character/Races/RaceLink";
    import { mapActions, mapState } from "pinia";
    import { useUIStore } from "@/store/UI/UIStore";
    import { useResizeObserver } from "@vueuse/core/index";
    import debounce from "lodash/debounce";

    export default {
        name: 'RacesView',
        components: {
            RaceLink,
            ContentLayout,
        },
        computed: {
            ...mapState(useUIStore, ['getIsMobile', 'getFullscreen']),
            ...mapState(useRacesStore, ['getRaces', 'getFilter']),

            filter() {
                return this.getFilter || undefined
            },

            showRightSide() {
                return this.$route.name === 'raceDetail'
            },
        },
        async mounted() {
            await this.initFilter();
            await this.initRaces();

            useResizeObserver(this.$refs.races, this.redrawMasonry);
        },
        beforeUnmount() {
            this.clearStore();
        },
        methods: {
            ...mapActions(useRacesStore, ['initFilter', 'initRaces', 'nextPage', 'clearStore']),

            // eslint-disable-next-line func-names
            redrawMasonry: debounce(function() {
                this.$redrawVueMasonry('races')
            }, 50, { maxWait: 150 }),

            async racesQuery() {
                await this.initRaces();
            },
        }
    }
</script>
