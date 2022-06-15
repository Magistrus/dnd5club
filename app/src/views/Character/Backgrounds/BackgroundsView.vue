<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="backgroundsQuery"
        @update="backgroundsQuery"
    >
        <background-link
            v-for="(background, key) in backgrounds"
            :key="key"
            :in-tab="inTab"
            :background-item="background"
            :to="{path: background.url}"
        />
    </component>
</template>

<script>
    import ContentLayout from '@/components/content/ContentLayout';
    import TabLayout from "@/components/content/TabLayout";
    import { shallowRef } from "vue";
    import { useBackgroundsStore } from "@/store/Character/BackgroundsStore";
    import BackgroundLink from "@/views/Character/Backgrounds/BackgroundLink";

    export default {
        name: 'BackgroundsView',
        components: {
            BackgroundLink,
            TabLayout,
            ContentLayout,
        },
        props: {
            inTab: {
                type: Boolean,
                default: false
            },
            storeKey: {
                type: String,
                default: ''
            },
        },
        data: () => ({
            backgroundsStore: useBackgroundsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            }
        }),
        computed: {
            filter() {
                return this.backgroundsStore.getFilter || undefined;
            },

            backgrounds() {
                return this.backgroundsStore.getBackgrounds || [];
            },

            showRightSide() {
                return this.$route.name === 'backgroundDetail'
            },

            layout() {
                return this.inTab
                    ? this.layoutComponents.tab
                    : this.layoutComponents.content;
            }
        },
        async mounted() {
            await this.backgroundsStore.initFilter(this.storeKey);
            await this.backgroundsStore.initBackgrounds();

            if (this.backgrounds.length && this.$route.name === 'backgrounds') {
                await this.$router.push({ path: this.backgrounds[0].url })
            }
        },
        beforeUnmount() {
            this.backgroundsStore.clearStore();
        },
        methods: {
            async backgroundsQuery() {
                await this.backgroundsStore.initBackgrounds();
            },
        }
    }
</script>