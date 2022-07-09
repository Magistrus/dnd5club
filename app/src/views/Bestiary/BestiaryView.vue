<template>
    <component
        :is="layout"
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="onSearch"
        @update="bestiaryQuery"
        @list-end="nextPage"
    >
        <creature-link
            v-for="creature in bestiary"
            :key="creature.url"
            :creature="creature"
            :in-tab="inTab"
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
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

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
            ...mapState(useUIStore, ['getIsMobile']),

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

            if (!this.getIsMobile && this.bestiary.length && this.$route.name === 'bestiary') {
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
            },

            async onSearch() {
                await this.bestiaryStore.initBestiary();
                this.$nextTick(async () => {
                    if (this.bestiary.length === 1 && !this.getIsMobile) {
                        await this.$router.push({ path: this.bestiary[0].url })
                    }
                });
            }
        }
    }
</script>
