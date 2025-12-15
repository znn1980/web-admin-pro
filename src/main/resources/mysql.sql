INSERT INTO `SYS_USER`
    (`ID`, `USERNAME`, `MOBILE`, `EMAIL`, `PASSWORD`, `SYS_ADMIN`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1, 'admin', '18633098531', 'admin@web-admin-pro.com', 'f5a7d7acaaca03a935a636bbe5ca9241', b'1', b'0', 'admin', now());
INSERT INTO `SYS_USER`
    (`ID`, `USERNAME`, `MOBILE`, `EMAIL`, `PASSWORD`, `SYS_ADMIN`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (2, 'guest', '13933092511', 'guest@web-admin-pro.com', 'f5a7d7acaaca03a935a636bbe5ca9241', b'0', b'1', 'admin', now());

INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (1, null, '系统管理', null, 1, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (2, 1, '用户管理', '/admin/user.html', 2, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (3, 1, '角色管理', '/admin/role.html', 3, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (4, 1, '菜单管理', '/admin/menu.html', 4, b'1', b'0', 'admin', now());
INSERT INTO `SYS_MENU`
    (`ID`, `PID`, `TITLE`, `URL`, `SORT`, `SYS_MENU`, `DISABLE`, `CREATE_USERNAME`, `CREATE_TIMESTAMP`)
VALUES (5, 1, '日志管理', '/admin/log.html', 5, b'1', b'0', 'admin', now());