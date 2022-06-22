// TODO: Go to Vue.js

const editorData = document.getElementById('editor-data');
const textContent = editorData.innerHTML;

let parsedContent = {};

if (textContent.length) {
    parsedContent = JSON.parse(textContent);
}

const editorJSOptions = {
    holder: 'text-preview',
    placeholder: 'Содержание статьи...',
    data: parsedContent,
    readOnly: true,
    logLevel: 'ERROR',
    tools: {
        checklist: {
            class: Checklist,
            inlineToolbar: true
        },
        code: CodeTool,
        delimiter: Delimiter,
        embed: {
            class: Embed,
            inlineToolbar: true
        },
        header: {
            class: Header,
            inlineToolbar: true,
            config: {
                placeholder: 'Заголовок',
                levels: [3, 4, 5],
                defaultLevel: 4
            }
        },
        inlineCode: {
            class: InlineCode
        },
        list: {
            class: List,
            inlineToolbar: true,
            config: {
                defaultStyle: 'unordered'
            }
        },
        marker: {
            class: Marker
        },
        paragraph: {
            class: Paragraph,
            inlineToolbar: true,
        },
        quote: {
            class: Quote,
            inlineToolbar: true,
            config: {
                quotePlaceholder: 'Введите цитату',
                captionPlaceholder: 'Автор цитаты',
            },
        },
        table: {
            class: Table,
            inlineToolbar: true
        },
        warning: {
            class: Warning,
            inlineToolbar: true,
            config: {
                titlePlaceholder: 'Заголовок',
                messagePlaceholder: 'Сообщение',
            },
        },
        dnd5ClubMarkersDiceText: DnD5ClubMarkersDiceText,
        dnd5ClubMarkersSavingThrow: DnD5ClubMarkersSavingThrow,
        dnd5ClubMarkersAdvantage: DnD5ClubMarkersAdvantage,
        dnd5ClubMarkersDisadvantage: DnD5ClubMarkersDisadvantage,
    },
    i18n: {
        messages: {
            ui: {
                "blockTunes": {
                    "toggler": {
                        "Click to tune": "Нажмите, чтобы настроить",
                        "or drag to move": "или перетащите"
                    },
                },
                "inlineToolbar": {
                    "converter": {
                        "Convert to": "Конвертировать в"
                    }
                },
                "toolbar": {
                    "toolbox": {
                        "Add": "Добавить"
                    }
                }
            },
            toolNames: {
                "Text": "Параграф",
                "Heading": "Заголовок",
                "List": "Список",
                "Warning": "Примечание",
                "Checklist": "Чек-лист",
                "Quote": "Цитата",
                "Code": "Код",
                "Delimiter": "Разделитель",
                "Raw HTML": "HTML-фрагмент",
                "Table": "Таблица",
                "Link": "Ссылка",
                "Marker": "Маркер",
                "Bold": "Полужирный",
                "Italic": "Курсив",
                "InlineCode": "Моноширинный",
                "Dnd5ClubMarkersDiceText": "Кубик",
                "Dnd5ClubMarkersSavingThrow": "Спасбросок",
                "Dnd5ClubMarkersAdvantage": "Преимущество",
                "Dnd5ClubMarkersDisadvantage": "Помеха",
            },
            tools: {
                "warning": {
                    "Title": "Название",
                    "Message": "Сообщение",
                },
                "link": {
                    "Add a link": "Вставьте ссылку"
                },
                "stub": {
                    'The block can not be displayed correctly.': 'Блок не может быть отображен'
                }
            },
            blockTunes: {
                "delete": {
                    "Delete": "Удалить"
                },
                "moveUp": {
                    "Move up": "Переместить вверх"
                },
                "moveDown": {
                    "Move down": "Переместить вниз"
                }
            },
        }
    },
};

const editor = new EditorJS(editorJSOptions);
