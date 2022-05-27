<template>
    <content-layout :show-right-side="showRightSide">
        <template #filter>
            <list-filter/>
        </template>

        <template #items>
            <div
                v-masonry="'option-items'"
                class="option-items"
                gutter="12"
                horizontal-order="false"
                item-selector=".option-item"
                transition-duration="0.15s"
            >
                <option-item
                    v-for="(el, key) in optionsStore.getOptions"
                    :key="key"
                    :option-item="el"
                    :to="{path: el.url}"
                />
            </div>
        </template>
    </content-layout>
</template>

<script>
    import ListFilter from '@/components/filter/ListFilter';
    import ContentLayout from '@/components/content/ContentLayout';
    import OptionItem from "@/views/Character/Options/OptionItem";
    import { useOptionsStore } from "@/store/CharacterStore/OptionsStore";

    export default {
        name: 'OptionsView',
        components: {
            OptionItem,
            ContentLayout,
            ListFilter
        },
        data: () => ({
            optionsStore: useOptionsStore(),
        }),
        computed: {
            showRightSide() {
                return this.$route.name === 'optionDetail'
            },
        },
    }
</script>
