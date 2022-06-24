<template>
    <span
        v-tippy="{
            content: result || 'Нажмите для броска',
            hideOnClick: false,
        }"
        class="dice-roller"
        :class="classes"
        @click.left.exact.prevent="tryRoll"
    >
        <slot>{{ formula }}</slot>
    </span>
</template>

<script>
    import { roll } from 'trpg-dice';
    import { debounce } from "lodash/fp";
    import errorHandler from "@/common/helpers/errorHandler";

    export default {
        name: "DiceRoller",
        props: {
            formula: {
                type: String,
                default: ''
            },
            type: {
                type: String,
                default: 'dice',
                validator: val => ['dice', 'advantage', 'disadvantage', 'saving-throw'].includes(val)
            }
        },
        data: () => ({
            roll: undefined,
            error: false,
        }),
        computed: {
            result() {
                return this.roll ? `Результат: <b>${ this.roll }</b>` : ''
            },

            classes() {
                const classes = [`is-${ this.type }`];

                if (this.error) {
                    classes.push('is-error');
                }

                return classes
            },

            computedFormula() {
                switch (this.type) {
                    case 'advantage':
                    case 'disadvantage':
                    case 'saving-throw':
                        return 'd20';
                    default:
                        return this.formula.replace('к', 'd');
                }
            },
        },
        methods: {
            tryRoll() {
                try {
                    this.error = false;

                    const callback = (err, result) => {
                        if (err) {
                            this.error = true;
                        } else {
                            switch (this.type) {
                                case 'advantage':
                                    this.roll = result.min;

                                    break;
                                case 'disadvantage':
                                    this.roll = result.min;

                                    break;
                                default:
                                    this.roll = result.avg;

                                    break;
                            }
                        }
                    }

                    switch (this.type) {
                        case 'advantage':
                        case 'disadvantage':
                            roll(this.computedFormula, { roll: 2 }, callback);

                            break;
                        default:
                            roll(this.computedFormula, callback);

                            break;
                    }

                    this.clearRoll();
                } catch (err) {
                    this.error = true;

                    errorHandler(err);
                }
            },

            // eslint-disable-next-line func-names
            clearRoll: debounce(function() {
                this.roll = undefined
            }, 5000),
        }
    }
</script>

<style lang="scss" scoped>
    .dice-roller {
        @include css_anim();

        padding: 0 3px;
        border-radius: 4px;
        color: var(--text-btn-color);
        cursor: pointer;

        &.is-dice {
            background-color: var(--bg-dice);
        }

        &.is-advantage {
            background-color: var(--bg-advantage);
        }

        &.is-disadvantage {
            background-color: var(--bg-disadvantage);
        }

        &.is-saving-throw {
            background-color: var(--bg-saving_throw);
        }

        &.is-error {
            background-color: var(--error);
        }
    }
</style>
