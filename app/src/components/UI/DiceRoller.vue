<template>
    <span
        v-tippy="{ content: `Нажмите для броска: <b>${formula}</b>` }"
        :class="classes"
        class="dice-roller"
        @click.left.exact.prevent="tryRoll"
        @click.ctrl="disadvantageRoll"
        @click.alt="advantageRoll"
    >
        <slot>{{ formula }}</slot>
    </span>
</template>

<script>
    import { DiceRoller } from 'dice-roller-parser';
    import { getRendered } from "@/common/utils/DiceRollRenderer";
    import { h } from "vue";
    import SvgIcon from "@/components/UI/icons/SvgIcon";

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
            advantage: false,
            disadvantage: false
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
                if (this.advantage) {
                    return this.formula
                        .replace(/к/gim, 'd')
                        .replace(/1{0,1}d20/gim, '2d20kh1')
                        .replace(/–/gim, '-');
                }

                if (this.disadvantage) {
                    return this.formula
                        .replace(/к/gim, 'd')
                        .replace(/1{0,1}d20/gim, '2d20kl1')
                        .replace(/–/gim, '-');
                }

                return this.formula
                    .replace(/к/gim, 'd')
                    .replace(/–/gim, '-');
            }
        },
        methods: {
            tryRoll() {
                try {
                    this.error = false;

                    const roller = new DiceRoller();
                    const result = roller.roll(this.computedFormula);

                    this.$toast(getRendered(result), {
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
            disadvantageRoll() {
                this.disadvantage = true;
                this.tryRoll();
                this.disadvantage = false;
            },
            advantageRoll() {
                this.advantage = true;
                this.tryRoll();
                this.advantage = false;
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
