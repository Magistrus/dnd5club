<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="bestiaryQuery"
        @update="bestiaryQuery"
        @list-end="nextPage"
    >
        <creature-link
            v-for="creature in bestiary"
            :key="creature.url"
            :in-tab="inTab"
            :creature="creature"
            :to="{path: creature.url}"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import { useBestiaryStore } from "@/store/Bestiary/BestiaryStore";
    import CreatureLink from "@/views/Bestiary/CreatureLink";

    export default {
        name: 'BestiaryView',
        components: {
            CreatureLink,
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
            bestiaryStore: useBestiaryStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            filter() {
                return this.bestiaryStore.getFilter || undefined;
            },

            bestiary() {
                return this.bestiaryStore.getBestiary || [];
            },

            showRightSide() {
                return this.$route.name === 'creatureDetail'
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

            if (this.bestiary.length && this.$route.name === 'bestiary') {
                await this.$router.push({ path: this.bestiary[0].url })
            }
        },
        beforeUnmount() {
            this.bestiaryStore.clearStore();
        },
        methods: {
            async init() {
                await this.bestiaryStore.initFilter(this.storeKey, this.customFilter);
                await this.bestiaryStore.initBestiary();
            },

            async bestiaryQuery() {
                await this.bestiaryStore.initBestiary();
            },

            async nextPage() {
                await this.bestiaryStore.nextPage();
            }
        }
    }
</script>
