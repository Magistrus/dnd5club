<template>
    <component
        :is="layout"
        :filter-instance="filter"
        :show-right-side="showRightSide"
        @search="spellsQuery"
        @update="spellsQuery"
        @list-end="nextPage"
    >
        <spell-link
            v-for="spell in spells"
            :key="spell.url"
            :in-tab="inTab"
            :spell="spell"
            :to="{path: spell.url}"
        />
    </component>
</template>

<script>
    import { useSpellsStore } from '@/store/Spells/SpellsStore';
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import SpellLink from "@/views/Spells/SpellLink";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'SpellsView',
        components: {
            SpellLink,
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
            filterUrl: {
                type: [String, undefined],
                default: undefined
            },
            books: {
                type: [Array, undefined],
                default: undefined
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
            ...mapState(useUIStore, ['getIsMobile']),

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
        watch: {
            storeKey: {
                async handler() {
                    await this.init();
                }
            },
            filterUrl: {
                async handler() {
                    await this.init();
                }
            },
            books: {
                deep: true,
                async handler() {
                    await this.init();
                }
            },
        },
        async mounted() {
            await this.init();

            if (!this.getIsMobile && this.spells.length && this.$route.name === 'spells') {
                await this.$router.push({ path: this.spells[0].url })
            }
        },
        beforeUnmount() {
            this.spellsStore.clearStore();
        },
        methods: {
            async init() {
                await this.spellsStore.initFilter(this.storeKey, this.filterUrl);
                await this.spellsStore.initSpells(this.books);
            },

            async spellsQuery() {
                await this.spellsStore.initSpells(this.books);
            },

            async nextPage() {
                await this.spellsStore.nextPage(this.books);
            }
        }
    }
</script>
