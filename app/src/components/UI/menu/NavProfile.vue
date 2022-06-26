<template>
    <div
        class="btn_nav"
        @click.left.exact.prevent="openModal"
    >
        <svg-icon icon-name="profile"/>
    </div>

    <auth-reg-modal
        v-model="modal.show"
        @close="closeModal"
    >
        <template #title>
            {{ modal.type === 'reg' ? 'Регистрация' : 'Авторизация' }}
        </template>

        <template #default>
            <component :is="modalType"/>
        </template>
    </auth-reg-modal>
</template>

<script>
    import { defineAsyncComponent } from "vue";
    import AuthRegModal from "@/components/UI/modals/AuthRegModal";

    export default {
        name: "NavProfile",
        components: { AuthRegModal },
        data: () => ({
            modal: {
                show: false,
                type: 'auth'
            }
        }),
        computed: {
            modalType: {
                get() {
                    switch (this.modal.type) {
                        case 'reg':
                            return defineAsyncComponent(() => import('@/components/account/RegistrationView'));

                        default:
                            return defineAsyncComponent(() => import('@/components/account/LoginView'));
                    }
                },

                set(e) {
                    this.modal.type = e;
                }
            }
        },
        methods: {
            openModal() {
                this.modal.show = true;
                this.modal.type = 'auth';
            },

            closeModal() {
                this.modal.show = false;
                this.modal.type = 'auth';
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
