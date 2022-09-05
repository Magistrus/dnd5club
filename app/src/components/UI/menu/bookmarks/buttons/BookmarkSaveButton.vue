<template>
    <default-bookmark-button v-bind="$props"/>

    <custom-bookmark-button
        v-if="isAuthenticated"
        v-bind="$props"
    />
</template>

<script>
    import { defineComponent } from "vue";
    import DefaultBookmarkButton from "@/components/UI/menu/bookmarks/buttons/DefaultBookmarkButton";
    import CustomBookmarkButton from "@/components/UI/menu/bookmarks/buttons/CustomBookmarkButton";
    import { useUserStore } from "@/store/UI/UserStore";
    import { mapActions, mapState } from "pinia";

    export default defineComponent({
        components: {
            CustomBookmarkButton,
            DefaultBookmarkButton
        },
        props: {
            name: {
                type: String,
                default: ''
            },
            url: {
                type: String,
                default: ''
            }
        },
        computed: {
            ...mapState(useUserStore, ['isAuthenticated'])
        },
        watch: {
            name: {
                immediate: true,
                async handler() {
                    await this.getUserStatus();
                }
            }
        },
        async beforeMount() {
            await this.getUserStatus();
        },
        methods: {
            ...mapActions(useUserStore, ['getUserStatus'])
        }
    });
</script>
