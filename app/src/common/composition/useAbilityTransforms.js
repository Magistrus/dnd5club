export function useAbilityTransforms() {
    /**
     * Получение модификатора значения характеристики.
     *
     * @param { number } ability - значение характеристики.
     * @return { number } - модификатор характеристики.
     */
    const getModifier = ability => {
        if (typeof ability !== 'number') {
            throw new Error('Ability is not number type');
        }

        return Math.floor((ability - 10) / 2);
    };

    /**
     * Получение значения без знака.
     *
     * @param { number } ability - значение характеристики.
     * @return { number } - модификатор характеристики.
     */
    const getABSModifier = ability => {
        if (typeof ability !== 'number') {
            throw new Error('Ability is not number type');
        }

        return Math.abs(getModifier(ability));
    };

    /**
     * Получение знака перед модификатором.
     *
     * @param { number } ability - значение характеристики.
     * @return { string } - модификатор характеристики.
     */
    const getSign = ability => {
        if (typeof ability !== 'number') {
            throw new Error('Ability is not number type');
        }

        return (Math.sign(getModifier(ability)) > -1 ? '+' : '−');
    };

    /**
     * Получение модификатора со знаком.
     *
     * @param { number } ability - значение характеристики.
     * @return { string } - модификатор характеристики.
     */
    const getFormattedModifier = ability => {
        if (typeof ability !== 'number') {
            throw new Error('Ability is not number type');
        }

        return (`${ getSign(ability) }${ getABSModifier(ability) }`);
    };

    const getFormula = ability => {
        if (typeof ability !== 'number') {
            throw new Error('Ability is not number type');
        }

        return (`1к20${ getSign(ability) }${ getABSModifier(ability) }`);
    };

    return {
        getModifier,
        getABSModifier,
        getSign,
        getFormattedModifier,
        getFormula
    };
}

export default {
    useAbilityTransforms
};
