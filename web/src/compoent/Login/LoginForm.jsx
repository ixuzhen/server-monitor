import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, Input } from 'antd';
// import { LoginAPI } from '../../request/index';
import { LoginAPI } from '../../request/index';
import { isSuccess, showError } from '../../helper/utils';
import { Link, useNavigate } from 'react-router-dom';

const LoginForm = () => {
  let navigate = useNavigate();
  const onFinish = async (values) => {
    // console.log(values);
    const res = await LoginAPI.post('/login', {
      username: values.username,
      password: values.password,
    });
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      // TODO: 将token存入中
      // userDispatch({ type: 'login', payload: data });
      localStorage.setItem('token', data['token']);
      // console.log(data);
      // console.log('成功登录');
      navigate('/');
    } else {
      showError('账号密码错误');
    }
  };

  return (
    <Form
      name='normal_login'
      className='login-form'
      initialValues={{
        remember: true,
      }}
      onFinish={onFinish}
    >
      <Form.Item
        name='username'
        rules={[
          {
            required: true,
            message: 'Please input your Username!',
          },
        ]}
      >
        <Input
          prefix={<UserOutlined className='site-form-item-icon' />}
          placeholder='Username'
        />
      </Form.Item>
      <Form.Item
        name='password'
        rules={[
          {
            required: true,
            message: 'Please input your Password!',
          },
        ]}
      >
        <Input
          prefix={<LockOutlined className='site-form-item-icon' />}
          type='password'
          placeholder='Password'
        />
      </Form.Item>
      <Form.Item>
        <a className='login-form-forgot' href=''>
          Forgot password
        </a>
      </Form.Item>

      <Form.Item>
        <Button type='primary' htmlType='submit' className='login-form-button'>
          Log in
        </Button>
        Or <Link to='/register'>register now!</Link>
      </Form.Item>
    </Form>
  );
};
export default LoginForm;
