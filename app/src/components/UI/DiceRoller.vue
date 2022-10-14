<template>
    <span
        v-tippy="{ content: `Нажмите для броска: <b>${formula}</b>` }"
        :class="classes"
        class="dice-roller"
        @click.left.exact.prevent="tryRoll"
    >
        <slot>{{ formula }}</slot>
    </span>
</template>

<script>
    import { DiceRoller } from 'dice-roller-parser';
    import { h } from "vue";
    import DiceRollRenderer from "@/components/UI/DiceRollRenderer";

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
            },

            computedFormula() {
                return this.formula.replace(/к/gim, 'd').replace(/–/gim, '-');
            }
        },
        methods: {
            tryRoll() {
                try {
                    this.error = false;

                    const roller = new DiceRoller();
                    const result = roller.roll(this.computedFormula);

                    console.log(result);

                    this.$toast(h(DiceRollRenderer, {
                        roll: result
                    }), {
                        position: "bottom-right",
                        timeout: 15000,
                        icon: 'dice-d20'
                    });
                } catch (err) {
                    this.error = true;

                    this.$toast.error('Произошла ошибка, попробуйте еще раз...');
                }
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
