INSERT INTO `SYS_USER`
    (`ID`, `USERNAME`, `PHONE`, `EMAIL`, `PASSWORD`, `SYS_ADMIN`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1, 'admin', '18888888888', 'admin@admin.com', '37b062c8ebfe6c3480f5c8b5753fcae8', b'1', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `ICON`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (100, null, 'AI对话', 'GET', '/admin/chat.html', 'layui-icon layui-icon-dialogue', 100, b'1', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `ICON`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1000, null, '系统管理', null, null, 'layui-icon layui-icon-app', 1000, b'1', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `ICON`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1100, 1000, '用户管理', 'GET', '/admin/user.html', 'layui-icon layui-icon-username', 1100, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `ICON`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1200, 1000, '角色管理', 'GET', '/admin/role.html', 'layui-icon layui-icon-auz', 1200, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `ICON`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1300, 1000, '菜单管理', 'GET', '/admin/menu.html', 'layui-icon layui-icon-tabs', 1300, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `ICON`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1800, 1000, '通知公告', 'GET', '/admin/notice.html', 'layui-icon layui-icon-notice', 1800, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `ICON`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1900, 1000, '日志管理', 'GET', '/admin/log.html', 'layui-icon layui-icon-survey', 1900, b'1', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1110, 1100, '查询用户', 'POST', '/sys/user/all.json', 1110, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1120, 1100, '创建用户', 'POST', '/sys/user/create.json', 1120, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1130, 1100, '修改用户', 'PUT', '/sys/user/update.json', 1130, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1140, 1100, '删除用户', 'DELETE', '/sys/user/delete.json', 1140, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1150, 1100, '修改密码', 'PUT', '/sys/user/pass.json', 1150, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1160, 1100, '重置密码', 'PUT', '/sys/user/reset.json', 1160, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1210, 1200, '查询角色', 'GET', '/sys/role/all.json', 1210, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1220, 1200, '移动角色', 'PUT', '/sys/role/move.json', 1220, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1230, 1200, '创建角色', 'POST', '/sys/role/create.json', 1230, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1240, 1200, '修改角色', 'PUT', '/sys/role/update.json', 1240, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1250, 1200, '删除角色', 'DELETE', '/sys/role/delete.json', 1250, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1310, 1300, '查询菜单', 'GET', '/sys/menu/all.json', 1310, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1320, 1300, '移动菜单', 'PUT', '/sys/menu/move.json', 1320, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1330, 1300, '创建菜单', 'POST', '/sys/menu/create.json', 1330, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1340, 1300, '修改菜单', 'PUT', '/sys/menu/update.json', 1340, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1350, 1300, '删除菜单', 'DELETE', '/sys/menu/delete.json', 1350, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1810, 1800, '创建通知公告', 'POST', '/sys/notice/create.json', 1810, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1820, 1800, '修改通知公告', 'PUT', '/sys/notice/update.json', 1820, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1830, 1800, '删除通知公告', 'DELETE', '/sys/notice/delete.json', 1830, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1910, 1900, '查询日志', 'POST', '/sys/log/all.json', 1910, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1920, 1900, '删除日志', 'DELETE', '/sys/log/delete.json', 1920, b'0', b'0', 'admin', now());