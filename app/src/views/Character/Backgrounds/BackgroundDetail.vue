<template>
    <content-detail class="background-detail">
        <template #fixed>
            <section-header
                :close-on-desktop="getFullscreen"
                :copy="!error && !loading"
                :fullscreen="!getIsMobile"
                :subtitle="background?.name?.eng || ''"
                :title="background?.name?.rus || ''"
                print
                @close="close"
            />
        </template>

        <template #default>
            <background-body :background="background"/>
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useBackgroundsStore } from '@/store/Character/BackgroundsStore';
    import errorHandler from "@/common/helpers/errorHandler";
    import BackgroundBody from "@/views/Character/Backgrounds/BackgroundBody";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'BackgroundDetail',
        components: {
            ContentDetail, BackgroundBody, SectionHeader
        },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewBackground(to.path);

            next();
        },
        data: () => ({
            backgroundStore: useBackgroundsStore(),
            background: undefined,
            loading: false,
            error: false
        }),
        computed: {
            ...mapState(useUIStore, [
                'getFullscreen',
                'getIsMobile'
            ])
        },
        async mounted() {
            await this.loadNewBackground(this.$route.path);
        },
        methods: {
            async loadNewBackground(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.background = await this.backgroundStore.backgroundInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err);
                }
            },

            close() {
                this.$router.push({ name: 'backgrounds' });
            }
        }
    };
</script>

<style lang="scss" scoped>

</style>
