<template>
    <div class="raw-content">
        <div
            v-if="loading && !error"
            class="raw-content__loading"
        >
            Загрузка...
        </div>

        <div
            v-else-if="error || (!loading && !component)"
            class="raw-content__loading"
        >
            Ошибка...
        </div>

        <component
            :is="component"
            v-else
        />
    </div>
</template>

<script>
    import defineRawComponent from "@/common/utils/DefineRawComponent";
    import { shallowRef } from "vue";

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
            error: false,
            loading: true
        }),
        watch: {
            async template() {
                await this.initComponent();
            },

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
                    this.error = false;
                    this.loading = true;

                    const component = await defineRawComponent(this.template, this.url);

                    this.component = shallowRef(component);
                    this.loading = false;
                } catch (err) {
                    this.error = true;
                    this.loading = false;
                }
            }
        }
    }
</script>
