<template>
    <div class="item-body">
        <detail-tob-bar
            :left="categoriesString"
            :source="item.source"
        />

        <p v-if="item.price">
            <b>Стоимость:</b>

            <span>{{ item.price }}</span>
        </p>

        <p v-if="item.weight">
            <b>Вес (в фунтах):</b>

            <span>{{ item.weight }}</span>
        </p>

        <raw-content
            v-if="item?.description"
            :template="item.description"
        />
    </div>
</template>

<script>
    import DetailTobBar from "@/components/UI/DetailTobBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "ItemBody",
        components: { RawContent, DetailTobBar },
        props: {
            item: {
                type: Object,
                default: undefined,
                required: true
            }
        },
        computed: {
            categoriesString() {
                if (!this.item.categories?.length) {
                    return ''
                }

                let str = '';

                if (this.item.categories.length === 1) {
                    str += 'Категория: '
                }

                if (this.item.categories.length > 1) {
                    str += 'Категории: '
                }

                str += this.item.categories.join(', ');

                return str;
            }
        }
    }
</script>

<style scoped lang="scss">
    .item-body {
        padding: 24px;
    }
</style>
