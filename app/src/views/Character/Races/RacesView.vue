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
            item-selector=".link-item-expand"
            transition-duration="0s"
            stagger="0s"
        >
            <race-link
                v-for="race in races"
                ref="race"
                :key="race.url"
                :race-item="race"
                :to="{path: race.url}"
                @resize="redrawMasonryOnResize"
                @submenu-toggled="redrawMasonry"
            />
        </div>
    </content-layout>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import { useRacesStore } from "@/store/Character/RacesStore";
    import RaceLink from "@/views/Character/Races/RaceLink";
    import debounce from "lodash/debounce";

    export default {
        name: 'RacesView',
        components: {
            RaceLink,
            ContentLayout,
        },
        data: () => ({
            racesStore: useRacesStore(),
            isMobile: false,
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
        watch: {
            races: {
                deep: true,
                handler() {
                    this.redrawMasonry();
                },
            }
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

            // eslint-disable-next-line func-names
            redrawMasonryOnResize: debounce(function() {
                this.redrawMasonry();
            }, 100, { maxWait: 300 }),

            redrawMasonry() {
                this.$nextTick(() => {
                    this.$redrawVueMasonry('race-items');
                })
            }
        }
    }
</script>
