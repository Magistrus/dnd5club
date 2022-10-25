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

<script lang="ts">
    import type { Component } from "vue";
    import {
        computed, defineComponent, onBeforeMount, ref
    } from "vue";
    import PageLayout from "@/components/content/PageLayout.vue";
    import UiSwitch from "@/components/form/UiSwitch.vue";
    import AbilityTable from "@/views/Tools/AbilityCalc/AbilityTable.vue";
    import AbilityRandom from "@/views/Tools/AbilityCalc/AbilityRandom.vue";

    type TCalcTab = {
        id: string
        name: string
        component: Component
    }

    export default defineComponent({
        components: {
            AbilityTable,
            PageLayout,
            UiSwitch
        },
        setup() {
            const currentTab = ref<TCalcTab>({
                id: 'random',
                name: 'Случайный набор',
                component: () => AbilityRandom
            });

            const component = computed<Component>(() => currentTab.value?.component);

            const tabs: TCalcTab[] = [
                {
                    id: 'random',
                    name: 'Случайный набор',
                    component: () => AbilityRandom
                },
                {
                    id: 'point-buy',
                    name: '«Покупка» значений',
                    component: () => AbilityRandom
                },
                {
                    id: 'standard',
                    name: 'Стандартный набор',
                    component: () => AbilityRandom
                }
            ];

            const initialAbilities = {
                str: {},
                dex: {},
                con: {},
                int: {},
                wis: {},
                cha: {}
            };

            const abilities = ref();

            onBeforeMount(() => {
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
