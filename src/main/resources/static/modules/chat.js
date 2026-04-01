layui.define(['layim', 'common'], function (exports) {

    layui.layim.config({
        contactsPanel: false
        , init: {
            user: {
                id: 'admin'
                , username: parent.layui.$('#sys-username').html()
                , avatar: parent.layui.$('#sys-avatar').attr('src')
            }
        }
    });

    layui.layim.extendChatTools([{
        name: 'mike', title: '语音输入', icon: 'layui-icon-mike'
        , onClick: function () {
            const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
            if (!SpeechRecognition) return layui.layer.msg('当前浏览器不支持语音识别');
            const recognition = new SpeechRecognition();
            recognition.lang = 'zh-CN';
            recognition.continuous = true;
            recognition.interimResults = true;
            recognition.maxAlternatives = 1;
            let loading;
            recognition.onstart = function () {
                loading = layui.layer.load(2, {
                    time: 0, shade: 0.1, shadeClose: true
                    , content: `<span style="position:absolute;left:-35px;width:150px;">语音识别中...<span>`
                    , end: function () {
                        recognition.stop();
                    }
                });
            }
            recognition.onend = function () {
                layui.layer.close(loading);
            }
            recognition.onerror = function (event) {
                layui.layer.close(loading);
                layui.layer.msg(`语音识别错误：${event.error}`);
            }
            recognition.onresult = function (event) {
                layui.$('textarea.layim-scrollbar').val(Array.from(event.results)
                    .map(result => result[0].transcript).join(''));
            }
            recognition.start();
        }
    }]);

    exports('chat', {
        abortController: null, conversationId: layui.common.asUuid()
        , asReload: function () {
            layui.$('#chat-flow').html('');
            layui.flow.reload('chat-flow');
        }
        , asDelete: function (id) {
            layui.layer.confirm(`&ensp;&ensp;&ensp;&ensp;删除后，聊天记录将不可恢复。`, {
                title: '确定删除对话？', closeBtn: 0, icon: 3, skin: 'layui-layer-admin'
            }, (index) => {
                layui.layer.close(index);
                layui.common.req(`${config.base}ai/chat/${id}`, 'DELETE', null, {
                    done: () => {
                        this.asReload();
                    }
                });
            });
        }
        , asContentParser: function (data) {
            const md = markdownit({typographer: true, linkify: true, breaks: true});
            return md.render(data).replaceAll('<table>', '<table class="layui-table" lay-size="sm">');
        }
        , asChatInit: function (data) {
            layui.layim.getMessage({
                system: true
                , id: data.data.id
                , type: data.data.type
                , content: config.copyright || data.data.username
                , saveLocalChatlog: false
            });
        }
        , asSendMessage: function (data) {
            const user = data.user, receiver = data.receiver;
            if (receiver.type === 'ai') {
                const messages = [];
                this.abortController = new AbortController();
                SSE.fetchEventSource(`${config.base}ai/chat/completions`, {
                    method: 'POST', headers: {'Content-Type': 'application/json'}
                    , body: JSON.stringify({conversationId: this.conversationId, question: user.content})
                    , signal: this.abortController.signal
                    , onopen: function (response) {
                        if (!response.ok) throw new Error(`URL ${response.status} ${response.statusText}`);
                    }
                    , onmessage: function (msg) {
                        const data = JSON.parse(msg.data);
                        data?.result?.output?.text && messages.push(data.result.output.text);
                        if (layui.$.trim(messages.join(''))) {
                            layui.layim.getMessage({...receiver, content: `${messages.join('')} _`, finished: false});
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
        }
        , asChatlog: function (data) {
            const user = data.user, receiver = data.receiver;
            return new Promise((resolve, reject) => {
                const messages = [];
                layui.common.req(`${config.base}ai/chat/${layui.chat.conversationId}`, 'GET', null, {
                    done: function (data) {
                        data.data.forEach(function (data) {
                            if (data.type === 'USER') {
                                messages.push({...user, content: data.content, timestamp: data.timestamp, user: true});
                            }
                            if (data.type === 'ASSISTANT') {
                                messages.push({...receiver, content: data.content, timestamp: data.timestamp});
                            }
                        });
                        resolve(messages);
                    }
                });
            });
        }
        , asChat: function (id) {
            this.conversationId = id || layui.common.asUuid();
            layui.layim.chat({
                type: 'ai'
                , id: config.title
                , username: `${config.title} v${config.version}`
                , avatar: `${config.base}style/imgs/logo.png`
                , new: true
                , enableLocalChatlog: false
                , layer: {
                    end: () => {
                        if (this.abortController) this.abortController.abort();
                        this.conversationId = layui.common.asUuid();
                        this.asReload();
                    }
                }
            });
        }
    });
})
;