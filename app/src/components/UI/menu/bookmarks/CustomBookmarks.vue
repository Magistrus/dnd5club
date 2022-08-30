<template>
    <div class="bookmarks is-custom">
        <div class="bookmarks__header">
            <div class="bookmarks__info">
                <span class="bookmarks__info--title">Группы</span>
            </div>

            <label
                v-if="false"
                class="bookmarks__search"
            >
                <span class="bookmarks__search--icon">
                    <svg-icon icon-name="search"/>
                </span>
            </label>
        </div>

        <div class="bookmarks__wrapper">
            <div class="bookmarks__body">
                <custom-bookmark-group
                    v-for="(group, groupKey) in bookmarks"
                    :key="group.uuid + groupKey"
                    :group="group"
                    :is-first="!groupKey"
                />

                <div
                    v-if="isGroupCreating"
                    class="bookmarks__input"
                >
                    <field-input
                        v-model="newGroupName"
                        placeholder="Название группы"
                        autofocus
                        @keyup.enter.exact.prevent="createGroup"
                    />

                    <form-button
                        type-link-filled
                        is-small
                        @click.left.exact.prevent="createGroup"
                    >
                        <svg-icon icon-name="check"/>
                    </form-button>

                    <form-button
                        type-link-filled
                        is-small
                        @click.left.exact.prevent="disableGroupCreating"
                    >
                        <svg-icon icon-name="close"/>
                    </form-button>
                </div>

                <form-button
                    v-else
                    class="bookmarks__new"
                    type-link-filled
                    is-small
                    @click.left.exact.prevent="enableGroupCreating"
                >
                    <svg-icon
                        icon-name="plus"
                        :stroke-enable="false"
                        fill-enable
                    />

                    <span>Группа</span>
                </form-button>
            </div>
        </div>
    </div>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import { useCustomBookmarkStore } from "@/store/UI/bookmarks/CustomBookmarksStore";
    import {
        computed, defineComponent, ref
    } from "vue";
    import CustomBookmarkGroup from "@/components/UI/menu/bookmarks/CustomBookmarks/CustomBookmarkGroup";
    import FieldInput from "@/components/form/FieldType/FieldInput";
    import FormButton from "@/components/form/FormButton";

    export default defineComponent({
        name: "CustomBookmarks",
        components: {
            FormButton,
            FieldInput,
            CustomBookmarkGroup,
            SvgIcon
        },
        setup() {
            const customBookmarkStore = useCustomBookmarkStore();
            const bookmarks = computed(() => customBookmarkStore.getGroupBookmarks);
            const isGroupCreating = ref(false);
            const newGroupName = ref('');

            function enableGroupCreating() {
                isGroupCreating.value = true;
                newGroupName.value = '';
            }

            function disableGroupCreating() {
                isGroupCreating.value = false;
                newGroupName.value = '';
            }

            async function createGroup() {
                await customBookmarkStore.queryAddBookmark({
                    name: newGroupName.value,
                    order: customBookmarkStore.getGroupBookmarks.length
                });

                disableGroupCreating();
            }

            return {
                bookmarks,
                isGroupCreating,
                newGroupName,
                enableGroupCreating,
                disableGroupCreating,
                createGroup
            };
        }
    });
</script>
