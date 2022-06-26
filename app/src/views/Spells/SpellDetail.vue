<template>
    <content-detail class="spell-detail">
        <template #fixed>
            <section-header
                :subtitle="spell?.name?.eng || ''"
                :title="spell?.name?.rus || ''"
                :copy="!error && !loading"
                :close-on-desktop="getFullscreen"
                :fullscreen="!getIsMobile"
                @close="close"
            />
        </template>

        <template #default>
            <div
                v-if="loading"
                class="spell-detail__loader"
            >
                <div class="spell-detail__loader_img">
                    <img
                        alt=""
                        src="/app/img/loader.png"
                    >
                </div>
            </div>

            <div
                v-else-if="error"
                class="spell-detail__err"
            >
                error...
            </div>

            <spell-body
                v-else-if="spell"
                :spell="spell"
            />
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from "@/components/UI/SectionHeader";
    import { useSpellsStore } from "@/store/Spells/SpellsStore";
    import SpellBody from "@/views/Spells/SpellBody";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'SpellDetail',
        components: { ContentDetail, SpellBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewSpell(to.path);

            next();
        },
        data: () => ({
            spellsStore: useSpellsStore(),
            spell: undefined,
            loading: true,
            error: false,
        }),
        computed: {
            ...mapState(useUIStore, ['getFullscreen', 'getIsMobile']),
        },
        async mounted() {
            await this.loadNewSpell(this.$route.path);
        },
        methods: {
            close() {
                this.$router.push({ name: 'spells' });
            },

            async loadNewSpell(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.spell = await this.spellsStore.spellInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.error = true;
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .spell-detail {
        overflow: hidden;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;

        &__loader {
            width: 100%;
            flex: 1 1 100%;
            display: flex;
            align-items: center;
            justify-content: center;

            &_img {
                width: 70%;
                position: relative;
                display: flex;
                align-items: center;
                justify-content: center;

                &:before {
                    content: '';
                    display: block;
                    width: 100%;
                    padding-bottom: 100%;
                }

                img {
                    width: 100%;
                    height: 100%;
                    object-fit: contain;
                    position: absolute;
                    filter: drop-shadow(0 0 12px var(--bg-main));
                }
            }
        }
    }
</style>
