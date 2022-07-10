<template>
    <component
        :is="layout"
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="onSearch"
        @update="traitsQuery"
    >
        <trait-link
            v-for="trait in traits"
            :key="trait.url"
            :in-tab="inTab"
            :to="{path: trait.url}"
            :trait-item="trait"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import { useTraitsStore } from "@/store/Character/TraitsStore";
    import TraitLink from "@/views/Character/Traits/TraitLink";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'TraitsView',
        components: {
            TraitLink,
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
        },
        data: () => ({
            traitsStore: useTraitsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile']),

            filter() {
                return this.traitsStore.getFilter || undefined;
            },

            traits() {
                return this.traitsStore.getTraits || [];
            },

            showRightSide() {
                return this.$route.name === 'traitDetail'
            },

            layout() {
                return this.inTab
                    ? this.layoutComponents.tab
                    : this.layoutComponents.content;
            }
        },
        async mounted() {
            await this.traitsStore.initFilter(this.storeKey);
            await this.traitsStore.initTraits();

            if (!this.getIsMobile && this.traits.length && this.$route.name === 'traits') {
                await this.$router.push({ path: this.traits[0].url })
            }
        },
        beforeUnmount() {
            this.traitsStore.clearStore();
        },
        methods: {
            async traitsQuery() {
                await this.traitsStore.initTraits();
            },

            async onSearch() {
                await this.traitsQuery()
                if (this.traits.length === 1 && !this.getIsMobile) {
                    await this.$router.push({ path: this.traits[0].url })
                }
            },
        }
    }
</script>
