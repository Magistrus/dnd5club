<template>
    <div class="magic-item-detail">
        <section-header
            :subtitle="magicItem?.name?.eng || ''"
            :title="magicItem?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
        />

        <div
            v-if="loading"
            class="magic-item-detail__loader"
        >
            <div class="magic-item-detail__loader_img">
                <img
                    alt=""
                    src="/app/img/loader.png"
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
    </div>
</template>

<script>
    import SectionHeader from "@/components/UI/SectionHeader";
    import MagicItemBody from "@/views/Treasures/MagicItems/MagicItemBody";
    import { useMagicItemsStore } from "@/store/Treasures/MagicItemsStore";

    export default {
        name: 'MagicItemDetail',
        components: { MagicItemBody, SectionHeader },
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