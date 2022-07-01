<template>
    <content-detail class="magic-item-detail">
        <template #fixed>
            <section-header
                :close-on-desktop="getFullscreen"
                :copy="!error && !loading"
                :fullscreen="!getIsMobile"
                :subtitle="magicItem?.name?.eng || ''"
                :title="magicItem?.name?.rus || ''"
                @close="close"
            />
        </template>

        <template #default>
            <div
                v-if="loading"
                class="magic-item-detail__loader"
            >
                <div class="magic-item-detail__loader_img">
                    <img
                        v-lazy="'/img/loader.png'"
                        alt=""
                    >
                </div>
            </div>

            <div
                v-else-if="error"
                class="magic-item-detail__err"
            >
                error...
            </div>

            <magic-item-body
                v-else-if="magicItem"
                :magic-item="magicItem"
            />
        </template>
    </content-detail>
</template>

<script>
    import SectionHeader from "@/components/UI/SectionHeader";
    import MagicItemBody from "@/views/Treasures/MagicItems/MagicItemBody";
    import { useMagicItemsStore } from "@/store/Treasures/MagicItemsStore";
    import ContentDetail from "@/components/content/ContentDetail";
    import { mapState } from "pinia/dist/pinia";
    import { useUIStore } from "@/store/UI/UIStore";

    export default {
        name: 'MagicItemDetail',
        components: { ContentDetail, MagicItemBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewMagicItem(to.path);

            next();
        },
        data: () => ({
            magicItemsStore: useMagicItemsStore(),
            magicItem: undefined,
            loading: true,
            error: false,
        }),
        computed: {
            ...mapState(useUIStore, ['getFullscreen', 'getIsMobile']),
        },
        async mounted() {
            await this.loadNewMagicItem(this.$route.path);
        },
        methods: {
            close() {
                this.$router.push({ name: 'magicItems' });
            },

            async loadNewMagicItem(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.magicItem = await this.magicItemsStore.itemInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.error = true;
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .magic-item-detail {
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
