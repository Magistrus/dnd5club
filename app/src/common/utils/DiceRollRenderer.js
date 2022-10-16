/* eslint-disable no-use-before-define */
import { h } from 'vue';

function doRender(roll) {
    let render;

    const { type } = roll;

    switch (type) {
        case 'diceexpressionroll':
            render = renderGroup(roll);

            break;
        case 'grouproll':
            render = renderGroup(roll);

            break;
        case 'die':
            render = renderDie(roll);

            break;
        case 'expressionroll':
            render = renderExpression(roll);

            break;
        case 'mathfunction':
            render = renderFunction(roll);

            break;
        case 'roll':
            return renderRoll(roll);
        case 'fateroll':
            return renderFateRoll(roll);
        case 'number':
            return h(
                'span',
                `${ roll.value }${ roll.label
                    ? ` (${ roll.label })`
                    : '' }`
            );
        case 'fate':
            return h('span', 'F');
        default:
            throw new Error('Unable to render');
    }

    if (!roll.valid) {
        render = h('u', render);
    }

    return roll.label
        ? h('span', [`${ roll.label }: `, render])
        : h('span', render);
}

function renderGroup(group) {
    const replies = [];

    for (let i = 0; i < group.dice.length; i++) {
        const die = group.dice[0];

        replies.push(doRender(die));

        if (i < group.dice.length - 1) {
            replies.push(' + ');
        }
    }

    return h('span', replies);
}

function renderDie(die) {
    const replies = [];

    for (let i = 0; i < die.rolls.length; i++) {
        const roll = die.rolls[i];

        replies.push(doRender(roll));

        if (i < die.rolls.length - 1) {
            replies.push(' + ');
        }
    }

    if (!['number', 'fate'].includes(die.die.type) || die.count.type !== 'number') {
        replies.push(h('span', [
            '[',
            h('i', [
                'Rolling: ',
                doRender(die.count),
                'd',
                doRender(die.die)
            ]),
            ']'
        ]));
    }

    return h('span', replies);
}

function renderExpression(expr) {
    if (expr.dice.length > 1) {
        const expressions = [];

        for (let i = 0; i < expr.dice.length - 1; i++) {
            expressions.push(doRender(expr.dice[i]));
            expressions.push(` ${ expr.ops[i] } `);
        }

        expressions.push(doRender(expr.dice.slice(-1)[0]));

        return h('span', expressions);
    }

    if (expr.dice[0].type === 'number') {
        return expr.value;
    }

    return doRender(expr.dice[0]);
}

function renderFunction(roll) {
    return h('span', [h('span', roll.op), doRender(roll.expr)]);
}

function renderRoll(roll) {
    let rollDisplay = roll.roll;

    if (!roll.valid) {
        rollDisplay = h('del', roll.roll);
    } else if (roll.success && roll.value === 1) {
        rollDisplay = h('span', { class: 'advantage' }, roll.roll);
    } else if (roll.success && roll.value === -1) {
        rollDisplay = h('span', { class: 'disadvantage' }, roll.roll);
    } else if (!roll.success && roll.critical === 'success') {
        rollDisplay = h('span', { class: 'advantage' }, roll.roll);
    } else if (!roll.success && roll.critical === 'failure') {
        rollDisplay = h('span', { class: 'disadvantage' }, roll.roll);
    }

    if (roll.matched) {
        rollDisplay = h('u', rollDisplay);
    }

    return h('span', [
        '[',
        rollDisplay,
        ']'
    ]);
}

function renderFateRoll(roll) {
    let rollDisplay = roll.roll;

    if (roll.roll > 0) {
        rollDisplay = h('span', ['+', rollDisplay]);
    }

    if (roll.roll < 0) {
        rollDisplay = h('span', ['-', rollDisplay]);
    }

    if (!roll.valid) {
        rollDisplay = h('del', rollDisplay);
    } else if (roll.success && roll.value === 1) {
        rollDisplay = h('span', { class: 'advantage' }, rollDisplay);
    } else if (roll.success && roll.value === -1) {
        rollDisplay = h('span', { class: 'disadvantage' }, rollDisplay);
    }

    if (roll.matched) {
        rollDisplay = h('u', rollDisplay);
    }

    return h('span', [
        '[',
        rollDisplay,
        ']'
    ]);
}
/* eslint-enable no-use-before-define */

export const getRendered = roll => h('span', [
    h('strong', roll.value),
    ' = ',
    doRender(roll)
]);

export default {
    getRendered
};
