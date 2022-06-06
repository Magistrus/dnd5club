<template>
    <div class="trait-detail">
        <section-header
            :subtitle="trait?.name?.eng || ''"
            :title="trait?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
            @close="close"
        />

        <raw-content :template="trait?.content"/>
    </div>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useTraitsStore } from '@/store/CharacterStore/TraitsStore';
    import RawContent from "@/components/content/RawContent";
    import errorHandler from "@/helpers/errorHandler";

    export default {
        name: 'TraitDetail',
        components: { RawContent, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewTrait(to.path);

            next();
        },
        data: () => ({
            traitStore: useTraitsStore(),
            trait: undefined,
            loading: false,
            error: false,
        }),
        async mounted() {
            await this.loadNewTrait(this.$route.path);
        },
        methods: {
            async loadNewTrait(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.trait = await this.traitStore.traitInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err);
                }
            },

            close() {
                this.$router.push({ name: 'traits' })
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
