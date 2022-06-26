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
    import errorHandler from "@/common/helpers/errorHandler";
    import _ from "lodash";

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
                        return '2d20kh1';
                    case 'disadvantage':
                        return '2d20kl1';
                    case 'saving-throw':
                        return 'd20';
                    default:
                        return this.formula.replace('к', 'd');
                }
            },
        },
        methods: {
            // eslint-disable-next-line func-names
            tryRoll: _.throttle(function() {
                try {
                    this.error = false;

                    const { DiceRoll } = () => import('@dice-roller/rpg-dice-roller');
                    const roller = new DiceRoll(this.computedFormula);
                    const result = roller.toJSON();

                    this.roll = result.total;

                    this.clearRoll();
                } catch (err) {
                    this.error = true;

                    errorHandler(err);
                }
            }, 1000),

            // eslint-disable-next-line func-names
            clearRoll: _.debounce(function() {
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
