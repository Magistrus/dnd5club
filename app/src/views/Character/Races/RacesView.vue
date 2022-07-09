<template>
    <content-layout
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="onSearch"
        @update="racesQuery"
    >
        <div
            ref="races"
            class="race-items"
            :class="{ 'is-selected': showRightSide, 'is-fullscreen': getFullscreen }"
        >
            <race-link
                v-for="race in getRaces"
                :key="race.url"
                :race-item="race"
                :to="{path: race.url}"
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
        },
        beforeUnmount() {
            this.clearStore();
        },
        methods: {
            ...mapActions(useRacesStore, ['initFilter', 'initRaces', 'clearStore']),

            async racesQuery() {
                await this.initRaces();
            },

            async onSearch() {
                await this.racesQuery();
                if (this.getRaces.length === 1 && !this.getIsMobile) {
                    await this.$router.push({ path: this.getRaces[0].url })
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .race-items {
        width: 100%;
        padding: 0;
        display: grid;
        grid-gap: 16px;
        align-items: start;
        grid-template-columns: repeat(1, 1fr);

        @include media-min($sm) {
            grid-template-columns: repeat(2, 1fr);
        }

        @include media-min($lg) {
            grid-template-columns: repeat(4, 1fr);
        }

        @include media-min($xxl) {
            grid-template-columns: repeat(5, 1fr);
        }

        &.is-selected {
            @include media-min($sm) {
                grid-template-columns: repeat(2, 1fr);
            }
        }
    }
</style>
