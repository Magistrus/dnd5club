<template>
    <span
        v-tooltip="{ content: 'Нажмите для броска' }"
        class="dice-roller"
        :class="className"
        @click.left.exact.prevent="tryRoll"
    >
        {{ result }}
    </span>
</template>

<script>
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
            roll: undefined
        }),
        computed: {
            result() {
                return `${ this.formula }${ this.roll ? ` = ${ this.roll }` : '' }`
            },

            className() {
                return `is-${ this.type }`;
            },

            computedFormula() {
                switch (this.type) {
                    case 'advantage':
                        return '2к20';
                    case 'disadvantage':
                        return '2к20';
                    case 'saving-throw':
                        return 'к20';
                    default:
                        return this.formula
                }
            },
        },
        methods: {
            getParsedString() {
                const arr = this.computedFormula.split('к');
                const [count, dice] = arr;

                let result = 0;

                for (let i = 0; i < count; i++) {
                    result += this.getDiceResult(dice);
                }

                return result;
            },

            getDiceResult(dice) {
                const min = Math.ceil(1);
                const max = Math.floor(dice);

                return Math.floor(Math.random() * (max - min + 1)) + min;
            },

            tryRoll() {
                this.roll = this.getParsedString();

                setTimeout(() => {
                    this.roll = undefined;
                }, 5000);
            }
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
    }
</style>
