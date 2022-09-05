<template>
    <component
        :is="layout"
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="onSearch"
        @update="godsQuery"
        @list-end="nextPage"
    >
        <god-link
            v-for="god in gods"
            :key="god.url"
            :god="god"
            :in-tab="inTab"
            :to="{path: god.url}"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import GodLink from "@/views/Wiki/Gods/GodLink";
    import { useGodsStore } from "@/store/Wiki/GodsStore";
    import { mapState } from "pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'GodsView',
        components: {
            GodLink,
            TabLayout,
            ContentLayout
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
            godsStore: useGodsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile']),

            filter() {
                return this.godsStore.getFilter || undefined;
            },

            gods() {
                return this.godsStore.getGods || [];
            },

            showRightSide() {
                return this.$route.name === 'godDetail';
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
            }
        },
        async mounted() {
            await this.init();

            if (!this.getIsMobile && this.gods.length && this.$route.name === 'gods') {
                await this.$router.push({ path: this.gods[0].url });
            }
        },
        beforeUnmount() {
            this.godsStore.clearStore();
        },
        methods: {
            async init() {
                await this.godsStore.initFilter(this.storeKey, this.customFilter);
                await this.godsStore.initGods();
            },

            async godsQuery() {
                await this.godsStore.initGods();
            },

            async nextPage() {
                await this.godsStore.nextPage();
            },

            async onSearch() {
                await this.godsQuery();

                if (this.gods.length === 1 && !this.getIsMobile) {
                    await this.$router.push({ path: this.gods[0].url });
                }
            }
        }
    };
</script>
