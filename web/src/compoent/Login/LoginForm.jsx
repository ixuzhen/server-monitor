import { Button, Form, Input } from 'antd';

import { LoginAPI } from '../../request/index';
import { isSuccess, showError } from '../../helper/utils';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Spin } from 'antd';
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';

const LoginForm = () => {

  const [spinning, setSpinning] = useState(false);

  const oauthLogin = async () => {

    const oauth = new URLSearchParams(window.location.search).get('oauth');
    if (oauth) {
      setSpinning(true);
      if (oauth === 'github') {
        const codeGithub = new URLSearchParams(window.location.search).get('code');
        const res = await LoginAPI.post('/login/github', {
          code: codeGithub
        },{
          timeout: 10000 // 设置超时时间，单位是毫秒
        });
        const { code, message, data } = res.data;
        setSpinning(false);
        if (isSuccess(code)) {
          localStorage.setItem('token', data['token']);
          navigate('/');
        } else {
          showError('Github登录发生错误错误');
        }
      } else if (oauth === 'gitee') {
        const codeGitee = new URLSearchParams(window.location.search).get('code');
        const res = await LoginAPI.post('/login/gitee', {
          code: codeGitee
        },{
          timeout: 10000 // 设置超时时间，单位是毫秒
        });
        const { code, message, data } = res.data;
        setSpinning(false);
        if (isSuccess(code)) {
          localStorage.setItem('token', data['token']);
          navigate('/');
        } else {
          showError('Gitee登录发生错误错误');
        }
      }
    }
  };

  useEffect(() => {
    oauthLogin()
      .then()
      .catch((reason) => {
        setSpinning(false);
        console.log(reason);
      });
  }, []);


  let navigate = useNavigate();
  const onFinish = async (values) => {
    // console.log(values);
    const res = await LoginAPI.post('/login', {
      username: values.username,
      password: values.password
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

  const handleGitHubLogin = async () => {
    window.open(
      `https://github.com/login/oauth/authorize?client_id=ebd485530f63d84b55df&scope=user:email`
    );
  };

  const handleGiteeLogin = async () => {
    window.open(
      `https://gitee.com/oauth/authorize?client_id=0f020b75c9ec21aed8cd9d5b7f469df49df38c8f9efb6ba69a112b69804b7eb3&response_type=code&redirect_uri=http://127.0.0.1:3000/login?oauth=gitee`
    );
  };

  return (
    <div>
      <div>
        {/*<h3>账号登录</h3>*/}
        <div style={{fontSize: "20px", marginBottom: "20px", textAlign:"center"}}>账号登陆</div>
        <Form
          name='normal_login'
          className='login-form'
          initialValues={{
            remember: true
          }}
          onFinish={onFinish}
        >

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
              // prefix={<UserOutlined className='site-form-item-icon' />}
              prefix={<div style={{ marginRight: '10px' }}>账号</div>}
              placeholder='请输入账号(admin)'
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
              prefix={<div style={{ marginRight: '10px' }}>密码</div>}
              type='password'
              placeholder='请输入密码(123456)'
              iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
            />

          </Form.Item>
          {/*<Form.Item>*/}
          {/*  /!*<a className='login-form-forgot' href=''>*!/*/}
          {/*  /!*  忘记密码*!/*/}
          {/*  /!*</a>*!/*/}
          {/*  <div style={{ textAlign: 'right' }}>*/}
          {/*    <Link to='/register'>注册登录</Link>*/}
          {/*  </div>*/}
          {/*</Form.Item>*/}

          <Form.Item>
            <div style={{ textAlign: 'center' }}>
              <Button
                // className='login-form-button'
                style={{ width: '120px', height: '35px' }}
                onClick={() => navigate('/register')}
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
      </div>

      <div style={{ margin: '0 auto', color: 'rgb(140, 146, 164)', textAlign: 'center', fontSize: '14px' , marginTop:"40px"}}>
        <div>
          其他方式登录
        </div>
        <div style={{ display: 'flex', marginTop: '20px', paddingLeft: '20px' }}>
          <div onClick={handleGitHubLogin} style={{ flex: 1, margin: '0 auto' }} className='otherLoginSpanCenter'>
            <span>
            <svg t='1692888905361' className='icon' viewBox='0 0 1024 1024' version='1.1'
                 xmlns='http://www.w3.org/2000/svg'
                 p-id='2841' width='32' height='32'>
            <path
              d='M512 42.666667A464.64 464.64 0 0 0 42.666667 502.186667 460.373333 460.373333 0 0 0 363.52 938.666667c23.466667 4.266667 32-9.813333 32-22.186667v-78.08c-130.56 27.733333-158.293333-61.44-158.293333-61.44a122.026667 122.026667 0 0 0-52.053334-67.413333c-42.666667-28.16 3.413333-27.733333 3.413334-27.733334a98.56 98.56 0 0 1 71.68 47.36 101.12 101.12 0 0 0 136.533333 37.973334 99.413333 99.413333 0 0 1 29.866667-61.44c-104.106667-11.52-213.333333-50.773333-213.333334-226.986667a177.066667 177.066667 0 0 1 47.36-124.16 161.28 161.28 0 0 1 4.693334-121.173333s39.68-12.373333 128 46.933333a455.68 455.68 0 0 1 234.666666 0c89.6-59.306667 128-46.933333 128-46.933333a161.28 161.28 0 0 1 4.693334 121.173333A177.066667 177.066667 0 0 1 810.666667 477.866667c0 176.64-110.08 215.466667-213.333334 226.986666a106.666667 106.666667 0 0 1 32 85.333334v125.866666c0 14.933333 8.533333 26.88 32 22.186667A460.8 460.8 0 0 0 981.333333 502.186667 464.64 464.64 0 0 0 512 42.666667'
              p-id='2842'></path>
          </svg>
          </span>
            <span style={{ marginLeft: '5px' }}>GitHub登录</span>
          </div>
          <div onClick={handleGiteeLogin} className='otherLoginSpanCenter' style={{ flex: 1, margin: '0 auto' }}>
            <span>
            <svg t='1692890597915' className='icon' viewBox='0 0 1024 1024' version='1.1'
                 xmlns='http://www.w3.org/2000/svg' p-id='3955' width='32' height='32'><path
              d='M512 1024C229.222 1024 0 794.778 0 512S229.222 0 512 0s512 229.222 512 512-229.222 512-512 512z m259.149-568.883h-290.74a25.293 25.293 0 0 0-25.292 25.293l-0.026 63.206c0 13.952 11.315 25.293 25.267 25.293h177.024c13.978 0 25.293 11.315 25.293 25.267v12.646a75.853 75.853 0 0 1-75.853 75.853h-240.23a25.293 25.293 0 0 1-25.267-25.293V417.203a75.853 75.853 0 0 1 75.827-75.853h353.946a25.293 25.293 0 0 0 25.267-25.292l0.077-63.207a25.293 25.293 0 0 0-25.268-25.293H417.152a189.62 189.62 0 0 0-189.62 189.645V771.15c0 13.977 11.316 25.293 25.294 25.293h372.94a170.65 170.65 0 0 0 170.65-170.65V480.384a25.293 25.293 0 0 0-25.293-25.267z'
              fill='#C71D23' p-id='3956'></path></svg>
          </span>
            <span style={{ marginLeft: '5px' }}>Gitee登录</span>
          </div>


        </div>
      </div>


      <Spin tip='登录中' size='large' spinning={spinning}>
        <div className='content' />
      </Spin>
    </div>
  );
};
export default LoginForm;


