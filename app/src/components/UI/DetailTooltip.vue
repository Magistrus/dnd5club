<template>
    <tippy
        v-bind="tippyConfig"
        ref="tooltip"
        theme="dnd5club no-padding"
    >
        <template #default>
            <slot name="default"/>
        </template>

        <template #content>
            <component
                :is="bodyComponent"
                v-if="content"
                :[type]="content"
                in-tooltip
            />
        </template>
    </tippy>
</template>

<script>
    import cloneDeep from "lodash/cloneDeep";
    import vTippyConfig from '@/common/utils/VueTippyConfig';
    import SpellBody from "@/views/Spells/SpellBody";
    import errorHandler from "@/common/helpers/errorHandler";
    import ScreenBody from "@/views/Screens/ScreenBody";
    import ItemBody from "@/views/Inventory/Items/ItemBody";
    import ArmorBody from "@/views/Inventory/Armors/ArmorBody";
    import WeaponBody from "@/views/Inventory/Weapons/WeaponBody";
    import CreatureBody from "@/views/Bestiary/CreatureBody";
    import MagicItemBody from "@/views/Treasures/MagicItems/MagicItemBody";
    import OptionBody from "@/views/Character/Options/OptionBody";
    import TraitBody from "@/views/Character/Traits/TraitBody";
    import GodBody from "@/views/Wiki/Gods/GodBody";

    export default {
        name: "DetailTooltip",
        components: { SpellBody },
        props: {
            url: {
                type: String,
                default: ''
            },
            type: {
                type: String,
                default: ''
            }
        },
        data: () => ({
            content: undefined,
            error: false,
            to: document.body
        }),
        computed: {
            tippyConfig() {
                const config = cloneDeep(vTippyConfig.defaultProps);

                config.onShow = () => this.getContent();

                return config;
            },

            bodyComponent() {
                switch (this.type) {
                    case 'option':
                        return OptionBody;

                    case 'trait':
                        return TraitBody;

                    case 'armor':
                        return ArmorBody;

                    case 'weapon':
                        return WeaponBody;

                    case 'magic-item':
                        return MagicItemBody;

                    case 'item':
                        return ItemBody;

                    case 'screen':
                        return ScreenBody;

                    case 'creature':
                        return CreatureBody;

                    case 'spell':
                        return SpellBody;

                    case 'god':
                        return GodBody;

                    default:
                        return 'div';
                }
            }
        },
        methods: {
            async getContent() {
                this.error = false;

                if (this.content) {
                    return true;
                }

                const link = this.$slots.default().find(node => node.props.href);
                const url = this.url || link?.props?.href;

                if (!url?.length) {
                    this.error = true;

                    return false;
                }

                const res = await this.$http.post(url);

                if (res.status !== 200) {
                    errorHandler(res.statusText);

                    this.error = true;

                    return false;
                }

                this.content = res.data;

                return true;
            }
        }
    };
</script>
