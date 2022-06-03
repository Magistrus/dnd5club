<template>
    <div class="race-detail">
        <section-header
            :copy="urlForCopy"
            :subtitle="race?.name?.eng || ''"
            :title="race?.name?.rus || ''"
            fullscreen
            @close="close"
        />
    </div>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useRacesStore } from '@/store/CharacterStore/RacesStore';

    export default {
        name: 'RaceDetail',
        components: { SectionHeader },
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
            urlForCopy() {
                return !this.error && !this.loading
                    ? window.location.origin + this.$route.path
                    : '';
            },
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
                    this.error = true;
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
