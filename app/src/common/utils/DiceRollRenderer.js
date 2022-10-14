/* eslint-disable no-use-before-define */
import { h } from 'vue';

function doRender(roll, root = false) {
    let render = '';

    const { type } = roll;

    switch (type) {
        case 'diceexpressionroll':
            render = renderGroupExpr(roll);

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
            return `${ roll.value }${ roll.label
                ? ` (${ roll.label })`
                : '' }`;
        case 'fate':
            return 'F';
        default:
            throw new Error('Unable to render');
    }

    if (!roll.valid) {
        render = `~~${ render.replace(/~~/g, '') }~~`;
    }

    if (root) {
        return stripBrackets(render);
    }

    return roll.label ? `(${ roll.label }: ${ render })` : render;
}

function renderGroup(group) {
    const replies = [];

    for (const die of group.dice) {
        replies.push(doRender(die));
    }

    if (replies.length > 1) {
        return `{ ${ replies.join(' + ') } }`;
    }

    const reply = stripBrackets(replies[0]);

    return `{ ${ reply } }`;
}

function renderGroupExpr(group) {
    const replies = [];

    for (const die of group.dice) {
        replies.push(doRender(die));
    }

    return replies.length > 1 ? `${ replies.join(' + ') }` : replies[0];
}

function renderDie(die) {
    const replies = [];

    for (const roll of die.rolls) {
        replies.push(doRender(roll));
    }

    let reply = '';

    if (die.length === 1) {
        reply += `<strong>${ die.value }</strong>`;
    }

    reply += ` ${ replies.join('+') }`;

    if (!['number', 'fate'].includes(die.die.type) || die.count.type !== 'number') {
        reply += `[*Rolling: ${ doRender(die.count) }d${ doRender(die.die) }*]`;
    }

    return `${ reply }`;
}

function renderExpression(expr) {
    if (expr.dice.length > 1) {
        const expressions = [];

        expressions.push(`<strong>${ expr.value }</strong> `);

        for (let i = 0; i < expr.dice.length - 1; i++) {
            expressions.push(doRender(expr.dice[i]));
            expressions.push(expr.ops[i]);
        }

        expressions.push(doRender(expr.dice.slice(-1)[0]));

        return `${ expressions.join(' ') }`;
    }

    if (expr.dice[0].type === 'number') {
        return `${ expr.value }`;
    }

    return doRender(expr.dice[0]);
}

function renderFunction(roll) {
    const render = doRender(roll.expr);

    return `${ roll.op }${ addBrackets(render) }`;
}

function addBrackets(render) {
    let res = render;

    if (!render.startsWith('(')) {
        res = `(${ res }`;
    }

    if (!render.endsWith(')')) {
        res = `${ res })`;
    }

    return res;
}

function stripBrackets(render) {
    let res = render;

    if (render.startsWith('(')) {
        res = res.substring(1);
    }

    if (render.endsWith(')')) {
        res = res.substring(0, res.length - 1);
    }

    return res;
}

function renderRoll(roll) {
    let rollDisplay = `<span>[${ roll.roll }]</span>`;

    if (!roll.valid) {
        rollDisplay = `<span><del>[${ roll.roll }]</del></span>`;
    } else if (roll.success && roll.value === 1) {
        rollDisplay = `<span class="advantage">[${ roll.roll }]</span>`;
    } else if (roll.success && roll.value === -1) {
        rollDisplay = `<span class="disadvantage">[${ roll.roll }]</span>`;
    } else if (!roll.success && roll.critical === 'success') {
        rollDisplay = `<span class="advantage">[${ roll.roll }]</span>`;
    } else if (!roll.success && roll.critical === 'failure') {
        rollDisplay = `<span class="disadvantage">[${ roll.roll }]</span>`;
    }

    if (roll.matched) {
        rollDisplay = `__${ rollDisplay }__`;
    }

    return rollDisplay;
}

function renderFateRoll(roll) {
    const rollValue = roll.roll === 0
        ? '0'
        : roll.roll > 0
            ? '+'
            : '-';

    let rollDisplay = `${ roll.roll }`;

    if (!roll.valid) {
        rollDisplay = `<span><del>[${ rollValue }]</del></span>`;
    } else if (roll.success && roll.value === 1) {
        rollDisplay = `<span class="advantage">[${ rollValue }]</span>`;
    } else if (roll.success && roll.value === -1) {
        rollDisplay = `<span class="disadvantage">[${ rollValue }]</span>`;
    }

    if (roll.matched) {
        rollDisplay = `__${ rollDisplay }__`;
    }

    return rollDisplay;
}
/* eslint-enable no-use-before-define */

export const getRendered = roll => {
    console.log(h('span', { innerHTML: doRender(roll, true) }));

    return h('span', { innerHTML: doRender(roll, true) });
};

export default {
    getRendered
};
