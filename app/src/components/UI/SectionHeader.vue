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
                <bookmark-save-button
                    v-if="bookmark"
                    :name="title"
                    :url="url"
                />

                <ui-button
                    v-if="print"
                    v-tippy="{ content: 'Открыть окно печати' }"
                    class="section-header__control--optional is-only-desktop"
                    type-link-filled
                    is-icon
                    @click.left.exact.prevent.stop="openPrintWindow"
                >
                    <svg-icon icon-name="print"/>
                </ui-button>

                <ui-button
                    v-if="onExportFoundry"
                    v-tippy="{ content: 'Импорт в Foundry VTT. <a href=&quot;/fvtt_import&quot;>Инструкция</a>' }"
                    class="section-header__control--optional is-only-desktop"
                    type-link-filled
                    is-icon
                    @click.left.exact.prevent.stop="$emit('exportFoundry')"
                >
                    <svg-icon icon-name="export-foundry"/>
                </ui-button>
            </div>

            <div
                v-if="hasMainControls"
                class="section-header__controls--main"
            >
                <button
                    v-if="fullscreen"
                    v-tippy="{
                        content: uiStore.fullscreen
                            ? 'Свернуть окно'
                            : 'Развернуть окно',
                    }"
                    class="section-header__control--main is-only-desktop"
                    type="button"
                    @click.left.exact.prevent.stop="uiStore.setFullscreenState(!uiStore.fullscreen)"
                >
                    <svg-icon :icon-name="uiStore.fullscreen ? 'exit-fullscreen' : 'fullscreen'"/>
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

<script lang="jsx">
    import { useClipboard } from "@vueuse/core";
    import { useUIStore } from '@/store/UI/UIStore';
    import BookmarkSaveButton from "@/components/UI/menu/bookmarks/buttons/BookmarkSaveButton";
    import UiButton from "@/components/form/UiButton";

    export default {
        name: 'SectionHeader',
        components: {
            UiButton,
            BookmarkSaveButton
        },
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
            bookmark: {
                type: Boolean,
                default: false
            },
            url: {
                type: String,
                default: ''
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
                default: false
            },
            onClose: {
                type: Function,
                default: null
            },
            onExportFoundry: {
                type: Function,
                default: null
            }
        },
        data: () => ({
            uiStore: useUIStore(),
            clipboard: useClipboard()
        }),
        computed: {
            hasOptionalControls() {
                return !!this.bookmark || !!this.print || !!this.onExportFoundry;
            },

            hasMainControls() {
                return !!this.onClose || !!this.fullscreen;
            },

            hasControls() {
                return !!this.hasOptionalControls || !!this.hasMainControls;
            },

            urlForCopy() {
                return window.location.origin + this.$route.path;
            },

            closeAvailable() {
                if (!this.uiStore.isMobile) {
                    return this.closeOnDesktop;
                }

                return this.onClose;
            }
        },
        methods: {
            copyText() {
                if (!this.clipboard.isSupported) {
                    this.$toast.error('Ваш браузер не поддерживает копирование');
                }

                this.clipboard.copy(this.urlForCopy)
                    .then(() => this.$toast('Ссылка успешно скопирована'))
                    .catch(() => this.$toast.error((
                      <span>
                        Произошла какая-то ошибка... попробуйте еще раз или обратитесь за помощью на нашем
                        <a
                          target="_blank"
                          href="https://discord.gg/zqBnMJVf3z"
                          rel="noopener"
                        >
                          Discord-канале
                        </a>
                      </span>
                    )));
            },

            openPrintWindow() {
                window.print();
            }
        }
    };
</script>

<style lang="scss" scoped>
    .section-header {
        width: 100%;
        height: 72px;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        flex-wrap: nowrap;
        flex-shrink: 0;
        background-color: var(--bg-sub-menu);
        border-bottom: 1px solid var(--border);

        &__body {
            padding: 0 16px;
            flex: 1;
            min-width: 0;

            @media (min-width: 1200px) {
                padding-left: 24px;
            }
        }

        &__title {
            display: flex;
            align-items: center;

            &--text {
                font-size: var(--h4-font-size);
                line-height: var(--h4-line-height);
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                position: relative;
                color: var(--text-color-title);
                font-weight: 400;
            }

            &--copy {
                @include css_anim();

                display: none;
                align-items: center;
                justify-content: center;
                width: 32px;
                height: 32px;
                margin-left: 6px;
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

            &--optional {
                padding-right: 8px;
            }
        }

        &__control {
            &--main,
            &--optional {
                &.is-only-desktop {
                    display: none;
                }

                @include media-min($lg) {
                    &.is-only-desktop {
                        display: flex;
                    }
                }
            }

            &--main {
                @include css_anim();

                display: flex;
                justify-content: center;
                align-items: center;
                flex-shrink: 0;
                cursor: pointer;
                color: var(--primary);
                width: 73px;
                height: 72px;

                @include media-min($md) {
                    &:hover {
                        background-color: var(--primary-hover);
                        color: var(--text-btn-color);
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
                & + & {
                    margin-left: 8px;
                }
            }
        }
    }
</style>
