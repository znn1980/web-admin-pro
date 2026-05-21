INSERT INTO SYS_USER
    (ID, USERNAME, PHONE, EMAIL, PASSWORD, SYS_ADMIN, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1, 'admin', '18888888888', 'admin@admin.com', '37b062c8ebfe6c3480f5c8b5753fcae8', true, false, 'admin', now());

SELECT SETVAL('SYS_USER_ID_SEQ', MAX(ID))
FROM SYS_USER;

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (100, null, 'AI对话', 'GET', '/admin/chat.html', 'layui-icon layui-icon-dialogue', 100, true, false, 'admin', now());

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
VALUES (1800, 1000, '通知公告', 'GET', '/admin/notice.html', 'layui-icon layui-icon-notice', 1800, true, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, ICON, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1900, 1000, '日志管理', 'GET', '/admin/log.html', 'layui-icon layui-icon-survey', 1900, true, false, 'admin', now());

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
VALUES (1810, 1800, '创建通知公告', 'POST', '/sys/notice/create.json', 1810, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1820, 1800, '修改通知公告', 'PUT', '/sys/notice/update.json', 1820, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1823, 1800, '删除通知公告', 'DELETE', '/sys/notice/delete.json', 1823, false, false, 'admin', now());

INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1910, 1900, '查询日志', 'POST', '/sys/log/all.json', 1910, false, false, 'admin', now());
INSERT INTO SYS_MENU
    (ID, PID, TITLE, METHOD, URL, SORT, SYS_MENU, DISABLE, CREATE_USERNAME, CREATE_TIMESTAMP)
VALUES (1920, 1900, '删除日志', 'DELETE', '/sys/log/delete.json', 1920, false, false, 'admin', now());

SELECT SETVAL('SYS_MENU_ID_SEQ', MAX(ID))
FROM SYS_MENU;