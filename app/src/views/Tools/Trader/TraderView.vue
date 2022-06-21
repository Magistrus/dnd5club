<template>
    <content-layout>
        <template #fixed>
            <form
                class="tools_settings"
                @submit.prevent="sendForm"
            >
                <div class="tools_settings__row">
                    <p>Количество магии в мире:</p>

                    <field-select
                        :options="magicCount"
                        :model-value="magicCountValue"
                        :searchable="false"
                        label="name"
                        track-by="value"
                        @update:model-value="magicCountValue = $event"
                    >
                        <template #placeholder>
                            Количество
                        </template>
                    </field-select>
                </div>

                <div class="tools_settings__row">
                    <label class="select_box">
                        <span>Результат проверки Харизмы (Убеждение):</span>

                        <input
                            v-model="form.persuasion"
                            type="number"
                            class="form-control select"
                            placeholder="Харизма (Убеждение)"
                        >
                    </label>
                </div>

                <div class="tools_settings__row">
                    <field-checkbox
                        :model-value="form.unique"
                        type="toggle"
                        @update:model-value="form.unique = $event"
                    >
                        Только уникальные
                    </field-checkbox>
                </div>

                <div
                    v-if="!form.unique"
                    class="tools_settings__row"
                >
                    <field-checkbox
                        :model-value="settings.grouping"
                        type="toggle"
                        @update:modelValue="settings.grouping = $event"
                    >
                        Группировать одинаковые
                    </field-checkbox>
                </div>

                <div
                    v-if="!form.unique && settings.grouping"
                    class="tools_settings__row"
                >
                    <field-checkbox
                        :model-value="settings.max"
                        type="toggle"
                        @update:modelValue="settings.max = $event"
                    >
                        {{ `Отображать ${settings.max ? 'максимальную' : 'среднюю'} цену` }}
                    </field-checkbox>
                </div>

                <div class="tools_settings__row btn-wrapper">
                    <button
                        id="generate"
                        class="btn btn_primary"
                        type="submit"
                    >
                        Найти торговца
                    </button>
                </div>
            </form>
        </template>

        <template #right-side>
            <section-header
                fullscreen
                :title="selected.item?.name.rus || 'В продаже'"
                :subtitle="selected.item?.name.eng || 'On sale'"
            />

            <div
                v-if="loading && !error"
                class="trader__loader"
            >
                <div class="trader__loader_img">
                    <img
                        alt=""
                        src="/app/img/loader.png"
                    >
                </div>
            </div>

            <div
                v-else-if="error"
                class="trader__err"
            >
                error...
            </div>

            <div
                v-if="!selected.item"
                class="trader__empty"
            >
                <p>Список товаров пуст.</p>
            </div>

            <div
                v-else
                class="trader__content"
            >
                <magic-item-body :magic-item="detailCard.item"/>

                <spell-body
                    v-if="detailCard.spell"
                    :spell="detailCard.spell"
                />
            </div>
        </template>

        <template #default>
            <magic-item-link
                v-for="(item, key) in groupedResults"
                :key="key"
                :magic-item="item"
                :is-active="selected.index === key"
                :to="{path: item.url}"
                @select-item="selectItem(key)"
            />
        </template>
    </content-layout>
</template>

