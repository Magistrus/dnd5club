<template>
    <tippy
        v-bind="tippyConfig"
        theme="dnd5club no-padding"
    >
        <template #default>
            <slot name="default"/>
        </template>

        <template #content>
            <spell-body
                v-if="spell"
                :spell="spell"
            />
        </template>
    </tippy>
</template>

<script>
    import vTippyConfig from '@/common/utils/VueTippyConfig';
    import SpellBody from "@/views/Spells/SpellBody";
    import errorHandler from "@/common/helpers/errorHandler";
    import cloneDeep from "lodash/cloneDeep";

    export default {
        name: "SpellTooltip",
        components: { SpellBody },
        props: {
            url: {
                type: String,
                default: ''
            }
        },
        data: () => ({
            spell: undefined,
            error: false,
            to: document.body
        }),
        computed: {
            tippyConfig() {
                const config = cloneDeep(vTippyConfig.defaultProps);

                config.onShow = () => this.getContent();

                return config;
            }
        },
        methods: {
            async getContent() {
                this.error = false;

                if (this.spell) {
                    return true;
                }

                if (!this.url) {
                    this.error = true;

                    return false
                }

                const res = await this.$http.post(this.url);

                if (res.status !== 200) {
                    errorHandler(res.statusText);

                    this.error = true;

                    return false;
                }

                this.spell = res.data;

                return true
            }
        }
    }
</script>
