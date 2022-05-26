<template>
    <content-layout :show-right-side="showRightSide">
        <template #filter>
            <list-filter/>
        </template>

        <template #items>
            <div
                v-masonry="'spell-items'"
                transition-duration="0.15s"
                class="spell-items"
                item-selector=".spell-item"
                gutter="12"
                horizontal-order="false"
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
</template>

<script>
    import { useSpellsStore } from '@/store/SpellsStore/SpellsStore';
    import ListFilter from '@/components/filter/ListFilter';
    import SpellItem from '@/views/SpellViews/Spells/SpellItem';
    import ContentLayout from '@/components/content/ContentLayout';

    export default {
        name: 'SpellsView',
        components: {
            ContentLayout,
            SpellItem,
            ListFilter
        },
        data: () => ({
            spellsStore: useSpellsStore(),
        }),
        computed: {
            showRightSide() {
                return this.$route.name === 'spellDetail'
            },
        },
    }
</script>
