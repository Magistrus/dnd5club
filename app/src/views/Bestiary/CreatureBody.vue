<template>
    <div
        v-if="creature"
        class="creature_wrapper creature-body bestiary"
    >
        <detail-tob-bar
            :left="topBarLeftString"
            :source="creature.source"
        />

        <div class="avatar">
            <div class="image-container">
                <a id="creature_href">
                    <img
                        id="creature_img"
                        :src="!creature.images?.length ? '/app/img/dark/no-img-best.png' : creature.images[0]"
                        alt="Title best"
                        @click.left.exact.prevent="showGallery"
                    >
                </a>
            </div>
        </div>

        <div class="beast_info">
            <p>
                <strong>Класс доспеха </strong>

                <span>{{ `${ creature.armorClass }${ creature.armorText ? ` (${ creature.armorText })` : '' }` }} </span>

                <span v-if="creature.armors?.length">
                    ({{ creature.armors.join(', ') }})
                </span>
            </p>

            <p>
                <strong>Хиты </strong>

                <span>{{ creature.hits.average }} ({{ creature.hits.formula }}) {{ creature.hits.text || '' }}</span>
            </p>

            <p>
                <strong>Скорость </strong>

                <span v-if="speed">{{ speed }}</span>
            </p>
        </div>

        <div class="scores">
            <div class="scores__stats strength">
                <h4>
                    <strong
                        v-tippy="'Сила'"
                    >СИЛ</strong>
                </h4>
                <p>{{ creature.ability.str }}</p>
            </div>

            <div class="scores__stats dexterity">
                <h4>
                    <strong
                        v-tippy="'Ловкость'"
                    >ЛОВ</strong>
                </h4>
                <p>{{ creature.ability.dex }}</p>
            </div>

            <div class="scores__stats constitution">
                <h4>
                    <strong
                        v-tippy="'Телосложение'"
                    >ТЕЛ</strong>
                </h4>
                <p>{{ creature.ability.con }}</p>
            </div>

            <div class="scores__stats intelligence">
                <h4>
                    <strong
                        v-tippy="'Интеллект'"
                    >ИНТ</strong>
                </h4>
                <p>{{ creature.ability.int }}</p>
            </div>

            <div class="scores__stats wisdom">
                <h4>
                    <strong
                        v-tippy="'Мудрость'"
                    >МДР</strong>
                </h4>
                <p>{{ creature.ability.wiz }}</p>
            </div>

            <div class="scores__stats charisma">
                <h4>
                    <strong
                        v-tippy="'Харизма'"
                    >ХАР</strong>
                </h4>
                <p>{{ creature.ability.cha }}</p>
            </div>
        </div>

        <div class="beast_info">
            <p v-if="savingThrows">
                <strong>Спасброски </strong> <span>{{ savingThrows }}</span>
            </p>

            <p v-if="skills">
                <strong>Навыки </strong> <span>{{ skills }}</span>
            </p>

            <p v-if="creature.damageVulnerability">
                <strong>Уязвимость к урону </strong>

                <span>{{ creature.damageVulnerability.join(', ') }}</span>
            </p>
            <p v-if="creature.damageResistances">
                <strong>Сопротивление к урону </strong>

                <span>{{ creature.damageResistances.join(', ') }}</span>
            </p>
            <p v-if="creature.damageImmunities">
                <strong>Иммунитет к урону </strong>

                <span>{{ creature.damageImmunities.join(', ') }}</span>
            </p>
            <p v-if="creature.conditionImmunities">
                <strong>Иммунитет к состояниям </strong>

                <span>{{ creature.conditionImmunities.join(', ') }}</span>
            </p>

            <p v-if="senses">
                <strong>Чувства </strong> <span>{{ senses }}</span>
            </p>

            <p>
                <strong>Языки </strong>

                <span> {{ creature.languages?.length ? creature.languages.join(', ') : '-' }}</span>
            </p>

            <p>
                <strong>Уровень опасности </strong>

                <span>{{ creature.challengeRating }} ({{ creature.experience }} опыта)</span>
            </p>
        </div>
        <div v-if="creature.feats?.length">
            <h4 class="header_separator">
                <span>Способности</span>
            </h4>
            <div
                v-for="(feat, key) in creature.feats"
                :key="key"
            >
                <span class="bestiary_h5">
                    <h5>{{ feat.name }}</h5>

                    <raw-content :template="feat.value"/>
                </span>
            </div>
        </div>
        <div v-if="creature.actions?.length">
            <h4 class="header_separator">
                <span>Действия</span>
            </h4>
            <div
                v-for="(action, key) in creature.actions"
                :key="key"
            >
                <span class="bestiary_h5">
                    <h5>{{ action.name }}</h5>

                    <raw-content :template="action.value"/>
                </span>
            </div>
        </div>
        <div v-if="creature.reactions?.length">
            <h4 class="header_separator">
                <span>Реакции</span>
            </h4>
            <div
                v-for="(reaction, key) in creature.reactions"
                :key="key"
            >
                <span class="bestiary_h5">
                    <h5>{{ reaction.name }}</h5>

                    <raw-content :template="reaction.value"/>
                </span>
            </div>
        </div>
        <div v-if="creature.bonusActions?.length">
            <h4 class="header_separator">
                <span>Бонусные действия</span>
            </h4>
            <div
                v-for="(bonus, key) in creature.bonusActions"
                :key="key"
            >
                <span class="bestiary_h5">
                    <h5>{{ bonus.name }}</h5>

                    <raw-content :template="bonus.value"/>
                </span>
            </div>
        </div>
        <div v-if="creature.legendary?.list?.length">
            <h4 class="header_separator">
                <span>Легендарные Действия</span>
            </h4>

            <p v-if="!creature.legendary.description">
                <span>{{ creature.name.rus }}</span> может совершить 3 легендарных действия,
                выбирая из представленных ниже вариантов. За один раз можно использовать только одно легендарное
                действие, и только в конце хода другого существа. <span>{{ creature.name.rus }}</span> восстанавливает
                использованные легендарные действия в начале своего хода.
            </p>

            <p v-else>
                {{ creature.legendary.description }}
            </p>

            <div
                v-for="(action, key) in creature.legendary.list"
                :key="key"
            >
                <span class="bestiary_h5">
                    <h5>{{ action.name }}</h5>

                    <raw-content :template="action.value"/>
                </span>
            </div>
        </div>

        <div v-if="creature.lair?.description">
            <h4 class="header_separator">
                <span>Логово</span>
            </h4>

            <raw-content :template="creature.lair.description"/>
        </div>

        <div v-if="creature.lair?.action">
            <h4
                class="header_separator"
                th:if="*{lair.action}"
            >
                <span>Действия логова:</span>
            </h4>

            <raw-content :template="creature.lair.action"/>
        </div>

        <div v-if="creature.lair?.effect">
            <h4
                class="header_separator"
                th:if="*{lair.effect}"
            >
                <span>Региональные эффекты:</span>
            </h4>

            <raw-content :template="creature.lair.effect"/>
        </div>

        <div v-if="creature.environment?.length">
            <h4 class="header_separator">
                <span>Места обитания</span>
            </h4>

            <p>
                <span>{{ creature.environment.join(', ') }}</span>
            </p>
        </div>

        <details v-if="creature.description">
            <summary class="h4 header_separator">
                <span>Описание</span>
            </summary>

            <raw-content :template="creature.description"/>
        </details>

        <details
            v-for="(tag, key) in creature.tags"
            :key="key"
        >
            <summary class="h4 header_separator">
                <span>{{ tag.name }}</span>
            </summary>

            <raw-content
                class="content"
                :template="tag.description"
            />
        </details>
    </div>

    <vue-easy-lightbox
        v-if="creature.images?.length"
        :imgs="creature.images"
        :index="gallery.index"
        :visible="gallery.show"
        loop
        move-disabled
        scroll-disabled
        @hide="gallery.show = false"
    >
        <template #toolbar/>
    </vue-easy-lightbox>
