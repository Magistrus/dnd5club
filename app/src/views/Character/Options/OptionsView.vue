<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="optionsQuery"
        @update="optionsQuery"
    >
        <option-item
            v-for="(option, key) in options"
            :key="key"
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
    import { useOptionsStore } from "@/store/CharacterStore/OptionsStore";
    import OptionItem from "@/views/Character/Options/OptionItem";

    export default {
        name: 'OptionsView',
        components: {
            OptionItem,
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
        async mounted() {
            await this.optionsStore.initFilter(this.storeKey, this.customFilter);
            await this.optionsStore.initOptions();

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
        }
    }
</script>
