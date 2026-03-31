/**
 * common
 */

layui.define(function (exports) {

    layui.table.set({skin: 'line', loading: true});

    layui.$('button.sys-query-more').on('click', function () {
        layui.$('.sys-query-more').toggle();
    });

    exports('common', {
        req: function (url, type, data, options) {
            const loading = layui.layer.load(2);
            layui.admin.req({
                url: url, type: type, data: data ? JSON.stringify(data) : {}
                , contentType: "application/json;charset=UTF-8"
                , complete: function () {
                    layui.layer.close(loading)
                    options && typeof options.complete === 'function' && options.complete();
                }
                , done: function (data) {
                    options && typeof options.done === 'function' && options.done(data);
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
        , asUuid: function () {
            if (crypto.randomUUID) return crypto.randomUUID();
            return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
                (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
            );
        }
    });
});