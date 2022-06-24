<template>
    <content-detail class="god-detail">
        <template #fixed>
            <section-header
                :subtitle="god?.name?.eng || ''"
                :title="god?.name?.rus || ''"
                :copy="!error && !loading"
                fullscreen
            />
        </template>

        <template #default>
            <div
                v-if="loading"
                class="god-detail__loader"
            >
                <div class="god-detail__loader_img">
                    <img
                        alt=""
                        src="/app/img/loader.png"
                    >
                </div>
            </div>

            <div
                v-else-if="error"
                class="god-detail__err"
            >
                error...
            </div>

            <god-body
                v-else-if="god"
                :god="god"
            />
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from "@/components/UI/SectionHeader";
    import { useGodsStore } from "@/store/Wiki/GodsStore";
    import GodBody from "@/views/Wiki/Gods/GodBody";
    import ContentDetail from "@/components/content/ContentDetail";

    export default {
        name: 'GodDetail',
        components: { ContentDetail, GodBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewGod(to.path);

            next();
        },
        data: () => ({
            godsStore: useGodsStore(),
            god: undefined,
            loading: true,
            error: false,
        }),
        async mounted() {
            await this.loadNewGod(this.$route.path);
        },
        methods: {
            close() {
                this.$router.push({ name: 'gods' });
            },

            async loadNewGod(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.god = await this.godsStore.godInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.error = true;
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .god-detail {
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
