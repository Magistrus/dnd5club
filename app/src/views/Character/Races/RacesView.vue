<template>
    <content-layout
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="racesQuery"
        @update="racesQuery"
        @list-end="nextPage"
    >
        <div
            ref="races"
            class="race-items"
        >
            <div
                v-for="(group, groupKey) in races"
                :key="groupKey"
                class="race-items__col"
            >
                <race-link
                    v-for="race in group"
                    :key="race.url"
                    :race-item="race"
                    :to="{path: race.url}"
                />
            </div>
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

    export default {
        name: 'RacesView',
        components: {
            RaceLink,
            ContentLayout,
        },
        data: () => ({
            cols: 1
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile', 'getFullscreen']),
            ...mapState(useRacesStore, ['getRaces', 'getFilter']),

            filter() {
                return this.getFilter || undefined
            },

            races() {
                const races = [];

                if (!this.getRaces) {
                    return races;
                }

                for (let i = 0; i < this.getRaces.length; i++) {
                    const col = i % this.cols;

                    if (!races[col]) {
                        races.push([]);
                    }

                    races[col].push(this.getRaces[i]);
                }

                return races;
            },

            showRightSide() {
                return this.$route.name === 'raceDetail'
            },
        },
        watch: {
            showRightSide() {
                this.setColumns();
            },
        },
        async mounted() {
            this.setColumns();

            await this.initFilter();
            await this.initRaces();

            useResizeObserver(this.$refs.races, this.setColumns);
        },
        beforeUnmount() {
            this.clearStore();
        },
        methods: {
            ...mapActions(useRacesStore, ['initFilter', 'initRaces', 'nextPage', 'clearStore']),

            setColumns() {
                const getSelectedCols = () => {
                    if (window.innerWidth >= 1400) {
                        return this.showRightSide ? 2 : 5;
                    }

                    if (window.innerWidth >= 992) {
                        return this.showRightSide ? 2 : 4;
                    }

                    if (window.innerWidth >= 576) {
                        return 2;
                    }

                    return 1;
                }

                if (window.innerWidth >= 1400) {
                    this.cols = this.getFullscreen ? 5 : getSelectedCols();

                    return;
                }

                if (window.innerWidth >= 992) {
                    this.cols = this.getFullscreen ? 4 : getSelectedCols();

                    return;
                }

                if (window.innerWidth >= 576) {
                    this.cols = this.getFullscreen ? 2 : getSelectedCols();

                    return;
                }

                this.cols = 1;
            },

            async racesQuery() {
                await this.initRaces();
            },
        }
    }
</script>

<style lang="scss" scoped>
    .race-items {
        display: flex;

        &__col {
            flex: 1 1 100%;

            & + & {
                margin-left: 16px;
            }
        }
    }
</style>
