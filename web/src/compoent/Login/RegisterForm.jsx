import { Button, Checkbox, Form, Input } from 'antd';
import { isSuccess, showError } from '../../helper/utils';
import { LoginAPI } from '../../request';
import { useNavigate } from 'react-router-dom';

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
    <Form
      name='basic'
      labelCol={{
        span: 8,
      }}
      wrapperCol={{
        span: 16,
      }}
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

      <Form.Item
        wrapperCol={{
          offset: 8,
          span: 16,
        }}
      >
        <Button type='primary' htmlType='submit'>
          Submit
        </Button>
      </Form.Item>
    </Form>
  );
};

export default RegisterForm;
