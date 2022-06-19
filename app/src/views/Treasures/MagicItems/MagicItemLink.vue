<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: magicItem.url }"
        custom
        v-bind="$props"
    >
        <a
            v-bind="$attrs"
            ref="magicItem"
            :class="getClassList(isActive)"
            :href="href"
            class="magic-item-link"
            @click.left.exact.prevent="navigate()"
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
                    </div>
                </div>
            </div>
        </a>
    </router-link>
</template>

<script>
    import { RouterLink } from 'vue-router';
    import { CapitalizeFirst } from '@/common/directives/CapitalizeFirst';
    import { useMagicItemsStore } from "@/store/Treasures/MagicItemsStore";

    export default {
        name: 'MagicItemLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            magicItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            magicItemsStore: useMagicItemsStore(),
        }),
        computed: {
            getRarityAbbreviation() {
                const words = this.magicItem.rarity.split(' ');

                let abbreviation = '';

                for (const word of words) {
                    if (!word) {
                        continue;
                    }

                    abbreviation += word[0].toUpperCase();
                }

                return abbreviation;
            },

            getRarityClass() {
                switch (this.magicItem.rarity) {
                    case 'артефакт':
                        return 'is-artifact'
                    case 'легендарный':
                        return 'is-legendary'
                    case 'очень редкий':
                        return 'is-very-rare'
                    case 'редкий':
                        return 'is-rare'
                    case 'необычный':
                        return 'is-uncommon'
                    case 'обычный':
                        return 'is-common'
                    default:
                        return 'is-unknown'
                }
            }
        },
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-green': this.magicItem?.source?.homebrew,
                    'in-tab': this.inTab
                }
            },
        }
    }
</script>

<style lang="scss" scoped src="./MagicItemLink.scss"/>
