<template>
    <div class="section-header">
        <div class="section-header__body">
            <div class="section-header__title">
                <div class="section-header__title--text">
                    {{ title }}
                </div>

                <a
                    v-if="copy"
                    v-tippy="{ content: 'Скопировать ссылку' }"
                    :href="urlForCopy"
                    class="section-header__title--copy"
                    @click.left.exact.prevent.stop="copyText"
                >
                    <svg-icon icon-name="copy"/>
                </a>
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
                    v-tippy="{ content: 'Открыть окно печати' }"
                    class="section-header__control--optional is-only-desktop"
                    type="button"
                    @click.left.exact.prevent.stop="openPrintWindow"
                >
                    <svg-icon icon-name="print"/>
                </button>

                <button
                    v-if="onExportFoundry"
                    v-tippy="{ content: 'Экспорт в Foundry VTT' }"
                    class="section-header__control--optional is-only-desktop"
                    type="button"
                    @click.left.exact.prevent.stop="$emit('exportFoundry')"
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
                    v-tippy="{
                        content: uiStore.getFullscreen
                            ? 'Свернуть окно'
                            : 'Развернуть окно'
                    }"
                    class="section-header__control--main is-only-desktop"
                    type="button"
                    @click.left.exact.prevent.stop="uiStore.setFullscreenState(!uiStore.getFullscreen)"
                >
                    <svg-icon :icon-name="uiStore.getFullscreen ? 'exit-fullscreen' : 'fullscreen'"/>
                </button>

                <button
                    v-if="closeAvailable"
                    v-tippy="{ content: 'Закрыть' }"
                    class="section-header__control--main"
                    type="button"
                    @click.left.exact.prevent.stop="$emit('close')"
                >
                    <svg-icon icon-name="close"/>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
    import SvgIcon from '@/components/UI/SvgIcon';
    import { useUIStore } from '@/store/UI/UIStore';
    import errorHandler from "@/common/helpers/errorHandler";

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
                type: Boolean,
                default: false
            },
            print: {
                type: Boolean,
                default: false
            },
            fullscreen: {
                type: Boolean,
                default: false
            },
            closeOnDesktop: {
                type: Boolean,
                default: false,
            },
            onClose: {
                type: Function,
                default: null
            },
            onExportFoundry: {
                type: Function,
                default: null
            },
        },
        data: () => ({
            uiStore: useUIStore()
        }),
        computed: {
            hasOptionalControls() {
                return !!this.print || !!this.onExportFoundry;
            },

            hasMainControls() {
                return !!this.onClose || !!this.fullscreen;
            },

            hasControls() {
                return !!this.hasOptionalControls || !!this.hasMainControls
            },

            urlForCopy() {
                return window.location.origin + window.location.pathname;
            },

            closeAvailable() {
                if (!this.uiStore.getIsMobile) {
                    return this.closeOnDesktop;
                }

                return this.onClose;
            }
        },
        methods: {
            async copyText() {
                if (navigator.clipboard) {
                    try {
                        await navigator.clipboard.writeText(this.urlForCopy);

                        return;
                    } catch (err) {
                        errorHandler(err)
                    }
                }

                const field = document.body.appendChild(document.createElement('input'));

                field.value = this.urlForCopy;

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
        justify-content: flex-end;
        flex-wrap: nowrap;
        overflow: hidden;
        flex-shrink: 0;
        background-color: var(--bg-sub-menu);
        border-bottom: 1px solid var(--border);

        &__body {
            padding: 0 16px;
            flex: 1;

            @media (min-width: 1200px) {
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

                @media (max-width: 800px) {
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

                @media (min-width: 800px) {
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

            @media (max-width: 1200px) {
                margin-top: 4px;
            }
        }

        &__controls {
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
                width: 73px;
                height: 72px;

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
                flex: 1 0 72px;
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
