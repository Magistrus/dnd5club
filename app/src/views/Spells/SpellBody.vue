<template>
    <div
        v-if="spell"
        class="spell_wrapper spell-body"
        :class="{ 'in-tooltip': inTooltip }"
    >
        <detail-top-bar
            :bg-grey="false"
            :left="`${
                spell.level ? `${ spell.level } уровень` : 'заговор'
            }, ${
                spell.school
            }${
                spell.additionalType ? ` [${ spell.additionalType }]` : ''
            }${
                spell.ritual ? ' (ритуал)' : ''
            }`"
            :source="spell.source"
        />

        <div class="grid_stat_block">
            <div class="block">
                <p>Время накладывания:</p>

                <span>{{ spell.time }}</span>
            </div>

            <div class="block distance">
                <p>Дистанция:</p>

                <span>{{ spell.range }}</span>
            </div>

            <div class="block duration">
                <p>Длительность:</p>

                <span>{{ spell.duration }}</span>
            </div>

            <div class="block one_row">
                <p>Компоненты:</p>

                <span
                    v-if="spell.components.v"
                    v-tippy="{ content: tooltip.v }"
                >Вербальный{{ spell.components.s || spell.components.m ? ', ' : '' }}</span>

                <span
                    v-if="spell.components.s"
                    v-tippy="{ content: tooltip.s }"
                >Соматический{{ spell.components.m ? ', ' : '' }}</span>

                <span
                    v-if="spell.components.m"
                    v-tippy="{ content: tooltip.m }"
                >Материальный ({{ spell.components.m }})</span>
            </div>
        </div>

        <div class="content-padding">
            <!-- eslint-disable-next-line vue/no-v-html -->
            <raw-content :template="spell.description"/>

            <p v-if="spell.upper">
                <!-- eslint-disable-next-line vue/no-v-html -->
                <strong>На более высоких уровнях: </strong> <raw-content
                    tag="span"
                    :template="spell.upper"
                />
            </p>

            <div
                v-if="spell.classes?.length"
                class="spell_stat_block_bottom"
            >
                <p>Классы:</p>

                <div class="classes_icon">
                    <class-square
                        v-for="(el, key) in spell.classes"
                        :key="key"
                        :icon="el.icon"
                        :name="el.name"
                        :url="el.url"
                    />
                </div>
            </div>

            <div
                v-if="spell.subclasses?.length"
                class="spell_stat_block_bottom"
            >
                <p>Подклассы:</p>

                <div>
                    <span
                        v-for="(el, key) in spell.subclasses"
                        :key="key"
                    >
                        <a
                            v-tippy="{ content: el.class }"
                            :href="el.url"
                        >{{ el.name }}</a>

                        <span v-if="key !== spell.subclasses.length - 1">,&nbsp;</span>
                    </span>
                </div>
            </div>
            <div
                v-if="spell.races?.length"
                class="spell_stat_block_bottom"
            >
                <p>Расы:</p>

                <div>
                    <span
                        v-for="(el, key) in spell.races"
                        :key="key"
                    >
                        <a
                            :href="el.url"
                        >{{ el.name }}</a>

                        <span v-if="key !== spell.races.length - 1">,&nbsp;</span>
                    </span>
                </div>
            </div>
            <div
                v-if="spell.backgrounds?.length"
                class="spell_stat_block_bottom"
            >
                <p>Предыстории:</p>

                <div>
                    <span
                        v-for="(el, key) in spell.backgrounds"
                        :key="key"
                    >
                        <a
                            :href="el.url"
                        >{{ el.name }}</a>

                        <span v-if="key !== spell.backgrounds.length - 1">,&nbsp;</span>
                    </span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import ClassSquare from "@/components/UI/ClassSquare";
    import DetailTopBar from "@/components/UI/DetailTopBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "SpellBody",
        components: {
            RawContent,
            DetailTopBar,
            ClassSquare
        },
        props: {
            spell: {
                type: Object,
                default: undefined,
                required: true
            },
            inTooltip: {
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            tooltip: {
                v: 'Большинство заклинаний требуют произношения таинственных слов.'
                    + '\nСами по себе слова не являются источником силы заклинания; просто комбинация звуков с особой'
                    + 'тональностью вызывает резонанс в прядях магии, приводя их в движение.'
                    + '\nТаким образом, персонаж с кляпом во рту или в области заклинания тишина, не может активировать'
                    + ' заклинания с вербальным компонентом.',
                s: 'Заклинание может требовать энергичной жестикуляции или замысловатой последовательности'
                    + ' телодвижений.\nЕсли у заклинания есть соматический компонент, у заклинателя должна быть'
                    + ' свободной хотя бы одна рука для исполнения этих жестов.',
                m: 'У заклинателя должна быть одна свободная рука для доступа к материальным компонентам,'
                    + ' но это может быть та же самая рука, что используется для выполнения соматического компонента.'
                    + '\nПерсонаж может использовать мешочек с компонентами или заклинательную фокусировку вместо'
                    + ' указанных компонентов. Однако, если для компонента указана цена, у персонажа для накладывания'
                    + ' заклинания должен быть именно такой компонент.'
            }
        })
    };
</script>
