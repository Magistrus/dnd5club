<template>
    <content-layout>
        <template #fixed>
            <form
                class="tools_settings"
                @submit.prevent="sendForm"
            >
                <div class="tools_settings__row">
                    <p>Показатель опасности монстров:</p>

                    <field-select
                        :options="crList"
                        :model-value="crValue"
                        :searchable="false"
                        label="name"
                        track-by="value"
                        @update:model-value="crValue = $event"
                    >
                        <template #placeholder>
                            Показатель опасности
                        </template>
                    </field-select>
                </div>

                <div class="tools_settings__row">
                    <field-checkbox
                        :model-value="form.coins"
                        type="toggle"
                        @update:model-value="form.coins = $event"
                    >
                        Монеты
                    </field-checkbox>
                </div>

                <div class="tools_settings__row">
                    <field-checkbox
                        :model-value="form.magicItem"
                        type="toggle"
                        @update:model-value="form.magicItem = $event"
                    >
                        Магические предметы
                    </field-checkbox>
                </div>

                <div class="tools_settings__row">
                    <field-checkbox
                        :model-value="form.scroll"
                        type="toggle"
                        @update:model-value="form.scroll = $event"
                    >
                        Свитки
                    </field-checkbox>
                </div>

                <div class="tools_settings__row">
                    <field-checkbox
                        :model-value="form.trinket"
                        type="toggle"
                        @update:model-value="form.trinket = $event"
                    >
                        Безделушки
                    </field-checkbox>
                </div>

                <div class="tools_settings__row">
                    <field-checkbox
                        :model-value="form.art"
                        type="toggle"
                        @update:model-value="form.art = $event"
                    >
                        Предметы искусства
                    </field-checkbox>
                </div>

                <div class="tools_settings__row">
                    <field-checkbox
                        :model-value="form.gem"
                        type="toggle"
                        @update:model-value="form.gem = $event"
                    >
                        Драгоценные камни
                    </field-checkbox>
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
                        @update:model-value="settings.grouping = $event"
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
                        @update:model-value="settings.max = $event"
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
                        Создать сокровищницу
                    </button>
                </div>
            </form>
        </template>

        <template #right-side>
            <section-header
                fullscreen
                :title="selected.item?.name.rus || 'В сокровищнице'"
                :subtitle="selected.item?.name.eng || 'In treasury'"
            />

            <div
                v-if="loading && !error"
                class="treasury__loader"
            >
                <div class="treasury__loader_img">
                    <img
                        alt=""
                        src="/app/img/loader.png"
                    >
                </div>
            </div>

            <div
                v-else-if="error"
                class="treasury__err"
            >
                error...
            </div>

            <div
                v-if="!selected.item"
                class="treasury__empty"
            >
                <p>Список товаров пуст.</p>
            </div>

            <div
                v-else
                class="treasury__content"
            >
                <magic-item-body :magic-item="detailCard.item"/>

                <spell-body
                    v-if="detailCard.spell"
                    :spell="detailCard.spell"
                />
            </div>
        </template>

        <template #default>
            <div
                v-if="groupedResult.coins"
                class="treasury-group"
            >
                <h4 class="header_separator">
                    <span>Магические предметы</span>
                </h4>

                <table class="table">
                    <tbody>
                        <tr>
                            <td>{{ groupedResult.coins.copper || 0 }} мм</td>
                            <td>{{ groupedResult.coins.silver || 0 }} см</td>
                            <td>{{ groupedResult.coins.electrum || 0 }} эм</td>
                            <td>{{ groupedResult.coins.gold || 0 }} зм</td>
                            <td>{{ groupedResult.coins.platinum || 0 }} пм</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div
                v-if="groupedResult.magicItems?.length"
                class="treasury-group"
            >
                <h4 class="header_separator">
                    <span>Магические предметы</span>
                </h4>

                <magic-item-link
                    v-for="(item, key) in groupedResult.magicItems"
                    :key="key"
                    :magic-item="item"
                    :is-active="selected.index === key"
                    :to="{path: item.url}"
                    @select-item="selectItem('magicItems', key)"
                />
            </div>

            <div
                v-if="groupedResult.gems?.length"
                class="treasury-group"
            >
                <h4 class="header_separator">
                    <span>Драгоценные камни</span>
                </h4>

                <treasure-item
                    v-for="(item, key) in groupedResult.gems"
                    :key="key"
                    :treasure="item"
                />
            </div>

            <div
                v-if="groupedResult.arts?.length"
                class="treasury-group"
            >
                <h4 class="header_separator">
                    <span>Предметы искусства</span>
                </h4>

                <treasure-item
                    v-for="(item, key) in groupedResult.arts"
                    :key="key"
                    :treasure="item"
                />
            </div>

            <div
                v-if="groupedResult.trinkets?.length"
                class="treasury-group"
            >
                <h4 class="header_separator">
                    <span>Безделушки</span>
                </h4>

                <treasure-item
                    v-for="(item, key) in groupedResult.trinkets"
                    :key="key"
                    :treasure="item"
                />
            </div>
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
    import TreasureItem from "@/views/Treasures/Treasures/TreasureItem";
    import MagicItemLink from "@/views/Treasures/MagicItems/MagicItemLink";

    export default {
        name: "TreasuryView",
        components: {
            MagicItemLink,
            TreasureItem,
            SpellBody,
            MagicItemBody,
            FieldCheckbox, SectionHeader, FieldSelect, ContentLayout
        },
        data: () => ({
            crList: [
                {
                    name: '0-4',
                    value: 1,
                },
                {
                    name: '5-10',
                    value: 2,
                },
                {
                    name: '11-15',
                    value: 3,
                },
                {
                    name: '17+',
                    value: 4,
                },
            ],
            form: {
                cr: 1,
                coins: true,
                magicItem: true,
                scroll: true,
                trinket: true,
                art: true,
                gem: true,
                unique: true
            },
            settings: {
                grouping: true,
                max: false,
            },
            result: {},
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
            crValue: {
                get() {
                    return this.crList.find(el => el.value === this.form.cr)
                },

                set(e) {
                    this.form.cr = e.value
                }
            },

            groupedResult() {
                if (!this.settings.grouping) {
                    return this.result;
                }

                const result = {};

                for (const [key, value] of Object.entries(this.result)) {
                    if (!value || !Array.isArray(value)) {
                        result[key] = value;

                        continue;
                    }

                    const groups = _.chain(value)
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

                    result[key] = res;
                }

                return result
            }
        },
        methods: {
            // eslint-disable-next-line func-names
            sendForm: _.throttle(function() {
                if (this.controllers.list) {
                    this.controllers.list.abort();
                }

                this.controllers.list = new AbortController();

                this.http.post('/tools/treasury', this.form, this.controllers.list.signal)
                    .then(res => {
                        if (res.status !== 200) {
                            errorHandler(res.statusText);

                            return;
                        }

                        this.result = [];

                        this.clearSelected();

                        this.$nextTick(() => {
                            this.result = res.data;
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

            async selectItem(group, index) {
                try {
                    if (this.controllers.detail) {
                        this.controllers.detail.abort();
                    }

                    this.error = false;
                    this.loading = true;

                    this.clearSelected();

                    this.controllers.detail = new AbortController();

                    const item = this.groupedResult[group][index];
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
