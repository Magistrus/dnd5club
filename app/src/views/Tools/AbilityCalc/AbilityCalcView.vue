<template>
    <page-layout
        class="ability-calc"
        :use-social-links="false"
    >
        <template #title>
            Калькулятор характеристик
        </template>

        <template #default>
            <div class="ability-calc__row">
                <ui-switch
                    v-model="currentTab"
                    :options="tabs"
                    class="ability-calc__tabs"
                    pre-select-first
                    use-full-width
                />
            </div>

            <div
                v-if="!!component"
                class="ability-calc__row"
            >
                <component
                    :is="component"
                    v-model="abilities"
                />
            </div>

            <div class="ability-calc__row">
                <ability-table v-model="abilities"/>
            </div>
        </template>
    </page-layout>
</template>

<script>
    import {
        computed,
        defineComponent, onMounted, ref
    } from "vue";
    import UiSwitch from "@/components/form/UiSwitch";
    import PageLayout from "@/components/content/PageLayout";
    import AbilityRandom from "@/views/Tools/AbilityCalc/AbilityRandom";
    import AbilityTable from "@/views/Tools/AbilityCalc/AbilityTable";

    export default defineComponent({
        components: {
            AbilityTable,
            PageLayout,
            UiSwitch
        },
        setup() {
            const currentTab = ref(null);

            const component = computed(() => currentTab.value?.component());

            const tabs = computed(() => [
                {
                    id: 'random',
                    name: 'Случайный набор',
                    component: () => AbilityRandom
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

            const initialAbilities = {
                str: {},
                dex: {},
                con: {},
                int: {},
                wis: {},
                cha: {}
            };

            const abilities = ref();

            onMounted(() => {
                abilities.value = initialAbilities;
            });

            return {
                tabs,
                currentTab,
                component,
                abilities
            };
        }
    });
</script>

<style lang="scss" scoped>
    .ability-calc {
        &__row {
            & + & {
                margin-top: 40px;
            }
        }
    }
</style>
