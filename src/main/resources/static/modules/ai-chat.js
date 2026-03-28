layui.define(['layim', 'common'], function (exports) {

    let abortController, conversationId = layui.common.asUuid();
    layui.layim.config({contactsPanel: false, init: {user: {id: 'admin'}}});

    const md = markdownit({typographer: true, linkify: true, breaks: true});
    layui.layim.callback('contentParser', function (data) {
        return md.render(data).replaceAll('<table>', '<table class="layui-table" lay-size="sm">');
    });

    layui.layim.on('sendMessage', function (data) {
        console.log('sendMessage => ', data);
        const user = data.user;
        const receiver = data.receiver;
        if (receiver.type === 'ai') {
            const messages = [];
            abortController = new AbortController();
            SSE.fetchEventSource(`${config.base}ai/chat/completions`, {
                method: 'POST', headers: {'Content-Type': 'application/json'}
                , body: JSON.stringify({conversationId: conversationId, question: user.content})
                , signal: abortController.signal
                , onopen: function (response) {
                    if (!response.ok) throw new Error(`URL ${response.status} ${response.statusText}`);
                }
                , onmessage: function (msg) {
                    const data = JSON.parse(msg.data);
                    data?.result?.output?.text && messages.push(data.result.output.text);
                    if (layui.$.trim(messages.join(''))) {
                        layui.layim.getMessage({...receiver, content: messages.join(''), finished: false});
                    }
                }
                , onerror: function (error) {
                    layui.layim.getMessage({...receiver, content: `**错误信息：** ${error}`, finished: true});
                    throw error;
                }
                , onclose: function () {
                    layui.layim.getMessage({...receiver, content: messages.join(''), finished: true});
                }
            });
        }
    });

    layui.layim.on('chatInit', function (obj) {
        const data = obj.data, elem = obj.elem;
        if (data.type === 'ai') {
            const lastItem = elem.find('.layim-chat-main>ul>li').last();
            const existMessages = layui.layim.chat.messages && layui.layim.chat.messages[data.id];
            if (!existMessages && !lastItem.hasClass('layim-chat-system')) {
                layui.layim.getMessage({
                    system: true
                    , id: data.id
                    , type: 'ai'
                    , content: '以下为新的对话'
                    , saveLocalChatlog: false
                });
            }
            layui.common.req(`${config.base}ai/chat/${conversationId}`, 'DELETE');
            conversationId = layui.common.asUuid();
        }
    });

    layui.admin.events.ai = function () {
        layui.layim.chat({
            type: 'ai'
            , id: config.title
            , username: `${config.title} v${config.version}`
            , avatar: config.base + 'favicon.ico'
            , new: true
            , enableLocalChatlog: false
            , layer: {
                cancel: function () {
                    if (abortController) abortController.abort();
                    layui.common.req(`${config.base}ai/chat/${conversationId}`, 'DELETE');
                }
            }
        });
    };

    exports('ai-chat', {});
});