<template>
    <content-layout
        v-if="!inTab"
        :show-right-side="showRightSide"
    >
        <template
            v-if="filter"
            #filter
        >
            <list-filter :filter-instance="filter"/>
        </template>

        <template #items>
            <div
                v-masonry="'spell-items'"
                class="spell-items"
                gutter="12"
                horizontal-order="false"
                item-selector=".spell-item"
                transition-duration="0.15s"
            >
                <spell-item
                    v-for="(el, key) in spellsStore.getSpells"
                    :key="key"
                    :spell-item="el"
                    :to="{path: el.url}"
                />
            </div>
        </template>
    </content-layout>

    <tab-layout v-else>
        <template
            v-if="filter"
            #filter
        >
            <list-filter
                :filter-instance="filter"
                :in-tab="inTab"
            />
        </template>

        <template #items>
            <spell-item
                v-for="(el, key) in spellsStore.getSpells"
                :key="key"
                :spell-item="el"
                :to="{path: el.url}"
                :in-tab="inTab"
            />
        </template>
    </tab-layout>
</template>

<script>
    import { useSpellsStore } from '@/store/SpellsStore/SpellsStore';
    import ListFilter from '@/components/filter/ListFilter';
    import ContentLayout from '@/components/content/ContentLayout';
    import SpellItem from '@/views/Spells/SpellItem';
    import TabLayout from "@/components/content/TabLayout";

    export default {
        name: 'SpellsView',
        components: {
            TabLayout,
            ContentLayout,
            SpellItem,
            ListFilter
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
            url: {
                type: String,
                default: ''
            }
        },
        data: () => ({
            spellsStore: useSpellsStore(),
        }),
        computed: {
            filter() {
                return this.spellsStore.getFilter;
            },

            showRightSide() {
                return this.$route.name === 'spellDetail'
            },
        },
        async beforeCreate() {
            if (this.inTab && this.storeKey) {
                const store = useSpellsStore();

                await store.initFilter(this.storeKey);
                await store.spellsQuery();
            }
        }
    }
</script>
