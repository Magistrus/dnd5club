<template>
    <a
        ref="magicItem"
        :class="getClassList(isActive)"
        :href="magicItem.url"
        class="magic-item-link"
        @click.left.exact.prevent="$emit('select-item')"
    >
        <div class="magic-item-link__content">
            <div
                class="magic-item-link__rarity"
                :class="getRarityClass"
            >
                <span>{{ getRarityAbbreviation }}</span>
            </div>

            <div class="magic-item-link__body">
                <div class="magic-item-link__row">
                    <div class="magic-item-link__name">
                        <div class="magic-item-link__name--rus">
                            {{ magicItem.name.rus }}
                        </div>

                        <div class="magic-item-link__name--eng">
                            [{{ magicItem.name.eng }}]
                        </div>
                    </div>
                </div>

                <div class="magic-item-link__row">
                    <div
                        v-capitalize-first
                        class="magic-item-link__type"
                    >
                        {{ magicItem.type.name }}
                    </div>

                    <div
                        v-if="magicItem.custom?.count"
                        class="magic-item-link__count"
                    >
                        {{ `x${ magicItem.custom.count }` }}
                    </div>

                    <div class="magic-item-link__price">
                        {{ `${ magicItem.custom?.price || magicItem.price || 0 } зм` }}
                    </div>
                </div>
            </div>
        </div>
    </a>
</template>

<script>
    import MagicItemLink from "@/views/Treasures/MagicItems/MagicItemLink";

    export default {
        name: "TreasuryMagicItemLink",
        extends: MagicItemLink,
        props: {
            isActive: {
                type: Boolean,
                default: false
            },
            onSelectItem: {
                type: Function,
                default: null
            }
        },
    }
</script>

<style lang="scss" scoped>
    @import "@/views/Treasures/MagicItems/MagicItemLink.scss";

    .magic-item-link {
        &__count,
        &__price {
            margin-left: auto;
            display: block;
        }
    }
</style>
