<template>
    <form-button
        v-tippy="{ content: 'Добавить в закладки' }"
        class="bookmark-save-button"
        type-link-filled
        @click.left.exact.prevent.stop="updateDefaultBookmark($route.path, name)"
    >
        <svg-icon
            :icon-name="isDefaultBookmarkSaved($route.path) ? 'bookmark-filled' : 'bookmark'"
            :stroke-enable="false"
            fill-enable
        />
    </form-button>

    <div
        v-if="isAuthorized"
        class="bookmark-submenu-button"
    >
        <form-button
            v-tippy="{ content: 'Добавить в закладки' }"
            type-link-filled
            @click.left.exact.prevent.stop="isOpen = !isOpen"
        >
            <svg-icon
                icon-name="arrow-2"
                :stroke-enable="false"
                fill-enable
            />
        </form-button>
    </div>
</template>

<script setup>
    import FormButton from "@/components/form/FormButton";
    import { useDefaultBookmarkStore } from "@/store/UI/bookmarks/DefaultBookmarkStore";
    import { useUserStore } from "@/store/UI/UserStore";
    import { onBeforeMount, ref } from "vue";

    defineProps({
        name: {
            type: String,
            default: ''
        }
    });

    const { isAuthorized, getUserStatus } = useUserStore();
    const {
        isBookmarkSaved: isDefaultBookmarkSaved,
        updateBookmark: updateDefaultBookmark

    } = useDefaultBookmarkStore();
    const isOpen = ref(false);

    onBeforeMount(async () => {
        await getUserStatus();
    });
</script>

<style lang="scss" scoped>
    .bookmark-save-button,
    .bookmark-submenu-button > .form-button {
        z-index: 1;

        &:hover {
            position: relative;
            z-index: 2;
        }
    }

    .bookmark-save-button {
        width: 28px;
    }

    .bookmark-submenu-button {
        .form-button {
            margin-left: -4px !important;
            width: 18px;
            padding: 12px 0;
        }
    }
</style>
