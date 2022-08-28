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
    import { computed, defineComponent } from "vue";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import { useUserStore } from "@/store/UI/UserStore";

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
            const userStore = useUserStore();
            const defaultBookmarkStore = useDefaultBookmarkStore();
            const customBookmarkStore = useCustomBookmarkStore();
            const getPath = () => (typeof props.url === "string" && props.url !== '' ? props.url : route.path);
            const isSaved = computed(() => defaultBookmarkStore.isBookmarkSaved(getPath()));

            async function updateBookmark() {
                if (await userStore.getUserStatus()) {
                    const defaultGroup = await customBookmarkStore.getDefaultGroup();

                    console.log(defaultGroup);

                    await customBookmarkStore.updateBookmarkInGroup({
                        url: getPath(),
                        name: props.name,
                        groupUUID: defaultGroup.uuid
                    });

                    return;
                }

                await defaultBookmarkStore.updateBookmark(getPath(), props.name);
            }

            return {
                isSaved,
                updateBookmark
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
