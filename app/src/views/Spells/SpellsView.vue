<template>
    <content-layout :show-right-side="showRightSide">
        <template #filter>
            <list-filter/>
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
</template>

<script>
    import { useSpellsStore } from '@/store/SpellsStore/SpellsStore';
    import ListFilter from '@/components/filter/ListFilter';
    import ContentLayout from '@/components/content/ContentLayout';
    import SpellItem from '@/views/Spells/SpellItem';

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
