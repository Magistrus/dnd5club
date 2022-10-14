<template>
    <div>
        <strong>{{ value }}</strong>&nbsp;
        <span
            v-for="(item, index) in dices"
            :key="index"
            :class="getAdditionalClass(item)"
        >[{{ item.value }}]<span v-if="index !== dices.length - 1">+</span></span>
    </div>
</template>

<script>
    import { computed, defineComponent } from "vue";

    export default defineComponent({
        props: {
            roll: {
                type: Object,
                required: true
            }
        },
        setup(props) {
            const value = computed(() => props.roll.value);
            const dices = computed(() => props.roll.dice || props.roll.rolls);
            const getAdditionalClass = roll => {
                if (roll.critical === 'failure') {
                    return 'disadvantage';
                }

                if (roll.critical === 'success') {
                    return 'advantage';
                }

                return '';
            };

            return {
                value,
                dices,
                getAdditionalClass
            };
        }
    });
</script>

<style lang="scss" scoped>

</style>
