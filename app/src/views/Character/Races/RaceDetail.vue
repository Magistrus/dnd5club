<template>
    <div class="race-detail">
        <section-header
            :subtitle="race?.name?.eng || ''"
            :title="race?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
            @close="close"
        />

        <raw-content :template="race?.content"/>
    </div>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useRacesStore } from '@/store/Character/RacesStore';
    import RawContent from "@/components/content/RawContent";
    import errorHandler from "@/helpers/errorHandler";

    export default {
        name: 'RaceDetail',
        components: { RawContent, SectionHeader },
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
