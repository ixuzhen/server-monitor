import React, { useState } from 'react';
import { Button, Form, Input, Modal } from 'antd';
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';

const Terminal = () => {

  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);


  // 弹窗控制
  const showModal = () => {
    setOpen(true);
  };
  const handleOk = () => {
    setConfirmLoading(true);
    setTimeout(() => {
      setOpen(false);
      setConfirmLoading(false);
    }, 2000);
  };
  const handleCancel = () => {
    console.log('Clicked cancel button');
    setOpen(false);
  };


  // 弹窗后的表格
  const onFinish = (values) => {
    console.log('Success:', values);
  };
  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };


  // 连接按钮
  function buttonClick() {
    if (true) {
      buildWebSocket();
    }
  }
  function buildWebSocket() {
    const w = window.open('_black') //这里是打开新窗口
    let url = '/webssh'
    w.location.href = url //这样就可以跳转了
  }


  // 风格
  const inputStylePrefix = { width:"70px" ,textAlign:"center"};

  return (

    <div>
      {/*弹窗按钮*/}
      <Button type="primary" onClick={showModal}>
        New Host
      </Button>
      {/*弹窗显示的内容*/}
      <Modal
        title="Title"
        open={open}
        // onOk={handleOk}
        confirmLoading={confirmLoading}
        // onCancel={handleCancel}
        footer={[null]}
      >
        <Form
          name='normal_login'
          className='login-form'
          initialValues={{
            remember: true
          }}
          onFinish={onFinish}
        >

          <Form.Item
            name='address'
            rules={[
              {
                required: true,
                message: 'Please input your Address!'
              }
            ]}
          >
            <Input
              style={{ height: '40px' }}
              // prefix={<UserOutlined className='site-form-item-icon' />}
              prefix={<div style={inputStylePrefix}>Address</div>}
              placeholder='请输入Address'
            />
          </Form.Item>


          <Form.Item
            name='port'
            rules={[
              {
                required: true,
                message: 'Please input your Port!'
              }
            ]}
          >
            <Input
              style={{ height: '40px' }}
              // prefix={<UserOutlined className='site-form-item-icon' />}
              prefix={<div style={inputStylePrefix}>Port</div>}
              placeholder='请输入Port'
            />
          </Form.Item>

          <Form.Item
            name='username'
            rules={[
              {
                required: true,
                message: 'Please input your Username!'
              }
            ]}
          >
            <Input
              style={{ height: '40px' }}
              // prefix={<LockOutlined className='site-form-item-icon' />}
              prefix={<div style={inputStylePrefix}>Username</div>}
              placeholder='请输入用户名'
            />

          </Form.Item>

          <Form.Item
            name='password'
            rules={[
              {
                required: true,
                message: 'Please input your Password!'
              }
            ]}
          >
            <Input.Password
              style={{ height: '40px' }}
              // prefix={<LockOutlined className='site-form-item-icon' />}
              prefix={<div style={inputStylePrefix}>Password</div>}
              type='password'
              placeholder='请输入Password'
              iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
            />

          </Form.Item>





          <Form.Item>
            <div style={{ textAlign: 'center' }}>
              <Button
                // className='login-form-button'
                style={{ width: '120px', height: '35px' }}
                // onClick={}
              >
                注册
              </Button>
              <Button
                type='primary'
                htmlType='submit'
                className='login-form-button'
                style={{ width: '120px', height: '35px', marginLeft: '20px' }}
              >
                登录
              </Button>
            </div>

          </Form.Item>
        </Form>
      </Modal>

      <Button onClick={buttonClick}>连接</Button>
    </div>
  );
};

export default Terminal;
