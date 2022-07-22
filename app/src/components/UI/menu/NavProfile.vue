<template>
    <div
        class="navbar__btn"
        @click.left.exact.prevent="openModal"
    >
        <svg-icon
            :stroke-enable="false"
            fill-enable
            :icon-name="getUser ? 'profile' : 'login'"
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
    import { mapState, mapActions } from "pinia";
    import { useUserStore } from "@/store/UI/UserStore";
    import errorHandler from "@/common/helpers/errorHandler";

    export default {
        name: "NavProfile",
        components: {
            AuthRegModal,
            SvgIcon
        },
        data: () => ({
            modal: false
        }),
        computed: {
            ...mapState(useUserStore, ['getUser'])
        },
        async beforeMount() {
            try {
                await this.getUserFromSession();
            } catch (err) {
                errorHandler(err);
            }
        },
        methods: {
            ...mapActions(useUserStore, ['getUserFromSession']),

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
