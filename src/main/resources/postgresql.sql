INSERT INTO SYS_USER
    (ID, USERNAME, PHONE, EMAIL, PASSWORD, SYS_ADMIN, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1, 'admin', '18888888888', 'admin@admin.com', '37b062c8ebfe6c3480f5c8b5753fcae8', true, false, 'admin', now());

SELECT SETVAL('SYS_USER_ID_SEQ', MAX(ID))
FROM SYS_USER;

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (100, null, '智能助手', 'GET', '/admin/chat.html', 'layui-icon layui-icon-dialogue', 100, true, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (200, null, '通知公告', 'GET', '/admin/notice.html', 'layui-icon layui-icon-notice', 200, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (210, 200, '创建通知公告', 'POST', '/sys/notice/create.json', 210, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (220, 200, '修改通知公告', 'PUT', '/sys/notice/update.json', 220, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (230, 200, '删除通知公告', 'DELETE', '/sys/notice/delete.json', 230, false, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1000, null, '系统管理', null, null, 'layui-icon layui-icon-app', 1000, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1100, 1000, '用户管理', 'GET', '/admin/user.html', 'layui-icon layui-icon-username', 1100, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1200, 1000, '角色管理', 'GET', '/admin/role.html', 'layui-icon layui-icon-auz', 1200, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1300, 1000, '菜单管理', 'GET', '/admin/menu.html', 'layui-icon layui-icon-tabs', 1300, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1600, 1000, '字典管理', 'GET', '/admin/dict.html', 'layui-icon layui-icon-read', 1600, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1700, 1000, '参数管理', 'GET', '/admin/config.html', 'layui-icon layui-icon-form', 1700, true, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1110, 1100, '查询用户', 'POST', '/sys/user/all.json', 1110, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1120, 1100, '创建用户', 'POST', '/sys/user/create.json', 1120, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1130, 1100, '修改用户', 'PUT', '/sys/user/update.json', 1130, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1140, 1100, '删除用户', 'DELETE', '/sys/user/delete.json', 1140, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1150, 1100, '修改密码', 'PUT', '/sys/user/pass.json', 1150, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1160, 1100, '重置密码', 'PUT', '/sys/user/reset.json', 1160, false, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1210, 1200, '查询角色', 'GET', '/sys/role/all.json', 1210, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1220, 1200, '移动角色', 'PUT', '/sys/role/move.json', 1220, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1230, 1200, '创建角色', 'POST', '/sys/role/create.json', 1230, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1240, 1200, '修改角色', 'PUT', '/sys/role/update.json', 1240, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1250, 1200, '删除角色', 'DELETE', '/sys/role/delete.json', 1250, false, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1310, 1300, '查询菜单', 'GET', '/sys/menu/all.json', 1310, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1320, 1300, '移动菜单', 'PUT', '/sys/menu/move.json', 1320, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1330, 1300, '创建菜单', 'POST', '/sys/menu/create.json', 1330, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1340, 1300, '修改菜单', 'PUT', '/sys/menu/update.json', 1340, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1350, 1300, '删除菜单', 'DELETE', '/sys/menu/delete.json', 1350, false, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1610, 1600, '查询字典', 'POST', '/sys/dict/**/all.json*', 1610, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1620, 1600, '移动字典', 'PUT', '/sys/dict/**/move.json', 1620, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1630, 1600, '创建字典', 'POST', '/sys/dict/**/create.json', 1630, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1640, 1600, '修改字典', 'PUT', '/sys/dict/**/update.json', 1640, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1650, 1600, '删除字典', 'DELETE', '/sys/dict/**/delete.json', 1650, false, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1710, 1700, '查询参数', 'POST', '/sys/config/all.json', 1710, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1720, 1700, '创建参数', 'POST', '/sys/config/create.json', 1720, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1730, 1700, '修改参数', 'PUT', '/sys/config/update.json', 1730, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1740, 1700, '删除参数', 'DELETE', '/sys/config/delete.json', 1740, false, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (2000, null, '系统监控', null, null, 'layui-icon layui-icon-console', 2000, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (2100, 2000, '服务监控', 'GET', '/admin/monitor.html', 'layui-icon layui-icon-chart', 2100, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (2200, 2000, '系统日志', 'GET', '/admin/log.html', 'layui-icon layui-icon-survey', 2200, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (2210, 2200, '查询日志', 'POST', '/sys/log/all.json', 2210, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (2220, 2200, '删除日志', 'DELETE', '/sys/log/delete.json', 2220, false, false, 'admin', now());

SELECT SETVAL('SYS_MENU_ID_SEQ', MAX(ID))
FROM SYS_MENU;