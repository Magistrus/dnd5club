<template>
    <div
        class="nav-head"
        :class="{'is-minified': menuConfig.minified}"
    >
        <router-link
            :to="{name: 'home'}"
            class="nav-head__logo"
        >
            <site-logo/>
        </router-link>

        <button
            type="button"
            class="nav-head__minify"
            @click.left.exact.prevent="toggleMenuSize()"
        >
            <svg-icon
                icon-name="arrow-full"
                size="24"
            />
        </button>

        <button
            type="button"
            class="nav-head__sandwich"
            @click.left.exact.prevent="toggleMenuShowing"
        >
            <svg-icon
                icon-name="sandwich"
                size="24"
            />
        </button>
    </div>
</template>

<script>
    import { mapActions, mapState } from 'pinia/dist/pinia';
    import SvgIcon from '@/components/UI/SvgIcon';
    import SiteLogo from '@/components/UI/SiteLogo';
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'NavHead',
        components: {
            SiteLogo,
            SvgIcon
        },
        computed: {
            ...mapState(useUIStore, {
                menuConfig: 'getMenuConfig',
            }),
        },
        methods: {
            ...mapActions(useUIStore, {
                toggleMenuShowing: 'toggleMenuShowing',
                toggleMenuSize: 'toggleMenuSize',
            }),
        }
    }
</script>

<style lang="scss" scoped>
    .nav-head {
        display: flex;
        align-items: center;
        justify-content: space-between;
        position: relative;
        width: 100%;
        height: 100%;

        @include media-min($md) {
            height: 72px;
            border-top: 0;
        }

        > * {
            flex-shrink: 0;
        }

        &__logo {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 72px;
            height: 72px;
            position: relative;

            svg {
                width: 52px;
                height: 52px;
            }

            &:after {
                content: 'β';
                display: block;
                position: absolute;
                top: 8px;
                right: 5px;
                color: var(--primary);
                font-size: 17px;
                font-weight: 600;
            }
        }

        &__minify {
            @include css_anim($item: background-color);

            width: 24px;
            height: 24px;
            display: none;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            border: 1px solid var(--border);
            position: absolute;
            right: 24px;
            background-color: var(--bg-main);
            color: var(--primary);

            svg {
                width: 24px;
                height: 24px;
            }

            @include media-min($md) {
                background-color: transparent;

                &:hover {
                    background-color: var(--hover);
                }
            }

            @include media-min($xl) {
                display: flex;
            }
        }

        &__sandwich {
            color: var(--primary);
            display: flex;
            justify-content: center;
            align-items: center;
            width: 72px;
            height: 72px;

            svg {
                width: 24px;
                height: 24px;
            }

            @include media-min($md) {
                display: none;
            }
        }

        &.is-minified:not(.is-maximized) {
            .nav-head {
                &__minify {
                    transform: rotate(180deg);
                    right: -16px;
                }
            }
        }
    }
</style>
