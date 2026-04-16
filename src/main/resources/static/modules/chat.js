/**
 * ai-chat
 */

layui.define(['layim', 'common'], function (exports) {

    let abortController
        , conversationId = layui.common.asUuid()
        , periodThinking = 0
        , md = markdownit({typographer: true, linkify: true, breaks: true});

    layui.form.on('checkbox(chat-tools-switch)', function (data) {
        if (data.elem.name === 'enable-thinking' || data.elem.name === 'enable-search') {
            layui.common.data({[data.elem.name]: data.elem.checked});
        }
        if (data.elem.name === 'mike') {
            layui.chat.asMike(function (text) {
                    layui.$('textarea.layim-scrollbar').val(text);
                }, function () {
                    data.elem.checked = false;
                }
            );
        }
    });

    layui.layim.config({
        contactsPanel: false
        , init: {
            user: {
                id: layui.common.data().username
                , username: layui.common.data().username
                , avatar: layui.common.data().avatar
            }
        }
    });

    layui.layim.extendChatTools([{
        name: 'mike', title: '语音输入', icon: 'layui-icon-mike'
        , onClick: function (data) {
            layui.chat.asMike(function (text) {
                layui.$('textarea.layim-scrollbar').val(text);
            });
        }
    }]);

    exports('chat', {
        asChat: function (id) {
            conversationId = id || layui.common.asUuid();
            layui.layim.chat({
                type: 'ai'
                , id: config.name
                , username: `${config.name} v${config.version}`
                , avatar: `${config.base}style/imgs/logo.png`
                , new: true
                , enableLocalChatlog: false
                , layer: {
                    end: () => {
                        if (abortController) abortController.abort();
                        conversationId = layui.common.asUuid();
                        this.asReload();
                    }
                }
            });
        }
        , asUpdate: function (id) {
            layui.common.req(`${config.base}ai/chat/all/${id}`, 'GET', null, {
                done: (data) => {
                    layui.layer.prompt({
                        title: '编辑对话名称', formType: 2, value: data.data.content
                        , skin: 'layui-layer-admin'
                        , success: function (layero) {
                            layero.addClass('layui-form').find('textarea')
                                .addClass('layui-textarea').attr('lay-affix', 'clear');
                            layui.form.render();
                            layero.find('textarea').focus();
                        }
                    }, (value, index, elem) => {
                        if (layui.$.trim(value) === '') return elem.focus();
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
            layui.layer.confirm(`&ensp;&ensp;&ensp;&ensp;删除后，${id ? '' : '全部'}聊天记录将不可恢复。`, {
                title: `确定删除${id ? '' : '历史'}对话？`, closeBtn: 0, icon: 3, skin: 'layui-layer-admin'
            }, (index) => {
                layui.layer.close(index);
                layui.common.req(`${config.base}ai/chat/${id ? id : 'all'}`, 'DELETE', null, {
                    done: () => {
                        this.asReload();
                    }
                });
            });
        }
        , asChatInit: function (data) {
            layui.$(data.elem).find('.layim-chat-tool').empty().html(`
                <span class="layui-form">
                  <input ${layui.common.data()['enable-thinking'] ? 'checked' : ''}
                   type="checkbox" name="enable-thinking" lay-skin="none" lay-filter="chat-tools-switch">
                  <div lay-checkbox class="chat-tools-skin layui-badge">
                    <i class="layui-icon layui-icon-component"></i>深度思考
                  </div>
                  <input ${layui.common.data()['enable-search'] ? 'checked' : ''}
                   type="checkbox" name="enable-search" lay-skin="none" lay-filter="chat-tools-switch">
                  <div lay-checkbox class="chat-tools-skin layui-badge">
                    <i class="layui-icon layui-icon-website"></i>联网搜索
                  </div>
                  <input type="checkbox" name="mike" lay-skin="none" lay-filter="chat-tools-switch">
                  <div lay-checkbox class="chat-tools-skin layui-badge">
                    <i class="layui-icon layui-icon-mike"></i>语音输入
                  </div>
                </span>
            `);
            layui.form.render();
            layui.layim.getMessage({
                system: true
                , id: data.data.id
                , type: data.data.type
                , content: config.copyright || data.data.username
                , saveLocalChatlog: false
            });
        }
        , asChatlog: function (data) {
            const user = data.user, receiver = data.receiver;
            return new Promise((resolve, reject) => {
                const messages = [];
                layui.common.req(`${config.base}ai/chat/${conversationId}`, 'GET', null, {
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
        , asContentParser: function (data) {
            return md.render(data).replaceAll('<table>', '<table class="layui-table" lay-size="sm">')
                .replaceAll(layui.util.escape('<think>'), `
                  <div class="layui-timeline">
                    <div class="layui-timeline-item">
                      <i class="layui-icon layui-timeline-axis layui-icon-component"></i>
                      <div class="layui-timeline-content layui-text">
                        <h6 class="layui-timeline-title">深度思考</h6>
                `)
                .replaceAll(layui.util.escape('</think>'), `
                      </div>
                    </div>
                    <div class="layui-timeline-item">
                      <i class="layui-icon layui-timeline-axis layui-icon-component"></i>
                      <div class="layui-timeline-content layui-text">
                        <h6 class="layui-timeline-title">已思考（用时${(periodThinking / 1000).toFixed(2)}秒）</h6>
                      </div>
                    </div>
                  </div>
                `);
        }
        , asSendMessage: function (data) {
            const user = data.user, receiver = data.receiver;
            if (receiver.type === 'ai') {
                let thinking = false, content = [];
                abortController = new AbortController();
                SSE.fetchEventSource(`${config.base}ai/chat/completions`, {
                    method: 'POST', headers: {'Content-Type': 'application/json'}
                    , body: JSON.stringify({
                        conversationId: conversationId
                        , question: user.content
                        , enableThinking: layui.common.data()['enable-thinking']
                        , enableSearch: layui.common.data()['enable-search']
                    })
                    , signal: abortController.signal
                    , onopen: function (response) {
                        if (!response.ok) throw new Error(`URL ${response.status} ${response.statusText}`);
                    }
                    , onmessage: function (msg) {
                        const data = JSON.parse(msg.data);
                        const reasoningContent = data?.result?.output?.metadata?.reasoningContent;
                        const text = data?.result?.output?.text;
                        if (layui.$.trim(reasoningContent)) {
                            if (thinking === false) {
                                thinking = true;
                                periodThinking = Date.now();
                                content.push('<think>');
                            }
                            content.push(reasoningContent);
                        }
                        if (layui.$.trim(text)) {
                            if (thinking === true) {
                                thinking = false;
                                periodThinking = Date.now() - periodThinking;
                                content.push('</think>');
                            }
                            content.push(text);
                        }
                        if (layui.$.trim(content.join(''))) {
                            layui.layim.getMessage({...receiver, content: `${content.join('')} _`, finished: false});
                        }
                    }
                    , onerror: function (error) {
                        layui.layim.getMessage({...receiver, content: `**错误信息：** ${error}`, finished: true});
                        throw error;
                    }
                    , onclose: function () {
                        layui.layim.getMessage({...receiver, content: content.join(''), finished: true});
                    }
                });
            }
        }
        , asBackgroundImage: function (data) {
            layui.$('.layui-layim-chat').css({
                'background-image': data.src ? 'url(' + data.src + ')' : 'none'
            });
        }
        , asMike: function (callback, end) {
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
                        typeof end === 'function' && end();
                    }
                });
            }
            recognition.onend = function () {
                layui.layer.close(loading);
                typeof end === 'function' && end();
            }
            recognition.onerror = function (event) {
                layui.layer.close(loading);
                layui.layer.msg(`语音识别错误：${event.error}`);
                typeof end === 'function' && end();
            }
            recognition.onresult = function (event) {
                const text = Array.from(event.results).map(result => result[0].transcript).join('');
                typeof callback === 'function' && callback(text);
            }
            recognition.start();
        }
        , asFlow: function (page, next) {
            layui.common.req(`${config.base}ai/chat/all?page=${page - 1}&limit=24`, 'GET', null, {
                done: (data) => {
                    data.data.forEach((item) => {
                        next(`
                            <div class="layui-col-xs6 layui-col-sm4 layui-col-md3 layui-col-lg3 layui-col-xl2">
                              <div class="layuiadmin-card-text">
                                <div class="layui-text-top">
                                  <i class="layui-icon layui-icon-dialogue"></i>
                                </div>
                                <p class="layui-text-center" id="chat-${item.conversationId}" lay-tips="${item.content}">${item.content}</p>
                                <p class="layui-text-bottom">
                                  <a id="chat-more-${item.conversationId}" href="javascript:" class="layui-icon layui-icon-more"></a>
                                  <span lay-tips="${item.timestamp}">${layui.util.timeAgo(item.timestamp)}</span>
                                </p>
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
        , asReload: function () {
            layui.$('#chat-flow').empty();
            layui.flow.reload('chat-flow');
        }
    });
});