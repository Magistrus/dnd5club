<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="weaponsQuery"
        @update="weaponsQuery"
    >
        <div
            v-for="(group, groupKey) in weapons"
            :key="groupKey"
            class="weapons-group"
        >
            <div class="weapons-group__name">
                {{ group.name }}
            </div>

            <div class="weapons-group__list">
                <weapon-link
                    v-for="weapon in group.list"
                    :key="weapon.url"
                    :weapon="weapon"
                    :to="{ path: weapon.url }"
                />
            </div>
        </div>
    </component>
</template>

<script>
    import { shallowRef } from "vue";
    import TabLayout from "@/components/content/TabLayout";
    import ContentLayout from "@/components/content/ContentLayout";
    import { useWeaponsStore } from "@/store/Inventory/WeaponsStore";
    import WeaponLink from "@/views/Inventory/Weapons/WeaponLink";
    import sortBy from "lodash/sortBy";

    export default {
        name: "WeaponsView",
        components: { WeaponLink },
        props: {
            inTab: {
                type: Boolean,
                default: false
            },
            storeKey: {
                type: String,
                default: ''
            },
            customFilter: {
                type: Object,
                default: undefined
            }
        },
        data: () => ({
            weaponsStore: useWeaponsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            },
        }),
        computed: {
            filter() {
                return this.weaponsStore.getFilter || undefined;
            },

            weapons() {
                const weapons = [];
                const types = [];

                if (!this.weaponsStore.getWeapons) {
                    return weapons
                }

                for (const weapon of this.weaponsStore.getWeapons) {
                    if (types.find(obj => obj.name === weapon.type.name)) {
                        continue;
                    }

                    types.push(weapon.type);
                }

                for (const type of sortBy(types, [o => o.order])) {
                    weapons.push({
                        name: type.name,
                        list: this.weaponsStore.getWeapons.filter(weapon => weapon.type.name === type.name)
                    });
                }

                return weapons;
            },

            showRightSide() {
                return this.$route.name === 'weaponDetail'
            },

            layout() {
                return this.inTab
                    ? this.layoutComponents.tab
                    : this.layoutComponents.content;
            }
        },
        watch: {
            storeKey: {
                async handler() {
                    await this.init();
                }
            },
            customFilter: {
                deep: true,
                async handler() {
                    await this.init();
                }
            },
        },
        async mounted() {
            await this.init();

            if (this.weapons[0]?.list[0]?.length && this.$route.name === 'weapons') {
                await this.$router.push({ path: this.weapons[0].list[0].url })
            }
        },
        beforeUnmount() {
            this.weaponsStore.clearStore();
        },
        methods: {
            async init() {
                await this.weaponsStore.initFilter(this.storeKey, this.customFilter);
                await this.weaponsStore.initWeapons();
            },

            async weaponsQuery() {
                await this.weaponsStore.initWeapons();
            },

            async nextPage() {
                await this.weaponsStore.nextPage();
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
