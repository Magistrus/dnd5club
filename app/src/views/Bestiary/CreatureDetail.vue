<template>
    <div class="creature-detail">
        <section-header
            :subtitle="creature?.name?.eng || ''"
            :title="creature?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
            print
            @export-foundry="exportFoundry"
        />

        <div
            v-if="loading"
            class="creature-detail__loader"
        >
            <div class="creature-detail__loader_img">
                <img
                    alt=""
                    src="/app/img/loader.png"
                >
            </div>
        </div>

        <div
            v-else-if="error"
            class="creature-detail__err"
        >
            error...
        </div>

        <creature-body
            v-else-if="creature"
            :creature="creature"
        />
    </div>
</template>

<script>
    import SectionHeader from "@/components/UI/SectionHeader";
    import { useBestiaryStore } from "@/store/Bestiary/BestiaryStore";
    import CreatureBody from "@/views/Bestiary/CreatureBody";

    export default {
        name: 'CreatureDetail',
        components: { CreatureBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewCreature(to.path);

            next();
        },
        data: () => ({
            bestiaryStore: useBestiaryStore(),
            creature: undefined,
            loading: true,
            error: false,
        }),
        async mounted() {
            await this.loadNewCreature(this.$route.path);
        },
        methods: {
            close() {
                this.$router.push({ name: 'bestiary' });
            },

            async loadNewCreature(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.creature = await this.bestiaryStore.creatureInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.error = true;
                }
            },

            exportFoundry() {
                if (!this.creature?.foundry) {
                    return
                }

                window.open(this.creature.foundry, '_self');
            }
        }
    }
</script>

<style lang="scss" scoped>
    .creature-detail {
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
