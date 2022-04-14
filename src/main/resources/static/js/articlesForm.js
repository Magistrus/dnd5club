const realTextarea = document.getElementById('text');
const textFaker = document.getElementById('text-faker');
const form = document.getElementById('article_edit');
const controller = new AbortController();
const query = axios.create({
    baseURL: '/profile/articles',
    timeout: 5000,
    method: 'post',
    responseType: 'json',
    signal: controller.signal,
    redirect: 'follow',
});

document.addEventListener('DOMContentLoaded', function () {
    const field = document.getElementById('id');
    const uuid = field.value;

    if (!uuid) {
        field.value = uuidv4();
    }
});

let savingDebounce;

function clearDebounce() {
    if (savingDebounce) {
        clearTimeout(savingDebounce);
    }
}

function getArticleUUID() {
    return new Promise(function (resolve, reject) {
        const uuidField = form.querySelector(`[name="id"]`);

        if (!uuidField) {
            reject();

            return;
        }

        const uuid = uuidField.value;

        if (!uuid) {
            reject();

            return;
        }

        resolve(uuid);
    })
}

async function getFormData() {
    try {
        await editor.save();

        const formBody = {
            id: null,
            status: null,
            title: null,
            description: null,
            text: null,
            linkAccess: null,
            tags: null,
            translation: null,
            originalAuthor: null,
            originalUrl: null,
            originalName: null,
            imageUrl: null,
            imageAuthor: null,
        };
        const fieldNames = Object.keys(formBody);
        const fakerContent = textFaker.innerText;
        const isFieldSuccess = function (element) {
            if (!element.required) {
                return true;
            }

            if (element.name === 'text' && fakerContent.length < element.minLength) {
                element.value = '';
            }


            return element.checkValidity();
        }

        let error;

        for (let i = 0; i < fieldNames.length && !error; i++) {
            const name = fieldNames[i];
            const field = form.querySelector(`[name=${ name }]`);

            if (!field) {
                error = true;

                console.error('Field with name ' + name + ' not found');

                continue;
            }

            if (!['INPUT', 'TEXTAREA'].includes(field.tagName)) {
                continue;
            }

            if (isFieldSuccess(field)) {
                if (field.type === 'checkbox') {
                    formBody[name] = field.checked;

                    continue;
                }

                formBody[name] = field.value.trim();

                continue;
            }

            error = field.reportValidity();
        }

        if (!error) {
            return formBody;
        }

        console.error('Oops...');
    } catch (err) {
        console.error(err);
    }
}

async function saveHandler(debounceClearing = true) {
    try {
        if (debounceClearing) {
            clearDebounce();
        }

        const data = await getFormData();

        await controller.abort();
        await query({
            url: '/save',
            data
        });
    } catch (err) {
        console.error(err);
    }
}

function autoSave() {
    clearDebounce();

    savingDebounce = setTimeout(async function () {
        try {
            await saveHandler(false);
        } catch (err) {
            console.error(err);
        }
    }, 1200);
}

async function saveDraft() {
    try {
        await saveHandler();
    } catch (err) {
        console.error(err);
    }
}

async function APIArticleQuery(url) {
    try {
        await saveHandler();

        const data = await getArticleUUID();

        return query({
            url,
            data
        });
    } catch (err) {
        console.error(err);
    }
}

async function publishArticle() {
    try {
        await APIArticleQuery('/publish');
    } catch (err) {
        console.error(err);
    }
}

async function sendToModerate() {
    try {
        await APIArticleQuery('/moderate');
    } catch (err) {
        console.error(err);
    }
}

async function goToPreview() {
    try {
        await APIArticleQuery('/preview');
    } catch (err) {
        console.error(err);
    }
}

async function deleteArticle() {
    try {
        await APIArticleQuery('/delete');
    } catch (err) {
        console.error(err);
    }
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
        autoSave();
    }
};

const editor = new EditorJS(editorJSOptions);

for (let i = 0; i < form.elements.length; i++) {
    form.elements[i].addEventListener('input', autoSave);
    form.elements[i].addEventListener('change', autoSave);
}

const publishBtn = document.getElementById('publish');
const moderateBtn = document.getElementById('moderate');
const saveBtn = document.getElementById('save');
const previewBtn = document.getElementById('preview');
const deleteBtn = document.getElementById('delete');

function isLeftClick(event) {
    const e = event || window.event;

    return 'object' === typeof e && e.button === 0;
}

publishBtn.addEventListener('click', async function (e) {
    e.preventDefault();

    if (!isLeftClick(e)) {
        return;
    }

    await publishArticle();
});

moderateBtn.addEventListener('click', async function (e) {
    e.preventDefault();

    if (!isLeftClick(e)) {
        return;
    }

    await sendToModerate();
});

saveBtn.addEventListener('click', async function (e) {
    e.preventDefault();

    if (!isLeftClick(e)) {
        return;
    }

    await saveDraft();
});

previewBtn.addEventListener('click', async function (e) {
    e.preventDefault();

    if (!isLeftClick(e)) {
        return;
    }

    await goToPreview();
});

deleteBtn.addEventListener('click', async function (e) {
    e.preventDefault();

    if (!isLeftClick(e)) {
        return;
    }

    await deleteArticle();
});
