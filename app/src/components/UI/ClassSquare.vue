<template>
    <a
        v-if="icon && url"
        v-tippy="{ content: name }"
        :href="url"
        class="class-square"
    >
        <svg-icon
            :icon-name="icon"
            :stroke-enable="false"
            fill-enable
        />
    </a>
</template>

<script>
    import SvgIcon from "@/components/UI/icons/SvgIcon";

    export default {
        name: "ClassSquare",
        components: {
            SvgIcon
        },
        props: {
            name: {
                type: String,
                default: ''
            },
            icon: {
                type: String,
                default: ''
            },
            url: {
                type: String,
                default: ''
            }
        }
    };
</script>

<style lang="scss" scoped>
    // .class-square {
    //     @include css_anim();

    //     display: block;
    //     width: 48px;
    //     height: 48px;
    //     padding: 8px;
    //     background-repeat: no-repeat;
    //     background-position: center;
    //     background-color: var(--hover);
    //     border-radius: 8px;
    //     margin-right: 8px;
    //     cursor: pointer;
    //     color: var(--primary);

    //     &:hover {
    //         color: var(--link-color-hover);
    //     }
    // }
</style>
