<template>
    <ui-button
        class="button"
        is-large
    >
        Бросить кубики
    </ui-button>
</template>

<script>
    import { defineComponent } from "vue";
    import UiButton from "@/components/form/UiButton";

    export default defineComponent({
        components: {
            UiButton
        }
    });
</script>

<style lang="scss" scoped>
</style>
