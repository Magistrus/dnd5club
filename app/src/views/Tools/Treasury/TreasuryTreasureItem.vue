<template>
    <div
        ref="treasure"
        :class="{ 'is-green': treasure?.source?.homebrew, 'in-tab': inTab}"
        class="treasure-link"
    >
        <div class="treasure-link__body">
            <div class="treasure-link__row">
                <div class="treasure-link__name">
                    {{ treasure.name.rus }}
                </div>
            </div>

            <div class="treasure-link__row">
                <div
                    v-tooltip="{ content: treasure.source.name }"
                    class="treasure-link__source"
                >
                    {{ treasure.source.shortName }}
                </div>

                <div
                    v-if="treasure.custom?.count"
                    class="treasure-link__count"
                >
                    x{{ treasure.custom.count }}
                </div>

                <div class="treasure-link__price">
                    {{ treasure.custom?.price ||treasure.price || 0 }} зм
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import TreasureItem from "@/views/Treasures/Treasures/TreasureItem";

    export default {
        name: 'TreasuryTreasureItem',
        extends: TreasureItem,
    }
</script>

<style lang="scss" scoped>
    @import "../../Treasures/Treasures/TreasureItem";

    .treasure-link {
        &__count {
            margin-left: auto;
        }
    }
</style>
