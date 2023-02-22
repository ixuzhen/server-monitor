import React, { Component } from 'react';
import { Button, Layout } from 'antd';
import { useNavigate } from 'react-router-dom';
// const { Header} = Layout;
const AntdHeader = Layout.Header;

function Header() {
  let navigate = useNavigate();
  const click = () => {
    console.log('click');
    localStorage.removeItem('token');
    navigate('/login');
  };
  return (
    <AntdHeader
      className='site-layout-background'
      style={{
        // position: 'sticky',
        top: 0,
        zIndex: 1,
        padding: 0,
        backgroundColor: 'white',
      }}
    >
      <Button style={{ marginLeft: 20 }} onClick={click}>
        退出登录
      </Button>
    </AntdHeader>
  );
}

export default Header;
