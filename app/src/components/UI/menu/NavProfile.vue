<template>
    <div
        class="btn_nav"
        @click.left.exact.prevent="openModal"
    >
        <svg-icon
            icon-name="profile"
            :stroke-enable="false"
            fill-enable
        />
    </div>

    <auth-reg-modal
        v-model="modal"
        @close="closeModal"
    />
</template>

<script>
    import AuthRegModal from "@/components/UI/modals/AuthRegModal";
    import SvgIcon from "@/components/UI/SvgIcon";

    export default {
        name: "NavProfile",
        components: { AuthRegModal, SvgIcon },
        data: () => ({
            modal: false
        }),
        methods: {
            openModal() {
                this.modal = true;
            },

            closeModal() {
                this.modal = false;
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
