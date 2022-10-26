<template>
    <div class="ability-random">
        <div class="ability-random__row">
            <ui-button
                class="button"
                is-large
                @click.left.exact.prevent="tryRoll"
            >
                Бросить кубики
            </ui-button>
        </div>

        <div class="ability-random__row">
            <ability-table v-model="abilities"/>
        </div>
    </div>
</template>

<script lang="ts">
    import {
        defineComponent, ref
    } from "vue";
    import { useToast } from "vue-toastification";
    import UiButton from "@/components/form/UiButton.vue";
    import AbilityTable from "@/views/Tools/AbilityCalc/AbilityTable.vue";
    import { useDiceRoller } from "@/common/composition/useDiceRoller";

    export default defineComponent({
        components: {
            AbilityTable,
            UiButton
        },
        setup() {
            const { doRoll } = useDiceRoller();
            const toast = useToast();

            const abilities = ref({
                str: 0,
                dex: 0,
                con: 0,
                int: 0,
                wis: 0,
                cha: 0
            });

            const rolls = ref<number[]>([]);

            const tryRoll = () => {
                try {
                    rolls.value = [];

                    for (let i = 0; i < 6; i++) {
                        const roll = doRoll({
                            formula: '4d6kh3'
                        });

                        rolls.value.push(Number(roll.value));
                    }
                } catch (err) {
                    toast.error('Произошла какая-то ошибка... попробуй еще раз');
                }
            };

            return {
                abilities,
                tryRoll,
                rolls
            };
        }
    });
</script>

<style lang="scss" scoped>
    .ability-random {
        &__row {
            & + & {
                margin-top: 40px;
            }
        }
    }
</style>
