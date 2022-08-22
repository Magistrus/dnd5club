<template>
    <form-button
        v-tippy="{ content: 'Добавить в закладки' }"
        class="default-bookmark-button"
        type-link-filled
        @click.left.exact.prevent.stop="updateBookmark"
    >
        <svg-icon
            :icon-name="isSaved ? 'bookmark-filled' : 'bookmark'"
            :stroke-enable="false"
            fill-enable
        />
    </form-button>
</template>

<script>
    import FormButton from "@/components/form/FormButton";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import { useRoute } from "vue-router";
    import {
        computed,
        defineComponent,
        unref
    } from "vue";

    export default defineComponent({
        components: {
            FormButton
        },
        props: {
            name: {
                type: String,
                default: ''
            },
            url: {
                type: String,
                default: ''
            }
        },
        setup(props) {
            const route = useRoute();
            const defaultBookmarkStore = useDefaultBookmarkStore();
            const path = computed(() => (typeof props.url === "string" && props.url !== '' ? props.url : route.path));
            const isSaved = computed(() => defaultBookmarkStore.isBookmarkSaved(unref(path)));

            async function updateBookmark() {
                await defaultBookmarkStore.updateBookmark(unref(path), unref(props.name));
            }

            return {
                isSaved,
                updateBookmark,
                path
            };
        }
    });
</script>

<style lang="scss" scoped>
    .default-bookmark-button {
        width: 28px;
        z-index: 1;

        &:hover {
            position: relative;
            z-index: 2;
        }
    }
</style>
