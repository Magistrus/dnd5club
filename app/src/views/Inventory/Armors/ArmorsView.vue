<template>
    <component
        :is="layout"
        :show-right-side="showRightSide"
        :filter-instance="filter"
        @search="armorsQuery"
        @update="armorsQuery"
    >
        <div
            v-for="(group, groupKey) in armors"
            :key="groupKey"
            class="armors-group"
        >
            <div class="armors-group__name">
                {{ group.name }}
            </div>

            <div class="armors-group__list">
                <armor-link
                    v-for="(armor, armorKey) in group.list"
                    :key="armorKey"
                    :armor="armor"
                    :to="{ path: armor.url }"
                />
            </div>
        </div>
    </component>
</template>

<script>
    import { shallowRef } from "vue";
    import TabLayout from "@/components/content/TabLayout";
    import ContentLayout from "@/components/content/ContentLayout";
    import { useArmorsStore } from "@/store/Inventory/ArmorsStore";
    import ArmorLink from "@/views/Inventory/Armors/ArmorLink";
    import { sortBy } from "lodash/fp";

    export default {
        name: "ArmorsView",
        components: { ArmorLink },
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
            armorsStore: useArmorsStore(),
            layoutComponents: {
                tab: shallowRef(TabLayout),
                content: shallowRef(ContentLayout)
            },
        }),
        computed: {
            filter() {
                return this.armorsStore.getFilter || undefined;
            },

            armors() {
                const armors = [];
                const types = [];

                if (!this.armorsStore.getArmors) {
                    return armors
                }

                for (const armor of this.armorsStore.getArmors) {
                    if (types.find(obj => obj.name === armor.type.name)) {
                        continue;
                    }

                    types.push(armor.type);
                }

                for (const type of sortBy(types, [o => o.order])) {
                    armors.push({
                        name: type.name,
                        list: this.armorsStore.getArmors.filter(armor => armor.type.name === type.name)
                    });
                }

                return armors;
            },

            showRightSide() {
                return this.$route.name === 'armorDetail'
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

            if (this.armors[0]?.list[0]?.length && this.$route.name === 'armors') {
                await this.$router.push({ path: this.armors[0].list[0].url })
            }
        },
        beforeUnmount() {
            this.armorsStore.clearStore();
        },
        methods: {
            async init() {
                await this.armorsStore.initFilter(this.storeKey, this.customFilter);
                await this.armorsStore.initArmors();
            },

            async armorsQuery() {
                await this.armorsStore.initArmors();
            },

            async nextPage() {
                await this.armorsStore.nextPage();
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
