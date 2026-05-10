/**
 * common
 */

layui.define(function (exports) {
    const common = {
        LOCAL_STORAGE_NAME: 'web-admin-pro'
        , req: function (url, type, data, callback) {
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
            if (uid) layui.data(this.LOCAL_STORAGE_NAME, {key: 'uid', value: uid});
            const local = layui.data(this.LOCAL_STORAGE_NAME).uid;
            const data = layui.data(this.LOCAL_STORAGE_NAME)[local] || {};
            if (settings) layui.data(this.LOCAL_STORAGE_NAME, {key: local, value: {...data, ...settings}})
            return data;
        }
        , asDay: function (day) {
            const ms = day ? day * (1000 * 60 * 60 * 24) : 0;
            return layui.util.toDateString((Date.now() + ms), 'yyyy-MM-dd');
        }
        , asAgo: function (ms) {
            const m = 1000 * 60, h = m * 60, d = h * 24;
            return `${parseInt(ms / d)}天${parseInt(ms % d / h)}小时${parseInt(ms % d % h / m)}分钟`;
        }
        , asRate: function (rate) {
            return (rate * 100).toFixed(2)
        }
        , asCols: function (data, id) {
            const fields = this.data()[id || data.id] || {};
            data.cols[0].forEach(function (col) {
                if (col.field in fields) {
                    layui.table.hideCol(data.id, {field: col.field, hide: fields[col.field]});
                }
            });
            const that = this;
            data.elem.next().on('mousedown', 'input[lay-filter="LAY_TABLE_TOOL_COLS"]+', function () {
                const field = layui.$(this).prev()[0];
                const fields = that.data()[id || data.id] || {};
                that.data({[id || data.id]: {...fields, [field.name]: field.checked}});
            });
        }
        , asUuid: function () {
            if (crypto.randomUUID) return crypto.randomUUID();
            return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
                (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
            );
        }
        , asFlatToTree: function (data) {
            data = data.map(function (data) {
                if (!data.children) data.children = [];
                return data;
            });
            return layui.lay.flatToTree(data, {parentKey: 'pid'});
        }
        , asWhois: function (ip, callback) {
            this.req(`${config.base}sys/whois?ip=${ip}`, 'GET', null, {
                done: function (data) {
                    let whois = data.data;
                    whois = (whois.pro || whois.city || whois.region)
                        ? [whois.pro, whois.city, whois.region].join('') : whois.addr
                    typeof callback === 'function' && callback(whois);
                }
            });
        }
    }

    if (common.data()[layui.$('button.sys-query-more').data('queryMore')]) {
        layui.$('.sys-query-more').toggle();
    }

    layui.$('button.sys-query-more').on('click', function () {
        layui.$('.sys-query-more').toggle();
        const data = layui.$(this).data('queryMore');
        common.data({[data]: !common.data()[data]});
    });

    layui.util.on({
        Whois: function (elem) {
            common.asWhois(elem.text(), (whois) => {
                layui.layer.tips(whois, this);
            });
        }
    });

    layui.table.set({skin: 'line', loading: true});

    exports('common', common);
});