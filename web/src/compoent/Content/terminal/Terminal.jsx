import React, { useEffect, useState } from 'react';
import { Button, Col, Form, Input, Modal, Row } from 'antd';
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import SshHostCard from './SshHostCard';
import { API } from '../../../request';
import { isSuccess, showErrorWithCode, showSuccess } from '../../../helper/utils';

const Terminal = () => {

  const [form] = Form.useForm();
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  // const [address, setAddress] = useState('123');
  // const [port, setPort] = useState('112');
  // const [username, setUsername] = useState('212');
  // const [password, setPassword] = useState('');

  const [data, setData] = useState([]); // 保存数据的数组
  const [edit, setEdit] = useState(false); // 是否是编辑sshhost模式
  const [sshHostId, setSshHostId] = useState(-1);


  const loadSshHostInfo = async () => {
    const res = await API.get('/ssh/shhHost');
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      setData(data);
      console.log(data);
    } else {
      showErrorWithCode(code,'加载数据失败' + message);
    }
  };

  useEffect(() => {
    loadSshHostInfo()
      .then()
      .catch((reason) => {
        console.log(reason);
      });
  }, []);


  // 弹窗控制
  const showModal = () => {
    setOpen(true);
  };
  const handleOk = () => {
    // form.setFieldValue('address', address);
    // form.setFieldValue('port', port);
    // form.setFieldValue('username', username);
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
  const onFinish = async (values) => {
    console.log('Success:', values);
    setConfirmLoading(true);
    const {id, address, port, username, password } = values;
    let res = null;
    try {
      // 如果是编辑
      if(edit) {
        res = await API.put('/ssh/shhHost', {
          id:sshHostId,address, port, username, password
        });
      } else {     // 如果是新增
        res = await API.post('/ssh/shhHost', {
          address, port, username, password
        });
      }
    } finally {
      setEdit(false);
    }
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      setData(data);
      // console.log(data);
      showSuccess('操作成功');
    } else {
      showErrorWithCode(code, '操作失败' + message);
    }
    setConfirmLoading(false);
    setOpen(false);
  };
  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };


  // // 连接按钮
  // function buttonClick() {
  //   if (true) {
  //     buildWebSocket();
  //   }
  // }



  // 处理sshHostCard的三个按钮
  // 删除按钮
  const deleteSshHost = async (hostId) => {
    const res = await API.delete('/ssh/shhHost/' + hostId);
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      setData(data);
      showSuccess('删除成功');
    } else {
      showErrorWithCode(code, '删除失败' + message);
    }
  }

  // 编辑按钮
  const editSshHost = async (sshHostData) => {
    setOpen(true);
    const {id,address, port, username} = sshHostData;
    form.setFieldValue('address', address);
    form.setFieldValue('port', port);
    form.setFieldValue('username', username);
    setEdit(true);
    setSshHostId(id);
  }


  // 风格
  const inputStylePrefix = { width:"70px" ,textAlign:"center"};

  const style = {
    background: '#0092ff',
    padding: '8px 0',
  };

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
        closeIcon={false}
      >
        <Form
          name='normal_login'
          className='login-form'
          initialValues={{
            remember: true
          }}
          onFinish={onFinish}
          form={form}
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
              // placeholder='请输入Address'
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
                onClick={handleCancel}
              >
                取消
              </Button>
              <Button
                type='primary'
                htmlType='submit'
                className='login-form-button'
                style={{ width: '120px', height: '35px', marginLeft: '20px' }}
                // onClick={handleOk}
                loading={confirmLoading}
              >
                确定
              </Button>
            </div>

          </Form.Item>
        </Form>
      </Modal>

      {/*<Button onClick={buttonClick}>连接</Button>*/}

      <Row gutter={[16,30]} style={{marginTop: "20px"}}>
        {
          data.map((item, index) => {
            return (<Col className="gutter-row" span={6} key={index}>
              <SshHostCard data={item} deleteHost={deleteSshHost} editHost={editSshHost}></SshHostCard>
            </Col>)
        })
        }

      </Row>


    </div>
  );
};

export default Terminal;
