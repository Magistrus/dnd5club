<template>
    <router-link
        v-slot="{ href, navigate, isActive }"
        :to="{ path: optionItem.url }"
        custom
        v-bind="$props"
    >
        <a
            ref="optionItem"
            :class="getClassList(isActive)"
            :href="href"
            class="link-item"
            v-bind="$attrs"
            @click.left.exact.prevent="navigate()"
        >
            <div class="link-item__content">
                <div class="link-item__body">
                    <div class="link-item__row">
                        <div class="link-item__name">
                            <div class="link-item__name--rus">
                                {{ optionItem.name.rus }}
                            </div>

                            <div class="link-item__name--eng">
                                [{{ optionItem.name.eng }}]
                            </div>
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
    import { useOptionsStore } from "@/store/Character/OptionsStore";

    export default {
        name: 'OptionLink',
        directives: {
            CapitalizeFirst
        },
        inheritAttrs: false,
        props: {
            ...RouterLink.props,
            optionItem: {
                type: Object,
                default: () => ({})
            },
            inTab: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            optionsStore: useOptionsStore(),
            option: {
                show: false,
                data: undefined
            }
        }),
        methods: {
            getClassList(isActive) {
                return {
                    'router-link-active': isActive,
                    'is-option-selected': this.$route.name === 'optionDetail',
                    'is-green': this.optionItem?.homebrew
                }
            },

            clickHandler(callback) {
                if (!this.inTab) {
                    callback();

                    return;
                }

                this.optionsStore.optionInfoQuery(this.optionItem.url)
                    .then(spell => {
                        this.option = {
                            show: true,
                            data: spell
                        }
                    });
            }
        }
    }
</script>

<style lang="scss" scoped src="../../../assets/styles/link-item.scss"/>
