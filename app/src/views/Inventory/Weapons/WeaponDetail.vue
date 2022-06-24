<template>
    <content-detail>
        <template #fixed>
            <section-header
                :title="weapon?.name?.rus"
                :subtitle="weapon?.name?.eng"
                fullscreen
                copy
            />
        </template>

        <template #default>
            <weapon-body :weapon="weapon"/>
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from "@/components/UI/SectionHeader";
    import WeaponBody from "@/views/Inventory/Weapons/WeaponBody";
    import { useWeaponsStore } from "@/store/Inventory/WeaponsStore";
    import ContentDetail from "@/components/content/ContentDetail";

    export default {
        name: "WeaponDetail",
        components: { ContentDetail, WeaponBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewWeapon(to.path);

            next();
        },
        data: () => ({
            weaponsStore: useWeaponsStore(),
            weapon: undefined,
            loading: true,
            error: false,
        }),
        async mounted() {
            await this.loadNewWeapon(this.$route.path);
        },
        methods: {
            close() {
                this.$router.push({ name: 'weapons' });
            },

            async loadNewWeapon(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.weapon = await this.weaponsStore.weaponInfoQuery(url);

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
