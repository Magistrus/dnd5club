<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="magicItemsQuery"
        @update="magicItemsQuery"
        @list-end="nextPage"
    >
        <magic-item-link
            v-for="(item, key) in magicItems"
            :key="key"
            :in-tab="inTab"
            :magic-item="item"
            :to="{path: item.url}"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import CreatureLink from "@/views/Bestiary/CreatureLink";
    import { useMagicItemsStore } from "@/store/Treasures/MagicItemsStore";
    import MagicItemLink from "@/views/Treasures/MagicItems/MagicItemLink";

    export default {
        name: 'MagicItemsView',
        components: {
            MagicItemLink,
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
            magicItemsStore: useMagicItemsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            filter() {
                return this.magicItemsStore.getFilter || undefined;
            },

            magicItems() {
                return this.magicItemsStore.getItems || [];
            },

            showRightSide() {
                return this.$route.name === 'magicItemDetail'
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

            if (this.magicItems.length && this.$route.name === 'magicItems') {
                await this.$router.push({ path: this.magicItems[0].url })
            }
        },
        beforeUnmount() {
            this.magicItemsStore.clearStore();
        },
        methods: {
            async init() {
                await this.magicItemsStore.initFilter(this.storeKey, this.customFilter);
                await this.magicItemsStore.initItems();
            },

            async magicItemsQuery() {
                await this.magicItemsStore.initItems();
            },

            async nextPage() {
                await this.magicItemsStore.nextPage();
            }
        }
    }
</script>
