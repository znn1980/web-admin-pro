/**
 * common
 */

layui.define(function (exports) {

    layui.admin.events.more = function () {
        layui.$(".layui-inline.more").toggle();
        layui.$('#more').html(layui.$('#more').html() === '\n收起' ? '\n展开' : '\n收起')
            .toggleClass('layui-icon-down').toggleClass('layui-icon-up');
    }

    const common = {
        req: function (url, type, data, options) {
            layui.admin.req({
                url: url, type: type, data: data ? JSON.stringify(data) : {}
                , contentType: "application/json;charset=UTF-8"
                , beforeSend: function () {
                    layui.layer.load();
                }
                , complete: function () {
                    layui.layer.closeAll('loading');
                }
                , done: function (data) {
                    options.done(data);
                }
            });
        }
        , ymd: function (ms) {
            return layui.util.toDateString(Date.now() + ((ms) ? ms : 0), 'yyyy-MM-dd');
        }
    }
    exports('common', common);
});