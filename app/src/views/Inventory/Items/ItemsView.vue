<template>
    <component
        :is="layout"
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="onSearch"
        @update="itemsQuery"
        @list-end="nextPage"
    >
        <item-link
            v-for="item in items"
            :key="item.url"
            :in-tab="inTab"
            :item-item="item"
            :to="{path: item.url}"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import ItemLink from "@/views/Inventory/Items/ItemLink";
    import { useItemsStore } from "@/store/Inventory/ItemsStore";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'ItemsView',
        components: {
            ItemLink,
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
            itemsStore: useItemsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            ...mapState(useUIStore, ['getIsMobile']),

            filter() {
                return this.itemsStore.getFilter || undefined;
            },

            items() {
                return this.itemsStore.getItems || [];
            },

            showRightSide() {
                return this.$route.name === 'itemDetail'
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

            if (!this.getIsMobile && this.items.length && this.$route.name === 'items') {
                await this.$router.push({ path: this.items[0].url })
            }
        },
        beforeUnmount() {
            this.itemsStore.clearStore();
        },
        methods: {
            async itemsQuery() {
                await this.itemsStore.initItems();
            },

            async init() {
                await this.itemsStore.initFilter(this.storeKey, this.customFilter);
                await this.itemsStore.initItems();
            },

            async nextPage() {
                await this.itemsStore.nextPage();
            },

            async onSearch() {
                await this.itemsQuery()
                if (this.items.length === 1 && !this.getIsMobile) {
                    await this.$router.push({ path: this.items[0].url })
                }
            },
        }
    }
</script>
