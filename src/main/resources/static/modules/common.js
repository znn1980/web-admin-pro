/**
 * common
 */

layui.define(function (exports) {

    layui.$('button[name="sys-query-more"]').on('click', function () {
        layui.$(".sys-query-more").toggle();
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
        , asDay: function (day) {
            const ms = day ? day * (1000 * 60 * 60 * 24) : 0;
            return layui.util.toDateString(Date.now() + ms, 'yyyy-MM-dd');
        }
        , asAgo: function (ms) {
            const m = 1000 * 60, h = m * 60, d = h * 24;
            return `${parseInt(ms / d)}天${parseInt(ms % d / h)}小时${parseInt(ms % d % h / m)}分钟`;
        }
        , asRate: function (rate) {
            return (rate * 100).toFixed(2)
        }
        , asTree: function (id, items) {
            const trees = [];
            items.forEach(function (item) {
                if (item.pid === id) {
                    const child = common.asTree(item.id, items);
                    if (child.length) {
                        item.children = child;
                    }
                    trees.push(item);
                }
            });
            return trees;
        }
        , asCols: function (id, fields) {
            const cols = [];
            fields.forEach(function (field) {
                cols.push({field: field, hide: true});
            });
            switch (layui.admin.screen()) {
                case 0://低于768px的屏幕
                    layui.table.hideCol(id, cols.map(field => ({...field, hide: true})));
                    break;
                case 1://768px到992px之间的屏幕
                case 2://992px到1200px之间的屏幕
                    for (let i = 0; i < fields.length / 2; i++) {
                        cols[i].hide = false;
                    }
                    layui.table.hideCol(id, cols);
                    break;
                case 3://高于1200px的屏幕
                default:
                    layui.table.hideCol(id, cols.map(field => ({...field, hide: false})));
                    break;
            }
        }
    }
    exports('common', common);
});