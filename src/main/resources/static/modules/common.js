/**
 * common
 */

layui.define(function (exports) {

    const localName = 'web-admin-pro'
        , localMore = layui.$('button.sys-query-more').data('localMore');
    if (localData()[localMore]) {
        layui.$('.sys-query-more').toggle();
    }

    layui.$('button.sys-query-more').on('click', function () {
        layui.$('.sys-query-more').toggle();
        localData({[localMore]: !localData()[localMore]});
    });

    layui.table.set({skin: 'line', loading: true});

    function localData(settings, uid) {
        if (uid) layui.data(localName, {key: 'uid', value: uid});
        const local = layui.data(localName).uid;
        const data = layui.data(localName)[local] || {};
        if (settings) layui.data(localName, {key: local, value: {...data, ...settings}})
        return data;
    }

    exports('common', {
        req: function (url, type, data, callback) {
            const loading = layui.layer.load(2);
            layui.admin.req({
                url: url, type: type, data: data ? typeof data !== 'string' ? JSON.stringify(data) : data : {}
                , contentType: "application/json;charset=UTF-8"
                , done: function (data) {
                    callback && typeof callback.done === 'function' && callback.done(data);
                }
                , success: function (data) {
                    const response = layui.setter.response;
                    const statusCode = response.statusCode;
                    if (data[response.statusName] !== statusCode.ok) {
                        callback && typeof callback.fail === 'function' && callback.fail();
                    }
                }
                , error: function () {
                    callback && typeof callback.fail === 'function' && callback.fail();
                }
                , complete: function () {
                    layui.layer.close(loading)
                    callback && typeof callback.always === 'function' && callback.always();
                }
            });
        }
        , data: function (settings, uid) {
            return localData(settings, uid);
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
            const fields = localData()[id || that.id] || {};
            that.cols[0].forEach(function (col) {
                if (col.field in fields) {
                    layui.table.hideCol(that.id, {field: col.field, hide: fields[col.field]});
                }
            });
            that.elem.next().on('mousedown', 'input[lay-filter="LAY_TABLE_TOOL_COLS"]+', function () {
                const field = layui.$(this).prev()[0];
                const fields = localData()[id || that.id] || {};
                localData({[id || that.id]: {...fields, [field.name]: field.checked}});
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