<template>
    <div
        class="navbar__btn"
        @click.left.exact.prevent="openModal"
    >
        <svg-icon
            :stroke-enable="false"
            fill-enable
            icon-name="profile"
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
        components: {
            AuthRegModal,
            SvgIcon
        },
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
    };
</script>

<style lang="scss" scoped>

</style>
