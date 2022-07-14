<template>
    <content-detail class="trait-detail">
        <template #fixed>
            <section-header
                :close-on-desktop="getFullscreen"
                :copy="!error && !loading"
                :fullscreen="!getIsMobile"
                :subtitle="trait?.name?.eng || ''"
                :title="trait?.name?.rus || ''"
                print
                @close="close"
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
    import errorHandler from "@/common/helpers/errorHandler";
    import TraitBody from "@/views/Character/Traits/TraitBody";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'TraitDetail',
        components: {
            ContentDetail,
            TraitBody,
            SectionHeader
        },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewTrait(to.path);

            next();
        },
        data: () => ({
            traitStore: useTraitsStore(),
            trait: undefined,
            loading: false,
            error: false
        }),
        computed: {
            ...mapState(useUIStore, ['getFullscreen', 'getIsMobile'])
        },
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
                this.$router.push({ name: 'traits' });
            }
        }
    };
</script>

<style lang="scss" scoped>

</style>
