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
    <div>
      <Form
        name='normal_login'
        className='login-form'
        initialValues={{
          remember: true,
        }}
        onFinish={onFinish}
      >
        <h3>账号登录</h3>
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
            placeholder='请输入账号'
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
            placeholder='请输入密码'
          />
        </Form.Item>
        <Form.Item>
          {/*<a className='login-form-forgot' href=''>*/}
          {/*  忘记密码*/}
          {/*</a>*/}
          <div style={{ textAlign: 'right' }}>
            <Link to='/register'>注册登录</Link>
          </div>
        </Form.Item>

        <Form.Item>
          <div style={{ textAlign: 'center' }}>
            <Button
              type='primary'
              htmlType='submit'
              className='login-form-button'
            >
              登录
            </Button>
          </div>
        </Form.Item>
      </Form>
    </div>
  );
};
export default LoginForm;
