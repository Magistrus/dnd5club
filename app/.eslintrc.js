module.exports = {
    root: true,
    env: {
        node: true,
    },
    extends: [
        'plugin:vue/vue3-recommended',
        '@vue/airbnb',
    ],
    parserOptions: {
        ecmaVersion: 2020,
        ecmaFeatures: {
            jsx: true
        }
    },
    ignorePatterns: ['./public_html/*', './dist/*'],
    rules: {
        'no-console': process.env.NODE_ENV === 'production'
            ? [
                'error',
                {
                    allow: ['warn', 'error']
                }
            ]
            : 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'max-len': [
            1,
            120
        ],
        'consistent-return': [
            1
        ],
        'comma-dangle': ['error', {
            arrays: 'only-multiline',
            objects: 'only-multiline',
            imports: 'only-multiline',
            exports: 'only-multiline',
            functions: 'never'
        }],
        'arrow-parens': [
            2,
            'as-needed'
        ],
        'object-curly-spacing': ['error', 'always', {
            objectsInObjects: false
        }],
        'quote-props': [
            'error',
            'consistent-as-needed'
        ],
        'import/extensions': [
            0
        ],
        'import/no-extraneous-dependencies': [
            0
        ],
        'import/no-unresolved': [
            0
        ],
        'import/no-webpack-loader-syntax': [
            0
        ],
        'indent': [
            2,
            4, {
                SwitchCase: 1,
                ignoredNodes: [
                    'TemplateLiteral'
                ]
            }
        ],
        'generator-star-spacing': [
            0
        ],
        'no-continue': [
            0
        ],
        'no-await-in-loop': [
            0
        ],
        'no-nested-ternary': [
            1
        ],
        'no-return-assign': [
            1
        ],
        'object-property-newline': [
            0
        ],
        'no-mixed-operators': [
            0
        ],
        'no-bitwise': [
            0
        ],
        'no-plusplus': [
            0
        ],
        'no-restricted-syntax': [
            2,
            'ForInStatement',
            'LabeledStatement',
            'WithStatement'
        ],
        'space-before-function-paren': [
            2,
            {
                anonymous: 'never',
                named: 'never',
                asyncArrow: 'always'
            }
        ],
        'linebreak-style': 'off',
        'template-curly-spacing': 'off',
        'no-param-reassign': [
            'error',
            {
                props: true,
                ignorePropertyModificationsFor: [
                    'state'
                ]
            }
        ],
        'semi': 'off',
        'vue/match-component-file-name': [
            'error', {
                extensions: ['jsx', 'js', 'tsx', 'ts', 'vue'],
                shouldMatchCase: true,
            }
        ],
        'vue/component-options-name-casing': [
            'error',
            'PascalCase'
        ],
        'vue/component-name-in-template-casing': [
            'error',
            'kebab-case', {
                registeredComponentsOnly: true,
                ignores: []
            }
        ],
        'vue/component-api-style': [
            'error',
            ['script-setup', 'composition', 'options']
        ],
        'vue/block-lang': [
            'error',
            {
                script: {
                    allowNoLang: true
                }
            }
        ],
        'vuejs-accessibility/anchor-has-content': 'off',
        'vuejs-accessibility/click-events-have-key-events': 'off',
        'vuejs-accessibility/mouse-events-have-key-events': 'off',
        'vuejs-accessibility/label-has-for': 'off',
        'vue/require-explicit-emits': [
            'warn',
            {
                allowProps: true
            }
        ]
    },
    overrides: [
        {
            files: ['*.vue'],
            rules: {
                'indent': 'off',
                'vue/html-indent': [
                    'error',
                    4
                ],
                'quotes': [
                    0,
                    'double',
                    'single'
                ],
                'vue/max-attributes-per-line': [
                    'error', {
                        singleline: 1,
                        multiline: 1
                    }
                ],
                'vue/script-indent': [
                    'error',
                    4,
                    {
                        baseIndent: 1,
                        switchCase: 1
                    }
                ],
                'vue/html-closing-bracket-spacing': [
                    'error',
                    {
                        selfClosingTag: 'never'
                    }
                ],
            }
        },
    ]
};
