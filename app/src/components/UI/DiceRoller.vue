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
    import { getRendered } from "@/common/utils/DiceRollRenderer";
    import SvgIcon from "@/components/UI/icons/SvgIcon";
    import { h } from "vue";

    export default {
        name: "DiceRoller",
        props: {
            formula: {
                type: String,
                default: '',
                required: true
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
        data: () => ({
            error: false
        }),
        computed: {
            classByType() {
                if (this.isAdvantage) {
                    return 'is-advantage';
                }

                if (this.isDisadvantage) {
                    return 'is-disadvantage';
                }

                if (this.isSavingThrow) {
                    return 'is-saving-throw';
                }

                return 'is-dice';
            },

            classes() {
                const classes = [this.classByType];

                if (this.error) {
                    classes.push('is-error');
                }

                return classes;
            }
        },
        methods: {
            /**
             * Выполнение броска
             *
             * @param { 'advantage' | 'disadvantage' | undefined } type - Бросок с преимуществом или помехой
             */
            tryRoll(type = undefined) {
                try {
                    this.error = false;

                    const roller = new DiceRoller();
                    const roll = roller.roll(this.getComputedFormula(type));

                    this.$toast(getRendered({
                        roll,
                        advantage: type === 'advantage' || this.isAdvantage,
                        disadvantage: type === 'disadvantage' || this.isDisadvantage
                    }), {
                        position: "bottom-right",
                        timeout: 5000,
                        icon: h(
                            SvgIcon,
                            {
                                iconName: 'dice-d20'
                            }
                        )
                    });
                } catch (err) {
                    this.error = true;

                    this.$toast.error('Произошла ошибка, попробуйте еще раз...');
                }
            },

            /**
             * Получение формулы броска
             *
             * @param { 'advantage' | 'disadvantage' | undefined } type - Бросок с преимуществом или помехой
             * @return { string }
             */
            getComputedFormula(type = undefined) {
                if (this.isAdvantage || type === 'advantage') {
                    return this.formula
                        .replace(/к/gim, 'd')
                        .replace(/1?d20/gim, '2d20kh1')
                        .replace(/–/gim, '-');
                }

                if (this.isDisadvantage || type === 'disadvantage') {
                    return this.formula
                        .replace(/к/gim, 'd')
                        .replace(/1?d20/gim, '2d20kl1')
                        .replace(/–/gim, '-');
                }

                return this.formula
                    .replace(/к/gim, 'd')
                    .replace(/–/gim, '-');
            }
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
