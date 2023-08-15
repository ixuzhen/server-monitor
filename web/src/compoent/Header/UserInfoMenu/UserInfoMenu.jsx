import { DownOutlined, UserOutlined } from '@ant-design/icons';
import React from 'react';
import MessagePush from '../../Content/message/MessagePush';
import { Button, Dropdown, Space } from 'antd';
import { useNavigate } from 'react-router-dom';

const items = [
  // {
  //   label: '用户信息',
  //   key: '1',
  //   icon: <UserOutlined />,
  // },
  {
    label: '退出登录',
    key: '2',
    icon: <UserOutlined />,
  },
];

const UserInfoMenu = () => {
  let navigate = useNavigate();
  const logOutClick = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const handleMenuClick = (e) => {
    var key = e.key;
    if (key === '1') {
      console.log('click用户信息');
    } else if (key === '2') {
      logOutClick();
    }
  };
  const menuProps = {
    items,
    onClick: handleMenuClick,
  };

  return (
    <div>
      <Dropdown menu={menuProps}>
        <Button>
          <Space>
            用户
            <DownOutlined />
          </Space>
        </Button>
      </Dropdown>
    </div>
  );
};
export default UserInfoMenu;
