<template>
    <content-layout class="ability-calc">
        <ui-switch
            v-model="currentTab"
            :options="tabs"
            class="ability-calc__tabs"
            pre-select-first
            use-full-width
        />
    </content-layout>
</template>

<script>
    import {
        defineComponent, ref
    } from "vue";
    import ContentLayout from "@/components/content/ContentLayout";
    import UiSwitch from "@/components/form/UiSwitch";

    export default defineComponent({
        components: {
            UiSwitch,
            ContentLayout
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
