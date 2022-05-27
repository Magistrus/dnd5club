<template>
    <div class="section-header">
        <div class="section-header__body">
            <div class="section-header__title">
                <div class="section-header__title--text">
                    {{ title }}
                </div>

                <button
                    v-if="copy"
                    class="section-header__title--copy"
                    @click.left.exact.prevent.stop="copyText"
                >
                    <svg-icon icon-name="copy"/>
                </button>
            </div>

            <div
                v-if="subtitle"
                class="section-header__subtitle"
            >
                {{ subtitle }}
            </div>
        </div>

        <div
            v-if="hasControls"
            class="section-header__controls"
        >
            <div
                v-if="hasOptionalControls"
                class="section-header__controls--optional"
            >
                <button
                    v-if="print"
                    class="section-header__control--optional is-only-desktop"
                    type="button"
                    @click.left.exact.prevent.stop="openPrintWindow"
                >
                    <svg-icon icon-name="print"/>
                </button>

                <button
                    v-if="exportFoundry"
                    class="section-header__control--optional is-only-desktop"
                    type="button"
                >
                    <svg-icon icon-name="export-foundry"/>
                </button>
            </div>

            <div
                v-if="hasMainControls"
                class="section-header__controls--main"
            >
                <button
                    v-if="fullscreen"
                    class="section-header__control--main is-only-desktop"
                    type="button"
                    @click.left.exact.prevent="uiStore.setFullscreenState(!uiStore.getContentConfig.fullscreen)"
                >
                    <svg-icon :icon-name="uiStore.getContentConfig.fullscreen ? 'exit-fullscreen' : 'fullscreen'"/>
                </button>

                <button
                    v-if="close"
                    class="section-header__control--main"
                    type="button"
                    @click.left.exact.prevent.stop="close()"
                >
                    <svg-icon icon-name="close"/>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useUIStore } from '@/store/UIStore/UIStore';

    export default {
        name: 'SectionHeader',
        components: { SvgIcon },
        props: {
            title: {
                type: String,
                required: true
            },
            subtitle: {
                type: String,
                default: ''
            },
            copy: {
                type: String,
                default: ''
            },
            print: {
                type: Boolean,
                default: false
            },
            exportFoundry: {
                type: Boolean,
                default: false
            },
            fullscreen: {
                type: Boolean,
                default: false
            },
            close: {
                type: Function,
                default: undefined
            }
        },
        data: () => ({
            uiStore: useUIStore()
        }),
        computed: {
            hasOptionalControls() {
                return !!this.print || !!this.exportFoundry;
            },

            hasMainControls() {
                return !!this.close || !!this.fullscreen;
            },

            hasControls() {
                return !!this.hasOptionalControls || !!this.hasMainControls
            }
        },
        methods: {
            async copyText() {
                if (navigator.clipboard) {
                    try {
                        await navigator.clipboard.writeText(this.copy);

                        return;
                    } catch (err) {
                        console.error(err)
                    }
                }

                const field = document.body.appendChild(document.createElement('input'));

                field.value = this.copy;

                field.focus();
                field.select();

                document.execCommand('copy');

                field.parentNode.removeChild(field);
            },

            openPrintWindow() {
                window.print()
            },
        }
    }
</script>

<style lang="scss" scoped>
    .section-header {
        width: 100%;
        height: 72px;
        display: flex;
        align-items: center;
        overflow: hidden;
        flex-shrink: 0;
        background-color: var(--bg-sub-menu);

        &__body {
            padding: 0 16px;
            flex: 1 1 100%;

            @include media-min($md) {
                padding-left: 24px;
            }
        }

        &__title {
            display: flex;
            align-items: center;
            width: fit-content;

            &--text {
                font-size: calc(var(--h1-font-size) - 12px);
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                position: relative;
                color: var(--text-color-title);
                font-weight: 400;

                @include media-min($md) {
                    font-size: calc(var(--h1-font-size) - 16px);
                }
            }

            &--copy {
                @include css_anim();

                display: none;
                align-items: center;
                justify-content: center;
                width: 32px;
                height: 32px;
                margin-left: 16px;
                padding: 4px;
                border-radius: 8px;
                color: var(--primary);
                background-color: transparent;
                cursor: pointer;
                flex-shrink: 0;
                transform: translateY(2px);

                @include media-min($md) {
                    display: flex;

                    &:hover {
                        background-color: var(--primary-hover);
                        color: var(--text-btn-color);
                    }
                }
            }
        }

        &__subtitle {
            font-size: calc(var(--h2-font-size) - 14px);
            color: var(--text-g-color);
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        &__controls {
            margin-left: auto;
            display: flex;
            align-items: center;
            flex-shrink: 0;

            &--main,
            &--optional {
                display: flex;
                align-items: center;
                flex-shrink: 0;
            }
        }

        &__control {
            &--main,
            &--optional {
                @include css_anim();

                display: flex;
                justify-content: center;
                align-items: center;
                flex-shrink: 0;
                cursor: pointer;
                color: var(--primary);

                &.is-only-desktop {
                    display: none;
                }

                @include media-min($md) {
                    &:hover {
                        background-color: var(--primary-hover);
                        color: var(--text-btn-color);
                    }
                }

                @include media-min($lg) {
                    &.is-only-desktop {
                        display: flex;
                    }
                }
            }

            &--main {
                background-color: var(--bg-secondary);
                border-left: 1px solid var(--border);
                height: 72px;
                padding: 24px;
                flex: 1 0 73px;
            }

            &--optional {
                width: 48px;
                height: 48px;
                padding: 12px;
                border-radius: 8px;
                background-color: transparent;
                flex: 1 0 48px;
                margin-right: 8px;
            }
        }
    }
</style>
