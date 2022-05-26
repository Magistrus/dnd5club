<template>
    <router-link
        v-if="!isExternalLink(navItem)"
        v-slot="{href, navigate, isActive}"
        v-bind="$props"
        :to="{ name: navItem.name }"
        custom
    >
        <div
            v-bind="$attrs"
            class="nav-item"
            :class="{'router-link-active': isActive, ...classList}"
            @mouseenter="setSubmenuPositionClass"
            @mouseleave="setSubmenuPositionClass"
        >
            <div
                v-if="hasChildren"
                class="nav-item__trigger"
                @click.left.exact.prevent="uiStore.toggleSubmenu(navItem.label)"
            >
                <div class="nav-item__icon">
                    <svg-icon :icon-name="`left-menu-${navItem.name}`"/>
                </div>

                <div class="nav-item__name">
                    {{ navItem.label }}
                </div>

                <div class="nav-item__arrow">
                    <svg-icon icon-name="arrow-stroke"/>
                </div>
            </div>

            <a
                v-else
                :href="href"
                class="nav-item__trigger"
                @click="navigate"
            >
                <div class="nav-item__icon">
                    <svg-icon :icon-name="`left-menu-${navItem.name}`"/>
                </div>

                <div class="nav-item__name">
                    {{ navItem.label }}
                </div>
            </a>

            <div
                ref="submenu"
                class="nav-item__submenu"
                :class="{ 'is-bottom': !submenuIsVisible }"
            >
                <div class="nav-item__sub-title">
                    {{ navItem.label }}
                </div>

                <div
                    v-for="(child, index) in navItem.children"
                    :key="index"
                    class="nav-item__sub-item--wrapper"
                >
                    <router-link
                        v-if="!isExternalLink(child)"
                        :to="{name: child.name}"
                        class="nav-item__sub-item"
                    >
                        {{ child.label }}
                    </router-link>

                    <a
                        v-else
                        target="_blank"
                        :href="child.url"
                        class="nav-item__sub-item"
                    >
                        {{ child.label }}
                    </a>
                </div>
            </div>
        </div>
    </router-link>

    <div
        v-else
        class="nav-item"
        :class="classList"
        @mouseenter="setSubmenuPositionClass"
        @mouseleave="setSubmenuPositionClass"
    >
        <div
            class="nav-item__trigger"
            @click.left.exact.prevent="uiStore.toggleSubmenu(navItem.label)"
        >
            <div class="nav-item__icon">
                <svg-icon :icon-name="`left-menu-${navItem.name}`"/>
            </div>

            <div class="nav-item__name">
                {{ navItem.label }}
            </div>

            <div class="nav-item__arrow">
                <svg-icon icon-name="arrow-stroke"/>
            </div>
        </div>

        <div
            ref="submenu"
            class="nav-item__submenu"
            :class="{ 'is-bottom': !submenuIsVisible }"
        >
            <div class="nav-item__sub-title">
                {{ navItem.label }}
            </div>

            <div
                v-for="(child, index) in navItem.children"
                :key="index"
                class="nav-item__sub-item--wrapper"
            >
                <a
                    target="_blank"
                    :href="child.url"
                    class="nav-item__sub-item"
                >
                    {{ child.label }}
                </a>
            </div>
        </div>
    </div>
</template>

<script>
    import { RouterLink } from 'vue-router'
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'NavItem',
        components: { SvgIcon },
        inheritAttrs: false,
        props: {
            navItem: {
                type: Object,
                default: () => ({}),
                required: true
            },
            ...RouterLink.props
        },
        data() {
            return {
                submenuIsVisible: false,
                disabledLinks: false,
                uiStore: useUIStore(),
            }
        },
        computed: {
            classList() {
                return {
                    'is-minified': this.uiStore.getMenuConfig.minified,
                    'is-opened': this.uiStore.getMenuConfig.submenu === this.navItem.label,
                }
            },

            hasChildren() {
                return 'children' in this.navItem
                    && Array.isArray(this.navItem.children)
                    && !!this.navItem.children.length
            },

            isParentExternalLink() {
                return !this.navItem?.url?.startsWith('http');
            },
        },
        methods: {
            setSubmenuPositionClass() {
                const { submenu } = this.$refs;

                if (!submenu) {
                    return;
                }

                const rect = submenu.getBoundingClientRect();

                this.submenuIsVisible = rect.bottom + 16 < window.innerHeight;
            },

            isExternalLink(el) {
                return !!el?.external;
            },
        },
    }
</script>

<style lang="scss" scoped>
    @import "NavItem.module.scss";
</style>
