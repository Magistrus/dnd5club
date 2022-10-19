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
            return 'F';
        default:
            throw new Error('Unable to render');
    }

    return h(
        !roll.valid ? 'u' : 'span',
        render
    );
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

    return h(
        'span',
        replies
    );
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
            h(
                'i',
                [
                    'Rolling: ',
                    doRender(die.count),
                    'd',
                    doRender(die.die)
                ]
            ),
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

        return h(
            'span',
            expressions
        );
    }

    if (expr.dice[0].type === 'number') {
        return expr.value;
    }

    return doRender(expr.dice[0]);
}

function renderFunction(roll) {
    return h(
        'span',
        [roll.op, doRender(roll.expr)]
    );
}

function renderRoll(roll) {
    let rollDisplay = h(
        'span',
        {
            class: {
                advantage: (roll.success && roll.value === 1) || (!roll.success && roll.critical === 'success'),
                disadvantage: (roll.success && roll.value === -1) || (!roll.success && roll.critical === 'failure')
            }
        },
        roll.roll
    );

    if (roll.matched) {
        rollDisplay = h('u', rollDisplay);
    }

    return h(
        !roll.valid ? 'del' : 'span',
        [
            '[',
            rollDisplay,
            ']'
        ]
    );
}

function renderFateRoll(roll) {
    let rollDisplay = roll.roll;

    if (roll.roll > 0) {
        rollDisplay = `+${ rollDisplay }`;
    }

    if (roll.roll < 0) {
        rollDisplay = `-${ rollDisplay }`;
    }

    rollDisplay = h(
        'span',
        {
            class: {
                advantage: roll.success && roll.value === 1,
                disadvantage: roll.success && roll.value === -1
            }
        },
        rollDisplay
    );

    if (roll.matched) {
        rollDisplay = h('u', rollDisplay);
    }

    return h(
        !roll.valid ? 'del' : 'span',
        [
            '[',
            rollDisplay,
            ']'
        ]
    );
}
/* eslint-enable no-use-before-define */

export const getRendered = ({
    roll = undefined,
    label = ''

    // advantage = false,
    // disadvantage = false
}) => {
    if (!roll) {
        throw new Error('roll is not defined');
    }

    const rendered = h(
        'span',
        {
            class: 'dice-roll__rendered'
        },
        doRender(roll)
    );

    return h(
        'span',
        {
            class: 'dice-roll'
        },
        [
            h(
                'strong',
                {
                    class: {
                        'dice-roll__result': true,
                        'is-critical': false,
                        'is-failure': false
                    }
                },
                Math.floor(roll.value)
            ),
            h(
                'span',
                {
                    class: 'dice-roll__body'
                },
                label
                    ? [
                        h(
                            'span',
                            {
                                class: 'dice-roll__label'
                            },
                            label
                        ),
                        rendered
                    ]
                    : rendered
            )
        ]
    );
};

export default {
    getRendered
};
