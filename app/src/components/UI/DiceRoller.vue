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
            error: false,
            rpgDiceRoller: undefined
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
        async beforeMount() {
            this.rpgDiceRoller = await import('@dice-roller/rpg-dice-roller');
        },
        methods: {
            tryRoll() {
                try {
                    this.error = false;

                    const { DiceRoll } = this.rpgDiceRoller;
                    const roller = new DiceRoll(this.computedFormula);
                    const result = roller.toJSON();

                    if (result.total === result.maxTotal) {
                        this.$toast.success(result.output, {
                            timeout: 2000
                        });

                        return;
                    }

                    if (result.total === result.minTotal) {
                        this.$toast.error(result.output, {
                            timeout: 2000
                        });

                        return;
                    }

                    this.$toast(result.output, {
                        timeout: false
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
