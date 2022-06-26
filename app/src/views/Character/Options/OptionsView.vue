<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="optionsQuery"
        @update="optionsQuery"
    >
        <option-link
            v-for="option in options"
            :key="option.url"
            :in-tab="inTab"
            :option-item="option"
            :to="{path: option.url}"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import { useOptionsStore } from "@/store/Character/OptionsStore";
    import OptionLink from "@/views/Character/Options/OptionLink";

    export default {
        name: 'OptionsView',
        components: {
            OptionLink,
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
            optionsStore: useOptionsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            filter() {
                return this.optionsStore.getFilter || undefined;
            },

            options() {
                return this.optionsStore.getOptions || [];
            },

            showRightSide() {
                return this.$route.name === 'optionDetail'
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

            if (this.options.length && this.$route.name === 'options') {
                await this.$router.push({ path: this.options[0].url })
            }
        },
        beforeUnmount() {
            this.optionsStore.clearStore();
        },
        methods: {
            async optionsQuery() {
                await this.optionsStore.initOptions();
            },

            async init() {
                await this.optionsStore.initFilter(this.storeKey, this.customFilter);
                await this.optionsStore.initOptions();
            }
        }
    }
</script>
