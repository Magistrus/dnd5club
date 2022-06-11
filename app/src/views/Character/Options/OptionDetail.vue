<template>
    <div class="option-detail">
        <section-header
            :subtitle="option?.name?.eng || ''"
            :title="option?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
        />

        <option-body
            v-if="option"
            :option="option"
        />
    </div>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import { useOptionsStore } from '@/store/Character/OptionsStore';
    import RawContent from "@/components/content/RawContent";
    import errorHandler from "@/helpers/errorHandler";
    import OptionBody from "@/views/Character/Options/OptionBody";

    export default {
        name: 'OptionDetail',
        components: { OptionBody, RawContent, SectionHeader },
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
