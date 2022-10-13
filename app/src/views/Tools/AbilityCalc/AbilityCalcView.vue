<template>
    <page-layout class="ability-calc">
        <template #default>
            <ui-switch
                v-model="currentTab"
                :options="tabs"
                class="ability-calc__tabs"
                pre-select-first
                use-full-width
            />
        </template>
    </page-layout>
</template>

<script>
    import {
        defineComponent, ref
    } from "vue";
    import UiSwitch from "@/components/form/UiSwitch";
    import PageLayout from "@/components/content/PageLayout";

    export default defineComponent({
        components: {
            PageLayout,
            UiSwitch
        },
        setup() {
            const tabs = ref([
                {
                    id: 'random',
                    name: 'Случайный набор'
                },
                {
                    id: 'point-buy',
                    name: '«Покупка» значений'
                },
                {
                    id: 'standard',
                    name: 'Стандартный набор'
                }
            ]);
            const currentTab = ref(null);

            return {
                tabs,
                currentTab
            };
        }
    });
</script>

<style lang="scss" scoped>

</style>
