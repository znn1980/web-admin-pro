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
        , onClick: function (data) {
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
                data.insert(Array.from(event.results).map(result => result[0].transcript).join(''));
            }
            recognition.start();
        }
    }, {
        name: 'upload', title: '上传附件', icon: 'layui-icon-upload-drag'
        , onClick: function (data) {
            layui.layer.msg('敬请期待！');
        }
    }, {
        name: 'more', title: '更多', icon: 'layui-icon-more'
        , onClick: function (data) {
            layui.dropdown.render({
                elem: data.elem, show: true
                , content: `
                <div class="layui-form layui-padding-2">
                  <input ${enable_thinking ? 'checked' : ''} type="checkbox"
                   name="enable-thinking" value="深度思考" lay-skin="none" lay-filter="chat-switch">
                  <div lay-checkbox class="chat-skin-tag layui-badge">
                    <i class="layui-icon layui-icon-component"></i> 深度思考
                  </div>
                  <input ${enable_search ? 'checked' : ''} type="checkbox"
                   name="enable-search" value="智能搜索" lay-skin="none" lay-filter="chat-switch">
                  <div lay-checkbox class="chat-skin-tag layui-badge">
                    <i class="layui-icon layui-icon-website"></i> 智能搜索
                  </div>
                </div>
                `
                , ready: function () {
                    layui.form.render();
                }
            });
        }
    }]);

    let enable_thinking = false, enable_search = false;
    layui.form.on('checkbox(chat-switch)', function (data) {
        if (data.elem.name === 'enable-thinking') {
            enable_thinking = data.elem.checked;
        }
        if (data.elem.name === 'enable-search') {
            enable_search = data.elem.checked;
        }
    });

    exports('chat', {
        abortController: null, conversationId: layui.common.asUuid()
        , asReload: function () {
            layui.$('#chat-flow').html('');
            layui.flow.reload('chat-flow');
        }
        , asUpdate: function (id) {
            layui.common.req(`${config.base}ai/chat/all/${id}`, 'GET', null, {
                done: (data) => {
                    layui.layer.prompt({
                        title: '编辑对话名称', formType: 2, value: data.data.content
                    }, (value, index, elem) => {
                        if (layer.$.trim(value) === '') return elem.focus();
                        layer.close(index);
                        layui.common.req(`${config.base}ai/chat/${id}`, 'PUT', value, {
                            done: () => {
                                this.asReload();
                            }
                        });
                    });
                }
            });
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
                layui.common.req(`${config.base}ai/chat/${this.conversationId}`, 'GET', null, {
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
        , asFlow: function (page, next) {
            layui.common.req(`${config.base}ai/chat/all?page=${page - 1}&limit=24`, 'GET', null, {
                done: (data) => {
                    data.data.forEach((item) => {
                        next(`
                            <div class="layui-col-md2 layui-col-sm4 layui-col-xs6">
                              <div class="cmdlist-container">
                                <div class="cmdlist-text">
                                  <p id="chat-${item.conversationId}" lay-tips="${item.content}"
                                   class="info layui-ellip" style="font-weight:bold;">${item.content}</p>
                                  <div class="price">
                                    <a id="chat-more-${item.conversationId}" href="javascript:" class="layui-icon layui-icon-more"></a>
                                    <span class="flow" lay-tips="${item.timestamp}">${layui.util.timeAgo(item.timestamp)}</span>
                                  </div>
                                </div>
                              </div>
                            </div>`, data.count > 0);
                        layui.$(`#chat-${item.conversationId}`).on('click', () => {
                            this.asChat(item.conversationId);
                        });
                        layui.dropdown.render({
                            elem: `#chat-more-${item.conversationId}`
                            , data: [
                                {
                                    id: `${item.conversationId}`, title: 'update'
                                    , templet: '<span><i class="layui-icon layui-icon-edit"></i> 重命名</span>'
                                }
                                , {
                                    id: `${item.conversationId}`, title: 'delete'
                                    , templet: '<span style="color: red;"><i class="layui-icon layui-icon-delete"></i> 删除</span>'
                                }]
                            , click: (data) => {
                                if (data.title === 'update') {
                                    this.asUpdate(data.id)
                                }
                                if (data.title === 'delete') {
                                    this.asDelete(data.id)
                                }
                            }
                        });
                    });
                    next('', data.count > 0);
                }
            });
        }
    });
})
;