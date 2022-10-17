<template>
    <span
        v-tippy="{ content: `Нажмите для броска: <b>${formula}</b>` }"
        :class="classes"
        class="dice-roller"
        @click.left.exact.prevent="tryRoll()"
        @click.left.shift.exact.prevent="tryRoll('advantage')"
        @click.left.ctrl.exact.prevent="tryRoll('disadvantage')"
        @click.left.meta.exact.prevent="tryRoll('disadvantage')"
    >
        <slot>{{ formula }}</slot>
    </span>
</template>

<script>
    import { DiceRoller } from 'dice-roller-parser';
    import { POSITION, useToast } from "vue-toastification";
    import { computed, ref } from "vue";
    import { getRendered } from "@/common/utils/DiceRollRenderer";
    import { useIsDev } from "@/common/helpers/isDev";

    export default {
        name: "DiceRoller",
        props: {
            formula: {
                type: String,
                default: '',
                required: true
            },
            label: {
                type: String,
                default: ''
            },
            isAdvantage: {
                type: Boolean,
                default: false
            },
            isDisadvantage: {
                type: Boolean,
                default: false
            },
            isSavingThrow: {
                type: Boolean,
                default: false
            }
        },
        setup(props) {
            const isDev = useIsDev();
            const toast = useToast();
            const error = ref(false);

            const classByType = computed(() => {
                if (props.isAdvantage) {
                    return 'is-advantage';
                }

                if (props.isDisadvantage) {
                    return 'is-disadvantage';
                }

                if (props.isSavingThrow) {
                    return 'is-saving-throw';
                }

                return 'is-dice';
            });

            const classes = computed(() => {
                const result = [classByType.value];

                if (error.value) {
                    result.push('is-error');
                }

                return result;
            });

            /**
             * Получение формулы броска
             *
             * @param { 'advantage' | 'disadvantage' | undefined } type - Бросок с преимуществом или помехой
             * @return { string }
             */
            const getComputedFormula = (type = undefined) => {
                if (props.isAdvantage || type === 'advantage') {
                    return props.formula
                        .replace(/к/gim, 'd')
                        .replace(/1?d20/gim, '2d20kh1')
                        .replace(/–/gim, '-');
                }

                if (props.isDisadvantage || type === 'disadvantage') {
                    return props.formula
                        .replace(/к/gim, 'd')
                        .replace(/1?d20/gim, '2d20kl1')
                        .replace(/–/gim, '-');
                }

                return props.formula
                    .replace(/к/gim, 'd')
                    .replace(/–/gim, '-');
            };

            /**
             * Выполнение броска
             *
             * @param { 'advantage' | 'disadvantage' | undefined } type - Бросок с преимуществом или помехой
             */
            const tryRoll = (type = undefined) => {
                try {
                    error.value = false;

                    const roller = new DiceRoller();
                    const roll = roller.roll(getComputedFormula(type));

                    toast(getRendered({
                        roll,
                        label: props.label,
                        advantage: type === 'advantage' || props.isAdvantage,
                        disadvantage: type === 'disadvantage' || props.isDisadvantage
                    }), {
                        position: POSITION.BOTTOM_RIGHT,
                        timeout: 5000,
                        icon: false
                    });
                } catch (err) {
                    error.value = true;

                    if (isDev) {
                        throw new Error(err);
                    }

                    toast.error('Произошла ошибка, попробуйте еще раз...');
                }
            };

            return {
                tryRoll,
                classes
            };
        }
    };
</script>

<style lang="scss" scoped>
    .dice-roller {
        @include css_anim();

        font-weight: 500;
        cursor: pointer;
        white-space: nowrap;

        &:not(.is-error) {
            &.is-dice {
                color: var(--bg-dice);
            }

            &.is-advantage {
                color: var(--bg-advantage);
            }

            &.is-disadvantage {
                color: var(--bg-disadvantage);
            }

            &.is-saving-throw {
                color: var(--bg-saving_throw);
            }
        }

        &.is-error {
            color: var(--error);
        }
    }
</style>

<style lang="scss">
    .dice-roll {
        display: flex;

        &__result {
            font-size: var(--h1-font-size);
            line-height: var(--h1-font-size);
            font-weight: bold;
        }

        &__body {
            margin-left: 8px;
            display: flex;
            flex-direction: column;
        }

        &__label {
            font-weight: bold;
        }

        &__rendered {

        }
    }
</style>
