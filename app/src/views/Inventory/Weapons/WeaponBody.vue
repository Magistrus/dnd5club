<template>
    <div
        v-if="weapon"
        class="weapon-body"
        :class="{ 'in-tooltip': inTooltip }"
    >
        <detail-top-bar
            :bg-grey="false"
            :left="weapon.type.name"
            :source="weapon.source"
        />

        <div class="grid_stat_block">
            <div class="block">
                <p>Стоимость: </p>

                <span>{{ weapon.price ? weapon.price : '—' }}</span>
            </div>

            <div class="block">
                <p>Урон: </p>

                <dice-roller :formula="weapon.damage.dice">
                    {{ weapon.damage.dice }}
                </dice-roller> <span>{{ weapon.damage.type }}</span>
            </div>

            <div class="block">
                <p>Вес (в фунтах): </p>

                <span>{{ weapon.weight }}</span>
            </div>

            <div
                v-if="weapon.properties?.length"
                class="block"
            >
                <p>Свойства: </p>

                <span
                    v-for="(property, propKey) in weapon.properties"
                    :key="propKey"
                >
                    <a
                        v-tippy="{ content: property.description }"
                        :href="property.url"
                    >{{ property.name }}</a>

                    <span
                        v-if="property.twoHandDice"
                        v-tippy="{ content: 'При атаке двумя руками' }"
                    > ({{ property.twoHandDice }})</span>

                    <span v-if="property.distance"> (дис. {{ property.distance }})</span>

                    <span v-if="propKey !== weapon.properties.length - 1">, </span>
                </span>
            </div>
        </div>

        <div class=" content-padding">
            <raw-content
                v-if="weapon.description"
                :template="weapon.description"
            />

            <p v-if="weapon.special">
                <strong>Особое свойство:</strong>
                <raw-content :template="weapon.special"/>
            </p>
        </div>
    </div>
</template>

<script>
    import RawContent from "@/components/content/RawContent";
    import DiceRoller from "@/components/UI/DiceRoller";
    import DetailTopBar from "@/components/UI/DetailTopBar";

    export default {
        name: "WeaponBody",
        components: {
            DetailTopBar,
            DiceRoller,
            RawContent
        },
        props: {
            weapon: {
                type: Object,
                required: true,
                default: undefined
            },
            inTooltip: {
                type: Boolean,
                default: false
            }
        }
    };
</script>
