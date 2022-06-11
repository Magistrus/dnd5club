<template>
    <div class="item-detail">
        <section-header
            :subtitle="item?.name?.eng || ''"
            :title="item?.name?.rus || ''"
            :copy="!error && !loading"
            fullscreen
        />

        <item-body
            v-if="item"
            :item="item"
        />
    </div>
</template>

<script>
    import SectionHeader from '@/components/UI/SectionHeader';
    import errorHandler from "@/helpers/errorHandler";
    import { useItemsStore } from "@/store/Inventory/ItemsStore";
    import ItemBody from "@/views/Inventory/Items/ItemBody";

    export default {
        name: 'ItemDetail',
        components: { ItemBody, SectionHeader },
        async beforeRouteUpdate(to, from, next) {
            await this.loadNewItem(to.path);

            next();
        },
        data: () => ({
            itemStore: useItemsStore(),
            item: undefined,
            loading: false,
            error: false,
        }),
        async mounted() {
            await this.loadNewItem(this.$route.path);
        },
        methods: {
            async loadNewItem(url) {
                try {
                    this.error = false;
                    this.loading = true;

                    this.item = await this.itemStore.itemInfoQuery(url);

                    this.loading = false;
                } catch (err) {
                    this.loading = false;
                    this.error = true;

                    errorHandler(err);
                }
            },

            close() {
                this.$router.push({ name: 'items' })
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>
