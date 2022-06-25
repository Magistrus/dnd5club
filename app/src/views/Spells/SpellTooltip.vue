<template>
    <tippy
        interactive
        sticky="reference"
        theme="dnd5club"
        strategy="fixed"
        :hide-on-click="false"
        @click-outside="() => false"
        @show="getContent"
    >
        <template #default>
            <slot name="default"/>
        </template>

        <template #content>
            <div
                v-if="spell"
                class="spell-tooltip"
            >
                <spell-body
                    :spell="spell"
                />
            </div>

            <span v-else-if="error">Боги не знают ответ на твой запрос...</span>

            <span v-else>Спрашиваю богов, ожидай...</span>
        </template>
    </tippy>
</template>

<script>
    import { Tippy } from "vue-tippy";
    import SpellBody from "@/views/Spells/SpellBody";
    import errorHandler from "@/common/helpers/errorHandler";

    export default {
        name: "SpellTooltip",
        components: { SpellBody, Tippy },
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
        methods: {
            async getContent() {
                this.error = false;

                if (this.spell) {
                    return;
                }

                if (!this.url) {
                    this.error = true;

                    return
                }

                const res = await this.$http.post(this.url);

                if (res.status !== 200) {
                    errorHandler(res.statusText);

                    this.error = true;

                    return;
                }

                this.spell = res.data;
            }
        }
    }
</script>

<style lang="scss" scoped>
    .spell-tooltip {
        width: 100%;
    }
</style>
