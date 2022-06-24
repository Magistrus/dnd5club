<template>
    <content-detail>
        <template #fixed>
            <section-header
                :title="armor?.name?.rus"
                :subtitle="armor?.name?.eng"
                fullscreen
                copy
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

    export default {
        name: "ArmorDetail",
        components: { ContentDetail, ArmorBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewArmor(to.path);

            next();
        },
        data: () => ({
            armorsStore: useArmorsStore(),
            armor: undefined,
            loading: true,
            error: false,
        }),
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
    }
</script>

<style lang="scss" scoped>

</style>
