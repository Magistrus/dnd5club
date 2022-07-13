<template>
    <content-detail>
        <template #fixed>
            <section-header
                :close-on-desktop="getFullscreen"
                :fullscreen="!getIsMobile"
                :subtitle="weapon?.name?.eng"
                :title="weapon?.name?.rus"
                copy
                @close="close"
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
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: "WeaponDetail",
        components: {
            ContentDetail, WeaponBody, SectionHeader
        },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewWeapon(to.path);

            next();
        },
        data: () => ({
            weaponsStore: useWeaponsStore(),
            weapon: undefined,
            loading: true,
            error: false
        }),
        computed: {
            ...mapState(useUIStore, [
                'getFullscreen',
                'getIsMobile'
            ])
        },
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
    };
</script>

<style lang="scss" scoped>

</style>
