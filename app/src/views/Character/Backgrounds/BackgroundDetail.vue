<template>
    <content-detail class="background-detail">
        <template #fixed>
            <section-header
                :subtitle="background?.name?.eng || ''"
                :title="background?.name?.rus || ''"
                :copy="!error && !loading"
                fullscreen
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
    import RawContent from "@/components/content/RawContent";
    import errorHandler from "@/common/helpers/errorHandler";
    import BackgroundBody from "@/views/Character/Backgrounds/BackgroundBody";
    import ContentDetail from "@/components/content/ContentDetail";

    export default {
        name: 'BackgroundDetail',
        components: {
            ContentDetail, BackgroundBody, RawContent, SectionHeader
        },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewBackground(to.path);

            next();
        },
        data: () => ({
            backgroundStore: useBackgroundsStore(),
            background: undefined,
            loading: false,
            error: false,
        }),
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
                this.$router.push({ name: 'backgrounds' })
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
