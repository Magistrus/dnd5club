<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="rulesQuery"
        @update="rulesQuery"
        @list-end="nextPage"
    >
        <rule-link
            v-for="(rule, key) in rules"
            :key="key"
            :in-tab="inTab"
            :rule="rule"
            :to="{path: rule.url}"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import { useRulesStore } from "@/store/Wiki/RulesStore";
    import RuleLink from "@/views/Wiki/Rules/RuleLink";

    export default {
        name: 'RulesView',
        components: {
            RuleLink,
            TabLayout,
            ContentLayout,
        },
        props: {
            inTab: {
                type: Boolean,
                default: false
            },
            storeKey: {
                type: String,
                default: ''
            },
            customFilter: {
                type: Object,
                default: undefined
            }
        },
        data: () => ({
            rulesStore: useRulesStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            filter() {
                return this.rulesStore.getFilter || undefined;
            },

            rules() {
                return this.rulesStore.getRules || [];
            },

            showRightSide() {
                return this.$route.name === 'ruleDetail'
            },

            layout() {
                return this.inTab
                    ? this.layoutComponents.tab
                    : this.layoutComponents.content;
            }
        },
        watch: {
            storeKey: {
                async handler() {
                    await this.init();
                }
            },
            customFilter: {
                deep: true,
                async handler() {
                    await this.init();
                }
            },
        },
        async mounted() {
            await this.init();

            if (this.rules.length && this.$route.name === 'rules') {
                await this.$router.push({ path: this.rules[0].url })
            }
        },
        beforeUnmount() {
            this.rulesStore.clearStore();
        },
        methods: {
            async rulesQuery() {
                await this.rulesStore.initRules();
            },

            async init() {
                await this.rulesStore.initFilter(this.storeKey, this.customFilter);
                await this.rulesStore.initRules();
            },

            async nextPage() {
                await this.rulesStore.nextPage();
            }
        }
    }
</script>
