<template>
    <span
        v-tippy="{
            content: result || `Нажмите для броска: <b>${formula}</b>`,
            hideOnClick: false,
        }"
        :class="classes"
        class="dice-roller"
        @click.left.exact.prevent="tryRoll"
    >
        <slot>{{ formula }}</slot>
    </span>
</template>

<script>
    import errorHandler from "@/common/helpers/errorHandler";
    import debounce from "lodash/debounce";

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
            roll: undefined,
            error: false,
            rpgDiceRoller: undefined
        }),
        computed: {
            result() {
                return this.roll ? `Результат: <b>${ this.roll }</b>` : '';
            },

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
                return this.formula.replace(/к/gim, 'd');
            }
        },
        async beforeMount() {
            this.rpgDiceRoller = await import('@dice-roller/rpg-dice-roller');
        },
        methods: {
            // eslint-disable-next-line func-names
            tryRoll() {
                try {
                    this.error = false;

                    const { DiceRoll } = this.rpgDiceRoller;
                    const roller = new DiceRoll(this.computedFormula);
                    const result = roller.toJSON();

                    this.roll = result.total;

                    this.clearRoll();
                } catch (err) {
                    this.error = true;

                    errorHandler(err);
                }
            },

            // eslint-disable-next-line func-names
            clearRoll: debounce(function() {
                this.roll = undefined;
            }, 5000)
        }
    };
</script>

<style lang="scss" scoped>
    .dice-roller {
        @include css_anim();

        font-weight: 500;
        cursor: pointer;

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
