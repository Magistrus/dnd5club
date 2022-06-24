<template>
    <content-detail class="trait-detail">
        <template #fixed>
            <section-header
                :subtitle="trait?.name?.eng || ''"
                :title="trait?.name?.rus || ''"
                :copy="!error && !loading"
                fullscreen
            />
        </template>

        <template #default>
            <trait-body :trait="trait"/>
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useTraitsStore } from '@/store/Character/TraitsStore';
    import RawContent from "@/components/content/RawContent";
    import errorHandler from "@/common/helpers/errorHandler";
    import TraitBody from "@/views/Character/Traits/TraitBody";
    import ContentDetail from "@/components/content/ContentDetail";

    export default {
        name: 'TraitDetail',
        components: {
            ContentDetail, TraitBody, RawContent, SectionHeader
        },
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
