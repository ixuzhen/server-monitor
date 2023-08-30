import React from 'react';
import {

  ContainerOutlined,
  DesktopOutlined,
  MailOutlined,
  PieChartOutlined,
} from '@ant-design/icons';
import { Menu } from 'antd';
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
  // getItem(<Link to='/gpuhost'>显卡信息</Link>, '/gpuhost', <DesktopOutlined />),
  // getItem(
  //   <Link to='/gpuhostip'>显卡信息</Link>,
  //   '/gpuhost',
  //   <DesktopOutlined />
  // ),

  getItem('显卡信息', 'gpu', <DesktopOutlined />, [
    getItem(
      <Link to='/gpuhostip'>主机 IP</Link>,
      '/gpuhost',
      <DesktopOutlined />
    ),
    getItem(
      <Link to='/gpuused'>空闲显卡</Link>,
      '/gpuused',
      <DesktopOutlined />
    ),
    getItem(
      <Link to='/gpuunused'>已使用显卡</Link>,
      '/gpuunused',
      <DesktopOutlined />
    ),
  ]),
  getItem(
    <Link to='/dockerhost'>Docker 信息</Link>,
    '/dockerhost',
    <ContainerOutlined />
  ),
  getItem(
    <Link to='/terminal'>远程终端</Link>,
    '/terminal',
    <ContainerOutlined />
  ),

  getItem(
    <Link to='/message'>消息推送</Link>,
    '/message',
    <ContainerOutlined />
  ),
  // getItem(
  //   <Link to='/terminal'>远程终端</Link>,
  //   '/terminal',
  //   <ContainerOutlined />
  // ),
  getItem('管理', 'sub1', <MailOutlined />, [
    getItem(<Link to='/warningPush'>警告推送</Link>, 'warning push'),
    // getItem('人员管理', '6'),
  ]),
];
const MyMenu = () => {
  const menuClick = (e) => {
    console.log(e.key);
  };
  return (
    <Menu
      defaultSelectedKeys={['1']}
      defaultOpenKeys={['gpu']}
      mode='inline'
      theme='light'
      // inlineCollapsed={collapsed}
      items={items}
      // onClick={menuClick}
    />
  );
};
export default MyMenu;
