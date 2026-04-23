INSERT INTO `SYS_USER`
    (`ID`, `USERNAME`, `PHONE`, `EMAIL`, `PASSWORD`, `SYS_ADMIN`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1, 'admin', '18888888888', 'admin@admin.com', '37b062c8ebfe6c3480f5c8b5753fcae8', b'1', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1, null, '系统管理', null, null, 1, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (2, 1, '用户管理', 'GET', '/admin/user.html', 2, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (3, 1, '角色管理', 'GET', '/admin/role.html', 3, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (4, 1, '菜单管理', 'GET', '/admin/menu.html', 4, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (5, 1, '日志管理', 'GET', '/admin/log.html', 5, b'1', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (6, 2, '查询用户', 'POST', '/sys/user/all.json', 6, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (7, 2, '创建用户', 'POST', '/sys/user/create.json', 7, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (8, 2, '修改用户', 'PUT', '/sys/user/update.json', 8, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (9, 2, '删除用户', 'DELETE', '/sys/user/delete.json', 9, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (10, 2, '修改密码', 'PUT', '/sys/user/pass.json', 10, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (11, 2, '重置密码', 'PUT', '/sys/user/reset.json', 11, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (12, 3, '查询角色', 'GET', '/sys/role/all.json', 12, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (13, 3, '移动角色', 'PUT', '/sys/role/move.json', 13, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (14, 3, '创建角色', 'POST', '/sys/role/create.json', 14, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (15, 3, '修改角色', 'PUT', '/sys/role/update.json', 15, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (16, 3, '删除角色', 'DELETE', '/sys/role/delete.json', 16, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (17, 4, '查询菜单', 'GET', '/sys/menu/all.json', 17, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (18, 4, '移动菜单', 'PUT', '/sys/menu/move.json', 18, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (19, 4, '创建菜单', 'POST', '/sys/menu/create.json', 19, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (20, 4, '修改菜单', 'PUT', '/sys/menu/update.json', 20, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (21, 4, '删除菜单', 'DELETE', '/sys/menu/delete.json', 21, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (22, 5, '查询日志', 'POST', '/sys/log/all.json', 22, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (23, 5, '删除日志', 'DELETE', '/sys/log/delete.json', 23, b'0', b'0', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (24, null, '通知公告', null, null, 24, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (25, 24, '创建通知公告', 'POST', '/sys/notice/create.json', 25, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (26, 24, '修改通知公告', 'PUT', '/sys/notice/update.json', 26, b'0', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `METHOD`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (27, 24, '删除通知公告', 'DELETE', '/sys/notice/delete.json', 27, b'0', b'0', 'admin', now());