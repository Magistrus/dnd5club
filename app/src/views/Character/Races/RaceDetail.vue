<template>
    <content-detail class="race-detail">
        <template #fixed>
            <section-header
                :subtitle="race?.name?.eng || ''"
                :title="race?.name?.rus || ''"
                :copy="!error && !loading"
                :fullscreen="!getIsMobile"
                close-on-desktop
                @close="close"
            />
        </template>

        <template #default>
            <race-body
                v-if="race"
                :race="race"
            />
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useRacesStore } from '@/store/Character/RacesStore';
    import RawContent from "@/components/content/RawContent";
    import errorHandler from "@/common/helpers/errorHandler";
    import RaceBody from "@/views/Character/Races/RaceBody";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'RaceDetail',
        components: {
            ContentDetail, RaceBody, RawContent, SectionHeader
        },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewRace(to.path);

            next();
        },
        data: () => ({
            raceStore: useRacesStore(),
            race: undefined,
            loading: false,
            error: false,
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile']),
        },
        async mounted() {
            await this.loadNewRace(this.$route.path);
        },
        methods: {
            async loadNewRace(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.race = await this.raceStore.raceInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err);
                }
            },

            close() {
                this.$router.push({ name: 'races' })
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
