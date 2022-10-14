<template>
    <div>{{ string }}</div>
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
            const string = computed(() => JSON.stringify(props.roll));

            return {
                string
            };
        }
    });
</script>

<style lang="scss" scoped>

</style>
