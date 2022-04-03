const realTextarea = document.getElementById('text');
const textContent = realTextarea.value;

let parsedContent = {};

if (textContent.length) {
    parsedContent = JSON.parse(textContent);
}

const editor = new EditorJS({
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
            levels: [4, 5, 6]
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
        raw: RawTool,
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
    },
    onChange: function () {
        editor.save()
            .then(function (output) {
                realTextarea.value = JSON.stringify(output);
            })
            .catch(function (err) {
                console.error(err);
            })
    }
});
