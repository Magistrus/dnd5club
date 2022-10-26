<template>
    <div class="ability-table"/>
</template>

<script lang="ts">
    import type { PropType } from 'vue';
    import { defineComponent } from "vue";

    export default defineComponent({
        props: {
            modelValue: {
                type: Object as PropType<{
                    str: number
                    dex: number
                    con: number
                    int: number
                    wis: number
                    cha: number
                }>,
                default: () => ({
                    str: 0,
                    dex: 0,
                    con: 0,
                    int: 0,
                    wis: 0,
                    cha: 0
                })
            }
        }
    });
</script>

<style lang="scss" scoped>

</style>
