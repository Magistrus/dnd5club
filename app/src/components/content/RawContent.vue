<template>
    <div class="raw-content">
        <suspense>
            <component :is="component"/>

            <template #fallback>
                <div class="raw-content__error">
                    Загрузка...
                </div>
            </template>
        </suspense>
    </div>
</template>

<script>
    import defineRawComponent from "@/utils/DefineRawComponent";

    export default {
        name: "RawContent",
        props: {
            template: {
                type: String,
                default: undefined
            },
            url: {
                type: String,
                default: undefined
            }
        },
        data: () => ({
            component: undefined,
            error: false
        }),
        watch: {
            async url() {
                await this.initComponent();
            }
        },
        async mounted() {
            await this.initComponent();
        },
        methods: {
            async initComponent() {
                try {
                    this.component = await defineRawComponent(this.template, this.url)
                } catch (err) {
                    this.error = true;
                }
            }
        }
    }
</script>
