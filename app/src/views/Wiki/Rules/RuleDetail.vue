<template>
    <div class="rule-detail">
        <section-header
            :subtitle="rule?.name?.eng || ''"
            :title="rule?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
        />

        <rule-body
            v-if="rule"
            :rule="rule"
        />
    </div>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import errorHandler from "@/helpers/errorHandler";
    import { useRulesStore } from "@/store/Wiki/RulesStore";
    import RuleBody from "@/views/Wiki/Rules/RuleBody";

    export default {
        name: 'RuleDetail',
        components: { RuleBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewRule(to.path);

            next();
        },
        data: () => ({
            ruleStore: useRulesStore(),
            rule: undefined,
            loading: false,
            error: false,
        }),
        async mounted() {
            await this.loadNewRule(this.$route.path);
        },
        methods: {
            async loadNewRule(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.rule = await this.ruleStore.ruleInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err);
                }
            },

            close() {
                this.$router.push({ name: 'rules' })
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
