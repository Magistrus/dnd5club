<template>
    <content-layout
        :filter-instance="getFilter"
        :show-right-side="showRightSide"
        @search="screensQuery"
        @list-end="nextPage"
    >
        <div
            class="screen-items"
            :class="{ 'is-selected': showRightSide }"
        >
            <router-link
                v-for="screen in getScreens"
                :key="screen.url"
                class="screen-item"
                :to="screen.url"
            >
                <div class="screen-item__name--rus">
                    {{ screen.name.rus }}
                </div>

                <div class="screen-item__name--eng">
                    {{ screen.name.eng }}
                </div>
            </router-link>
        </div>
    </content-layout>
</template>

<script>
    import ContentLayout from "@/components/content/ContentLayout";
    import { mapActions, mapState } from "pinia";
    import { useScreensStore } from "@/store/Screens/ScreensStore";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: "ScreensView",
        components: { ContentLayout },
        computed: {
            ...mapState(useUIStore, ['getIsMobile', 'getFullscreen']),
            ...mapState(useScreensStore, ['getScreens', 'getFilter']),

            showRightSide() {
                return this.$route.name === 'screenDetail'
            },
        },
        async mounted() {
            await this.initFilter();
            await this.initScreens();
        },
        beforeUnmount() {
            this.clearStore();
        },
        methods: {
            ...mapActions(useScreensStore, ['initFilter', 'initScreens', 'nextPage', 'clearStore']),

            async screensQuery() {
                await this.initScreens();
            },
        }
    }
</script>

<style lang="scss" scoped>
    .screen-items {
        width: 100%;
        display: grid;
        grid-gap: 16px;
        align-items: start;
        grid-template-columns: repeat(1, 1fr);

        @include media-min($sm) {
            grid-template-columns: repeat(2, 1fr);
        }

        @include media-min($lg) {
            grid-template-columns: repeat(3, 1fr);
        }

        @include media-min($xxl) {
            grid-template-columns: repeat(4, 1fr);
        }

        &.is-selected {
            @include media-min($sm) {
                grid-template-columns: repeat(1, 1fr);
            }
        }
    }

    .screen-item {
        @include css_anim();

        display: block;
        width: 100%;
        background-color: var(--bg-table-list);
        border-radius: 12px;
        border: 1px solid transparent;
        padding: 12px;
        color: var(--text-color-title);

        @include media-min($xl) {
            padding: 16px;
        }

        &__name {
            &--rus {
                font-size: calc(var(--h5-font-size) + 2px);
                margin: 0;
                font-weight: 500;
                line-height: normal;
                display: block;
            }

            &--eng {
                margin-top: 4px;
                font-size: calc(var(--h5-font-size) - 1px);
            }
        }

        &:hover {
            &:not(.router-link-active) {
                background-color: var(--bg-secondary);
            }
        }

        &.router-link-active {
            border-color: var(--primary-active);
        }
    }
</style>
