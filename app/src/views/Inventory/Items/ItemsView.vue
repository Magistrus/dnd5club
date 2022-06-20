<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="itemsQuery"
        @update="itemsQuery"
        @list-end="nextPage"
    >
        <item-link
            v-for="(item, key) in items"
            :key="key"
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

            if (this.items.length && this.$route.name === 'items') {
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
            }
        }
    }
</script>