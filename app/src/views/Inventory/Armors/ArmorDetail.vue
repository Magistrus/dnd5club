<template>
    <content-detail>
        <template #fixed>
            <section-header
                :close-on-desktop="getFullscreen"
                :fullscreen="!getIsMobile"
                :subtitle="armor?.name?.eng"
                :title="armor?.name?.rus"
                bookmark
                print
                copy
                @close="close"
            />
        </template>

        <template #default>
            <armor-body :armor="armor"/>
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from "@/components/UI/SectionHeader";
    import ArmorBody from "@/views/Inventory/Armors/ArmorBody";
    import { useArmorsStore } from "@/store/Inventory/ArmorsStore";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: "ArmorDetail",
        components: {
            ContentDetail,
            ArmorBody,
            SectionHeader
        },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewArmor(to.path);

            next();
        },
        data: () => ({
            armorsStore: useArmorsStore(),
            armor: undefined,
            loading: true,
            error: false
        }),
        computed: {
            ...mapState(useUIStore, ['getFullscreen', 'getIsMobile'])
        },
        async mounted() {
            await this.loadNewArmor(this.$route.path);
        },
        methods: {
            close() {
                this.$router.push({ name: 'armors' });
            },

            async loadNewArmor(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.armor = await this.armorsStore.armorInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.error = true;
                }
            }
        }
    };
</script>

<style lang="scss" scoped>

</style>