</template>

<script>
    import SvgIcon from "@/components/UI/SvgIcon";
    import DetailTobBar from "@/components/UI/DetailTobBar";
    import RawContent from "@/components/content/RawContent";

    export default {
        name: "CreatureBody",
        components: {
            RawContent,
            DetailTobBar,
            SvgIcon
        },
        props: {
            creature: {
                type: Object,
                default: undefined,
                required: true
            }
        },
        data: () => ({
            gallery: {
                index: 0,
                show: false,
            }
        }),
        computed: {
            topBarLeftString() {
                // eslint-disable-next-line max-len
                return `${ this.creature.size.rus } ${ this.creature.type.name }${ this.creature.type.tags?.length ? ` (${ this.creature.type.tags.join(', ') })` : '' }, ${ this.creature.alignment } / ${ this.creature.size.eng } ${ this.creature.size.cell }`
            },

            speed() {
                if (!this.creature.speed?.length) {
                    return '';
                }

                const speeds = [];

                for (const speed of this.creature.speed) {
                    speeds.push(`${ speed.name ? `${ speed.name } ` : '' }${ speed.value }фт.`)
                }

                return speeds.join(', ')
            },

            savingThrows() {
                if (!this.creature.savingThrows?.length) {
                    return '';
                }

                const saves = [];

                for (const save of this.creature.savingThrows) {
                    saves.push(`${ save.name } ${ save.value }`)
                }

                return saves.join(', ')
            },

            skills() {
                if (!this.creature.skills?.length) {
                    return '';
                }

                const skills = [];

                for (const skill of this.creature.skills) {
                    skills.push(`${ skill.name } ${ skill.value }`)
                }

                return skills.join(', ')
            },

            senses() {
                const senses = [];

                if (this.creature.senses?.senses?.length) {
                    for (const sense of this.creature.senses.senses) {
                        const index = senses.push(`${ sense.name } ${ sense.value }`);

                        if (sense.additional) {
                            senses[index - 1] += `(${ sense.additional })`;
                        }
                    }
                }

                if (this.creature.senses?.passivePerception) {
                    senses.push(`пассивная Внимательность ${ this.creature.senses.passivePerception }`);
                }

                return senses.join(', ')
            },
        },
        methods: {
            showGallery() {
                if (!this.creature.images?.length) {
                    return;
                }

                this.gallery.show = true;
                this.gallery.index = 0;
            }
        }
    }
</script>

<style lang="scss" scoped>
    .creature-body {
        padding: 24px;
    }
</style>
