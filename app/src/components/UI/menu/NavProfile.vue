<template>
    <nav-popover v-model="popover">
        <template #trigger="{ setRef, isActive }">
            <div
                :ref="el => setRef(el)"
                class="navbar__btn"
                :class="{ 'is-active': isActive }"
                @click.left.exact.prevent="clickHandler"
            >
                <svg-icon
                    :stroke-enable="false"
                    fill-enable
                    :icon-name="!!getUser ? 'profile' : 'profile-outline'"
                />
            </div>
        </template>

        <template #default>
            <div
                v-if="!!getUser"
                class="nav-profile"
            >
                <div class="nav-profile__line is-main">
                    <span class="nav-profile__line_body">
                        {{ greeting }}, <b>{{ getUser.username }}</b>
                    </span>
                </div>

                <a
                    href="#"
                    class="nav-profile__line"
                    @click.left.exact.prevent="userLogout"
                >
                    <span class="nav-profile__line_body">
                        Выйти
                    </span>

                    <span class="nav-profile__line_icon">
                        <svg-icon
                            icon-name="logout"
                            :stroke-enable="false"
                            fill-enable
                        />
                    </span>
                </a>
            </div>
        </template>
    </nav-popover>

    <auth-reg-modal
        v-if="!getUser"
        v-model="modal"
        @close="closeModal"
    />
</template>

<script>
    import AuthRegModal from "@/components/UI/modals/AuthRegModal";
    import SvgIcon from "@/components/UI/SvgIcon";
    import { mapActions, mapState } from "pinia";
    import { useUserStore } from "@/store/UI/UserStore";
    import NavPopover from "@/components/UI/menu/NavPopover";

    export default {
        name: "NavProfile",
        components: {
            NavPopover,
            AuthRegModal,
            SvgIcon
        },
        data: () => ({
            modal: false,
            popover: false
        }),
        computed: {
            ...mapState(useUserStore, ['getUser']),

            greeting() {
                const hours = new Date().getHours();

                if (hours < 6) {
                    return 'Доброй ночи';
                }

                if (hours < 12) {
                    return 'Доброе утро';
                }

                if (hours < 18) {
                    return 'Добрый день';
                }

                return 'Добрый вечер';
            }
        },
        methods: {
            ...mapActions(useUserStore, ['logout']),

            async userLogout() {
                this.closeModal();

                await this.logout();

                this.closePopover();
            },

            clickHandler() {
                if (!this.getUser) {
                    this.toggleModal();

                    return;
                }

                this.togglePopover();
            },

            togglePopover() {
                if (!this.popover) {
                    this.openPopover();

                    return;
                }

                this.closePopover();
            },

            openPopover() {
                this.popover = true;
            },

            closePopover() {
                this.popover = false;
            },

            toggleModal() {
                if (!this.modal) {
                    this.openModal();

                    return;
                }

                this.closeModal();
            },

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
    .nav-profile {
        &__line {
            @include css_anim();

            width: 100%;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            text-align: left;
            padding: 0;
            color: var(--text-b-color);
            background-color: var(--bg-secondary);

            &.is-main {
                background-color: var(--bg-sub-menu);
                cursor: default;
            }

            &_body {
                flex: 1 1 auto;
                padding: 12px 16px;
            }

            &_icon {
                flex-shrink: 0;
                width: 40px;
                height: 40px;
                padding: 8px;
            }

            &:hover {
                background-color: var(--bg-sub-menu);
            }
        }
    }
</style>
