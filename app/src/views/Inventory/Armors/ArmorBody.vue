<template>
    <div
        v-if="armor"
        class="armor-body"
    >
        <detail-tob-bar
            :left="armor.type.name"
            :source="armor.source"
            :bg-grey="false"
        />

        <div class="grid_stat_block">
            <div class="block">
                <p>Класс Доспеха (AC):</p>

                <span>{{ armor.armorClass }}</span>
            </div>

            <div class="block">
                <p>Стоимость:</p>

                <span>{{ armor.price }}</span>
            </div>

            <div class="block">
                <p>Вес (в фунтах):</p>

                <span>{{ armor.weight }}</span>
            </div>

            <div class="block">
                <p>Помеха на Скрытность:</p>

                <span>{{ armor.disadvantage ? 'Есть' : 'Нет' }}</span>
            </div>

            <div class="block">
                <p>Требование к Силе:</p>

                <span>{{ armor.requirement || 'Нет' }}</span>
            </div>

            <div class="block">
                <p>Надевание / Снятие:</p>

                <span>{{ armor.duration }}</span>
            </div>
        </div>

        <raw-content
            v-if="armor.description"
            :template="armor.description"
        />
    </div>
</template>

<script>
    import RawContent from "@/components/content/RawContent";
    import DiceRoller from "@/components/UI/DiceRoller";
    import DetailTobBar from "@/components/UI/DetailTobBar";

    export default {
        name: "ArmorBody",
        components: { DetailTobBar, DiceRoller, RawContent },
        props: {
            armor: {
                type: Object,
                required: true,
                default: undefined
            },
        }
    }
</script>

<style lang="scss" scoped>
    .armor-body {
        padding: 24px;
    }
</style>
