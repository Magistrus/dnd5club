const realTextarea = document.getElementById('text');
const textContent = realTextarea.value;
const form = document.getElementById('article_edit');

let savingDebounce;

function saveForm() {
    if (savingDebounce) {
        clearTimeout(savingDebounce);
    }

    savingDebounce = setTimeout(function () {
        const body = new FormData(form);

        body.append('save', 'save');

        fetch(form.getAttribute('action'), {
            method: form.getAttribute('method'),
            redirect: 'follow',
            body
        }).catch(function (err) {
            if (err.name !== 'AbortError') {
                throw new Error(err);
            }
        });
    }, 1200);
}

let parsedContent = {};

if (textContent.length) {
    parsedContent = JSON.parse(textContent);
}

const editorJSOptions = {
    holder: 'text-faker',
    placeholder: 'Содержание статьи...',
    data: parsedContent,
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
    onChange: function () {
        editor.save()
            .then(async function (output) {
                realTextarea.value = JSON.stringify(output);

                saveForm();
            })
            .catch(function (err) {
                console.error(err);
            })
    }
};

const editor = new EditorJS(editorJSOptions);

for (let i = 0; i < form.elements.length; i++) {
    form.elements[i].addEventListener('input', saveForm);
}
