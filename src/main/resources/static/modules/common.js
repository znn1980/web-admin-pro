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
        , asCols: function (that, id) {
            const fields = layui.data(id || that.id);
            that.cols[0].forEach(function (col) {
                if (col.field in fields) {
                    layui.table.hideCol(that.id, {field: col.field, hide: fields[col.field]});
                }
            });
            that.elem.next().on('mousedown', 'input[lay-filter="LAY_TABLE_TOOL_COLS"]+', function () {
                const field = layui.$(this).prev()[0];
                layui.data(id || that.id, {key: field.name, value: field.checked});
            });
        }
        , _asCols: function (id, fields, cols) {
            let queries = 0.0;
            switch (layui.admin.screen()) {
                case 0://低于768px的屏幕
                    queries = cols && cols.xs && cols.xs >= 0 && cols.xs <= 1 ? cols.xs : 0;
                    break;
                case 1://768px到992px之间的屏幕
                    queries = cols && cols.sm && cols.sm >= 0 && cols.sm <= 1 ? cols.sm : 0.3;
                    break;
                case 2://992px到1200px之间的屏幕
                    queries = cols && cols.md && cols.md >= 0 && cols.md <= 1 ? cols.md : 0.7;
                    break;
                case 3://高于1200px的屏幕
                    queries = cols && cols.lg && cols.lg >= 0 && cols.lg <= 1 ? cols.lg : 1;
                    break;
                default:
                    queries = cols && cols.xl && cols.xl >= 0 && cols.xl <= 1 ? cols.xl : 1;
                    break;
            }
            layui.table.hideCol(id, fields.map(function (field, index) {
                return {field: field, hide: index >= Math.round(fields.length * queries)};
            }));
        }
    }
    exports('common', common);
});