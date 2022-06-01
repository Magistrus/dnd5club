<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        @list-end="nextPage"
    >
        <template
            v-if="filter"
            #filter
        >
            <list-filter
                :filter-instance="filter"
                @search="spellsQuery"
                @update="spellsQuery"
            />
        </template>

        <template #items>
            <spell-item
                v-for="(spell, key) in spells"
                :key="key"
                :spell-item="spell"
                :to="{path: spell.url}"
            />
        </template>
    </component>
</template>

<script>
    import { useSpellsStore } from '@/store/SpellsStore/SpellsStore';
    import ListFilter from '@/components/filter/ListFilter';
    import ContentLayout from '@/components/content/ContentLayout';
    import SpellItem from '@/views/Spells/SpellItem';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";

    export default {
        name: 'SpellsView',
        components: {
            TabLayout,
            ContentLayout,
            SpellItem,
            ListFilter,
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
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            filter() {
                return this.spellsStore.getFilter;
            },

            spells() {
                return this.spellsStore.getSpells || [];
            },

            showRightSide() {
                return this.$route.name === 'spellDetail'
            },

            layout() {
                return this.inTab
                    ? this.layoutComponents.tab
                    : this.layoutComponents.content;
            }
        },
        async mounted() {
            await this.spellsStore.initFilter(this.storeKey);
            await this.spellsStore.initSpells(this.url);

            if (this.spells.length && this.$route.name !== 'spellDetail') {
                await this.$router.push({ path: this.spells[0].url })
            }
        },
        beforeUnmount() {
            this.spellsStore.clearStore();
        },
        methods: {
            async spellsQuery() {
                await this.spellsStore.initSpells(this.url);
            },

            async nextPage() {
                await this.spellsStore.nextPage();
            }
        }
    }
</script>

<style lang="scss" scoped>
    .spell-items {
        width: 100%;
        height: 100%;
        overflow: hidden;
    }
</style>
