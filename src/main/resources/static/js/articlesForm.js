const realTextarea = document.getElementById('text');
const textFaker = document.getElementById('text-faker');
const form = document.getElementById('article_edit');
const xhr = new XMLHttpRequest();

let savingDebounce;

function saveForm() {
    editorSaveHandler();

    if (savingDebounce) {
        clearTimeout(savingDebounce);
    }

    if (xhr.readyState > 0 && xhr.readyState < 4) {
        xhr.abort();
    }

    savingDebounce = setTimeout(function () {
        const body = new FormData();
        const fakerContent = textFaker.innerText;

        for (let i = 0; i < form.elements.length; i++) {
            if (!['INPUT', 'TEXTAREA'].includes(form.elements[i].tagName)) {
                continue;
            }

            if (!form.elements[i].required) {
                body.append(form.elements[i].name, form.elements[i].value);

                continue;
            }

            if (form.elements[i].name === 'text' && fakerContent.length < form.elements[i].minLength) {
                form.elements[i].value = '';
            }

            if (!form.elements[i].checkValidity()) {
                return;
            }

            body.append(form.elements[i].name, form.elements[i].value);
        }

        if (!body.has('save')) {
            body.append('save', 'save');
        }

        xhr.open(
            form.getAttribute('method'),
            form.getAttribute('action')
        )

        xhr.send(body);

        xhr.onload = function () {
            if (xhr.status !== 200) {
                throw new Error(xhr.statusText);
            }
        };

        xhr.onerror = function () {
            throw new Error("Something wrong..")
        }
    }, 1200);
}

function editorSaveHandler() {
    editor.save()
        .then(function (output) {
            realTextarea.value = JSON.stringify(output);
        })
        .catch(function (err) {
            console.error(err);
        })
}

let parsedContent = {};

if (realTextarea.value.length) {
    parsedContent = JSON.parse(realTextarea.value);
}

const editorJSOptions = {
    holder: 'text-faker',
    placeholder: 'Содержание статьи...',
    data: parsedContent,
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
    onChange: function () {
        saveForm();
    }
};

const editor = new EditorJS(editorJSOptions);

for (let i = 0; i < form.elements.length; i++) {
    form.elements[i].addEventListener('input', saveForm);
    form.elements[i].addEventListener('change', saveForm);
}
