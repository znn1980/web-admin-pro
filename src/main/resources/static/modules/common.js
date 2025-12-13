/**
 * common
 */

layui.define(function (exports) {

    layui.admin.events.more = function () {
        layui.$(".layui-inline.sys-more").toggle();
        layui.$('#sys-more').html(layui.$('#sys-more').html() === '收起' ? '展开' : '收起')
            .toggleClass('layui-icon-down').toggleClass('layui-icon-up');
    }

    const common = {
        req: function (url, type, data, options) {
            let loading;
            layui.admin.req({
                url: url, type: type, data: data ? JSON.stringify(data) : {}
                , contentType: "application/json;charset=UTF-8"
                , beforeSend: function () {
                    loading = layui.layer.load();
                }
                , complete: function () {
                    loading ? layui.layer.close(loading) : layui.layer.closeAll('loading');
                    typeof options.complete === 'function' && options.complete();
                }
                , done: function (data) {
                    typeof options.done === 'function' && options.done(data);
                }
            });
        }
        , ymd: function (ms) {
            return layui.util.toDateString(Date.now() + ((ms) ? ms : 0), 'yyyy-MM-dd');
        }
    }
    exports('common', common);
});