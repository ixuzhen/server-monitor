import { Button, Form, Input } from 'antd';
import { isSuccess, showError } from '../../helper/utils';
import { LoginAPI } from '../../request';
import { Link, useNavigate } from 'react-router-dom';

const RegisterForm = () => {
  let navigate = useNavigate();
  const onFinish = async (values) => {
    if (values.password !== values.confirmPsw) {
      showError('两次输入的密码不一致');
      return;
    }
    const res = await LoginAPI.post('/register', {
      username: values.username,
      password: values.password,
    });
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      // TODO: 将token存入中
      console.log(data);
      localStorage.setItem('token', data['token']);

      // console.log('成功登录');
      navigate('/');
    } else {
      showError('账号已被使用');
    }
  };
  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <div>
      <h3>账号注册</h3>
      <Form
        name='basic'
        labelCol={{
          span: 8,
        }}
        // wrapperCol={{
        //   span: 16,
        // }}
        style={{
          maxWidth: 600,
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={onFinish}
        onFinishFailed={onFinishFailed}
        autoComplete='off'
      >
        <Form.Item
          label='用户名'
          name='username'
          rules={[
            {
              required: true,
              message: 'Please input your username!',
            },
          ]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label='密码'
          name='password'
          rules={[
            {
              required: true,
              message: 'Please input your password!',
            },
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          label='确认密码'
          name='confirmPsw'
          rules={[
            {
              required: true,
              message: 'Please confirm your password!',
            },
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item>
          <div style={{ textAlign: 'right' }}>
            <Link to='/login'>返回登录</Link>
          </div>
        </Form.Item>

        <Form.Item
        // wrapperCol={{
        //   offset: 8,
        //   span: 16,
        // }}
        >
          <div style={{ width: '100%', textAlign: 'center' }}>
            <Button
              type='primary'
              htmlType='submit'
              className='login-form-button'
            >
              注册
            </Button>
          </div>
        </Form.Item>
      </Form>
    </div>
  );
};

export default RegisterForm;
