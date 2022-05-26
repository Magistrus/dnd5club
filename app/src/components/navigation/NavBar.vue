<template>
    <div
        class="nav-bar"
        :class="{'is-minified': uiStore.getMenuConfig.minified}"
    >
        <nav-head/>

        <div
            class="nav-bar__body"
            :class="{ 'is-active': isMenuShow }"
        >
            <nav class="nav-bar__nav-list">
                <nav-item
                    v-for="(item, index) in uiStore.getMenuConfig.leftItems"
                    :key="index"
                    :nav-item="item"
                    :to="{ name: item.name }"
                />
            </nav>

            <div class="nav-bar__controls">
                <nav-item-theme/>
            </div>
        </div>
    </div>
</template>

<script>
    import NavHead from '@/components/navigation/NavHead';
    import NavItem from '@/components/navigation/NavItem/NavItem';
    import NavItemTheme from '@/components/navigation/NavItem/NavItemTheme';
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'NavBar',
        components: {
            NavItemTheme,
            NavItem,
            NavHead
        },
        data() {
            return {
                isMenuShow: false,
                uiStore: useUIStore()
            }
        },
        watch: {
            menuConfig: {
                deep: true,
                handler(newValue) {
                    this.isMenuShow = newValue.show
                },
            }
        },
        mounted() {
            window.addEventListener('resize', this.setMenuShowing);
        },
        beforeUnmount() {
            window.removeEventListener('resize', this.setMenuShowing);
        },
        methods: {
            setMenuShowing() {
                if (window.innerWidth >= 768) {
                    this.isMenuShow = true;

                    return
                }

                this.isMenuShow = this.uiStore.getMenuConfig.show;
            },
        }
    };
</script>

<style lang="scss" scoped>
    .nav-bar {
        position: sticky;
        top: initial;
        left: 0;
        bottom: 0;
        right: 0;
        width: 100%;
        height: 73px;
        background-color: var(--bg-main);
        z-index: 102;
        margin-top: auto;
        border-top: 1px solid var(--border);

        @include media-min($md) {
            flex-shrink: 0;
            width: 72px;
            height: 100%;
            margin-top: initial;
            border: 0;
            background-color: transparent;
            top: 0;
        }

        &__nav-list,
        &__controls {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
        }

        &__controls {
            @include media-min($md) {
                margin-top: auto;
            }
        }

        &__body {
            position: absolute;
            top: initial;
            left: 0;
            bottom: 100%;
            background-color: var(--bg-main);
            width: 100%;
            height: calc(var(--max-vh) - 73px);
            overflow: hidden scroll;
            padding: 32px 16px;
            display: none;

            &.is-active {
                display: flex;
                flex-direction: column;
            }

            @include media-min($md) {
                padding: 24px 0;
                position: relative;
                left: initial;
                bottom: initial;
                overflow: visible;
                display: flex;
                align-items: center;
                flex-direction: column;
                background-color: transparent;
            }
        }

        @include media-min($xl) {
            &:not(.is-minified) {
                width: 270px;

                .nav-bar {
                    &__body {
                        padding: 24px 16px;
                        overflow: hidden scroll;
                    }
                }
            }
        }
    }
</style>
