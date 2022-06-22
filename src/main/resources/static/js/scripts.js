// TODO: Change to Vue.js

$(document).ready(function () {
    Tipped.delegate('.tip', {
        skin: 'dnd5',
    });
    Tipped.delegate('.tip_scroll', {
        skin: 'dnd5',
        afterUpdate: function (content, element) {
            content.classList.add('tooltip_scroll');
        },
    });
    Tipped.delegate('.tip_spell', {
        ajax: {
            url: '/spells/id',
            type: 'get',
            success: function (data, textStatus, jqXHR) {
                return {
                    content: jqXHR.responseText
                };
            }
        },
        afterUpdate: function (content, element) {
            content.classList.add('tooltip_scroll');
        },
        skin: 'dnd5',
    });
});
