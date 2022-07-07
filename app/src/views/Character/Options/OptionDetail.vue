<template>
    <content-detail class="option-detail">
        <template #fixed>
            <section-header
                :close-on-desktop="getFullscreen"
                :copy="!error && !loading"
                :fullscreen="!getIsMobile"
                :subtitle="option?.name?.eng || ''"
                :title="option?.name?.rus || ''"
                print
                @close="close"
            />
        </template>

        <template #default>
            <option-body
                v-if="option"
                :option="option"
            />
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useOptionsStore } from '@/store/Character/OptionsStore';
    import errorHandler from "@/common/helpers/errorHandler";
    import OptionBody from "@/views/Character/Options/OptionBody";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'OptionDetail',
        components: {
            ContentDetail, OptionBody, SectionHeader
        },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewOption(to.path);

            next();
        },
        data: () => ({
            optionStore: useOptionsStore(),
            option: undefined,
            loading: false,
            error: false,
        }),
        computed: {
            ...mapState(useUIStore, ['getFullscreen', 'getIsMobile']),
        },
        async mounted() {
            await this.loadNewOption(this.$route.path);
        },
        methods: {
            async loadNewOption(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.option = await this.optionStore.optionInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err);
                }
            },

            close() {
                this.$router.push({ name: 'options' })
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
