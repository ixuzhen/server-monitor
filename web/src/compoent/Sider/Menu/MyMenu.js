import React, { useState } from 'react';
import {
  AppstoreOutlined,
  ContainerOutlined,
  DesktopOutlined,
  MailOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  PieChartOutlined,
} from '@ant-design/icons';
import { Button, Menu } from 'antd';
import { Link } from 'react-router-dom';
function getItem(label, key, icon, children, type) {
  return {
    key,
    icon,
    children,
    label,
    type,
  };
}
const items = [
  getItem(
    <Link to='/hostinfo'>主机列表</Link>,
    '/hostinfo',
    <PieChartOutlined />
  ),
  // getItem('主机列表', '/content', <PieChartOutlined />),
  getItem(<Link to='/gpuhost'>显卡信息</Link>, '/gpuhost', <DesktopOutlined />),

  getItem(
    <Link to='/message'>消息推送</Link>,
    '/message',
    <ContainerOutlined />
  ),
  getItem(
    <Link to='/terminal'>远程终端</Link>,
    '/terminal',
    <ContainerOutlined />
  ),
  getItem('管理', 'sub1', <MailOutlined />, [
    getItem(<Link to='/warningPush'>警告推送</Link>, 'warning push'),
    getItem('人员管理', '6'),
    getItem('Option 7', '7'),
    getItem('Option 8', '8'),
  ]),
  getItem('Navigation Two', 'sub2', <AppstoreOutlined />, [
    getItem('Option 9', '9'),
    getItem('Option 10', '10'),
    getItem('Submenu', 'sub3', null, [
      getItem('Option 11', '11'),
      getItem('Option 12', '12'),
    ]),
  ]),
];
const MyMenu = () => {
  const menuClick = (e) => {
    console.log(e.key);
  };
  return (
    <Menu
      defaultSelectedKeys={['1']}
      defaultOpenKeys={['sub1']}
      mode='inline'
      theme='light'
      // inlineCollapsed={collapsed}
      items={items}
      // onClick={menuClick}
    />
  );
};
export default MyMenu;
