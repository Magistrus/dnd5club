<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="spellsQuery"
        @update="spellsQuery"
        @list-end="nextPage"
    >
        <spell-item
            v-for="(spell, key) in spells"
            :key="key"
            :in-tab="inTab"
            :spell-item="spell"
            :to="{path: spell.url}"
        />
    </component>
</template>

<script>
    import { useSpellsStore } from '@/store/SpellsStore/SpellsStore';
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
                return this.spellsStore.getFilter || undefined;
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

            if (this.spells.length && this.$route.name === 'spells') {
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