<script>
    import ContentLayout from "@/components/content/ContentLayout";
    import FieldSelect from "@/components/form/FieldType/FieldSelect";
    import SectionHeader from "@/components/UI/SectionHeader";
    import HTTPService from "@/services/HTTPService";
    import errorHandler from "@/helpers/errorHandler";
    import _ from "lodash";
    import FieldCheckbox from "@/components/form/FieldType/FieldCheckbox";
    import MagicItemBody from "@/views/Treasures/MagicItems/MagicItemBody";
    import SpellBody from "@/views/Spells/SpellBody";
    import { reactive } from "vue";
    import MagicItemLink from "@/views/Treasures/MagicItems/MagicItemLink";

    export default {
        name: "TraderView",
        components: {
            MagicItemLink,
            SpellBody,
            MagicItemBody,
            FieldCheckbox, SectionHeader, FieldSelect, ContentLayout
        },
        data: () => ({
            magicCount: [
                {
                    name: 'Мало',
                    value: 1,
                },
                {
                    name: 'Норма',
                    value: 2,
                },
                {
                    name: 'Много',
                    value: 3,
                },
            ],
            form: {
                magicLevel: 1,
                persuasion: 1,
                unique: true
            },
            settings: {
                grouping: true,
                max: false,
            },
            results: [],
            detailCard: {
                item: undefined,
                spell: undefined
            },
            selected: {
                index: undefined,
                item: undefined
            },
            loading: false,
            error: false,
            http: new HTTPService(),
            controllers: {
                list: undefined,
                detail: undefined
            }
        }),
        computed: {
            magicCountValue: {
                get() {
                    return this.magicCount.find(el => el.value === this.form.magicLevel)
                },

                set(e) {
                    this.form.magicLevel = e.value
                }
            },

            groupedResults() {
                if (!this.settings.grouping) {
                    return this.results;
                }

                const groups = _.chain(this.results)
                    .groupBy(o => o.name.rus)
                    .map(item => item)
                    .value();
                const res = [];

                for (const group of groups) {
                    const el = group[0];

                    if (group.length === 1) {
                        res.push(el);

                        continue;
                    }

                    const prices = _.chain(group.map(o => o.price))
                        .sortedUniq()
                        .value();

                    res.push(reactive({
                        ...el,
                        custom: {
                            count: group.length,
                            price: this.settings.max ? _.max(prices) : Math.round(_.mean(prices))
                        }
                    }))
                }

                return res
            }
        },
        methods: {
            // eslint-disable-next-line func-names
            sendForm: _.throttle(function() {
                if (this.controllers.list) {
                    this.controllers.list.abort();
                }

                this.controllers.list = new AbortController();

                this.http.post('/tools/trader', this.form, this.controllers.list.signal)
                    .then(res => {
                        if (res.status !== 200) {
                            errorHandler(res.statusText);

                            return;
                        }

                        this.results = [];

                        this.clearSelected();

                        this.$nextTick(() => {
                            this.results = res.data;
                        })
                    })
                    .catch(err => {
                        errorHandler(err);
                    })
                    .finally(() => {
                        this.controllers.list = undefined;
                    });
            }, 300),

            clearSelected() {
                this.selected.index = undefined;
                this.selected.item = undefined;
                this.detailCard.item = undefined;
                this.detailCard.spell = undefined;
            },

            async selectItem(index) {
                try {
                    if (this.controllers.detail) {
                        this.controllers.detail.abort();
                    }

                    this.error = false;
                    this.loading = true;

                    this.clearSelected();

                    this.controllers.detail = new AbortController();

                    const item = this.groupedResults[index];
                    const resMagicItem = await this.http.post(item.url, null, this.controllers.detail.signal);

                    if (resMagicItem.status !== 200) {
                        this.error = true;
                        this.loading = false;

                        errorHandler(resMagicItem.statusText);

                        return;
                    }

                    this.detailCard.item = resMagicItem.data;
                    this.controllers.detail = new AbortController();

                    if (item.spell?.url) {
                        const resSpell = await this.http.post(item.spell.url, null, this.controllers.detail.signal);

                        if (resSpell.status !== 200) {
                            this.error = true;
                            this.loading = false;

                            errorHandler(resSpell.statusText);

                            return;
                        }

                        this.detailCard.spell = resSpell.data;
                    }

                    this.selected = {
                        index,
                        item
                    };
                } catch (err) {
                    this.error = true;
                    this.loading = false;
                    this.detailCard = {
                        item: undefined,
                        spell: undefined
                    }

                    errorHandler(err);
                } finally {
                    this.loading = false;
                    this.controllers.detail = undefined;
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .tools_settings {
        &__row {
            margin-top: 8px;
        }
    }
</style>
