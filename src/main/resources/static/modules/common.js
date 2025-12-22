/**
 * common
 */

layui.define(function (exports) {

    layui.$('#sys-query-more').on('click', function () {
        layui.$(".sys-query-more").toggle();
        layui.$(this).html(layui.$(this).html() === '收起' ? '展开' : '收起')
            .toggleClass('layui-icon-down').toggleClass('layui-icon-up');
    });

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
        , ago: function (ms) {
            const m = 1000 * 60, h = m * 60, d = h * 24;
            return `${parseInt(ms / d)}天${parseInt(ms % d / h)}小时${parseInt(ms % d % h / m)}分钟`;
        }
        , rate: function (total) {
            return (total * 100).toFixed(2)
        }
        , tgm: function (total) {
            const kb = 1024, mb = kb * kb, gb = mb * kb, tb = gb * kb;
            if (total >= tb) {
                return (total / tb).toFixed(2) + 'TB';
            } else if (total >= gb) {
                return (total / gb).toFixed(2) + 'GB';
            } else if (total >= mb) {
                return (total / mb).toFixed(2) + 'MB';
            } else if (total >= kb) {
                return (total / kb).toFixed(2) + 'KB';
            } else {
                return total + 'B';
            }
        }
        , tree: function (id, items) {
            const trees = [];
            items.forEach(function (item) {
                if (item.pid === id) {
                    const child = common.tree(item.id, items);
                    if (child.length) {
                        item.children = child;
                    }
                    trees.push(item);
                }
            });
            return trees;
        }
    }
    exports('common', common);
});