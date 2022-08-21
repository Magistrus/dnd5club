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
                />

                <form-button
                    v-if="print"
                    v-tippy="{ content: 'Открыть окно печати' }"
                    class="section-header__control--optional is-only-desktop"
                    type-link-filled
                    @click.left.exact.prevent.stop="openPrintWindow"
                >
                    <svg-icon icon-name="print"/>
                </form-button>

                <form-button
                    v-if="onExportFoundry"
                    v-tippy="{ content: 'Экспорт в Foundry VTT' }"
                    class="section-header__control--optional is-only-desktop"
                    type-link-filled
                    @click.left.exact.prevent.stop="$emit('exportFoundry')"
                >
                    <svg-icon icon-name="export-foundry"/>
                </form-button>
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
    import { useUIStore } from '@/store/UI/UIStore';
    import errorHandler from "@/common/helpers/errorHandler";
    import BookmarkSaveButton from "@/components/UI/menu/bookmarks/buttons/BookmarkSaveButton";
    import FormButton from "@/components/form/FormButton";

    export default {
        name: 'SectionHeader',
        components: {
            FormButton,
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
            uiStore: useUIStore()
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
                if (!this.uiStore.getIsMobile) {
                    return this.closeOnDesktop;
                }

                return this.onClose;
            }
        },
        watch: {
            title(value) {
                document.title = `${ value } | DnD5 Club`;
            }
        },
        methods: {
            async copyText() {
                if (navigator.clipboard) {
                    try {
                        await navigator.clipboard.writeText(this.urlForCopy);

                        return;
                    } catch (err) {
                        errorHandler(err);
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
        overflow: hidden;
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
