import React from 'react';
import LoginForm from './LoginForm';
import '../common.css';
import './Login.css';
import RegisterForm from './RegisterForm';
import { Outlet } from 'react-router-dom';

const Login = () => {
  return (
    <div
      style={{
        position: 'absolute',
        width: '100vw',
        height: '100vh',
        backgroundImage: 'url(image.jpg)',
        backgroundSize: 'cover',
        zIndex: -1,
      }}
    >
      <div className='rounded-div center' style={{ backgroundColor: 'white' }}>
        <div className='login'>
          {/*<LoginForm />*/}
          {/*<RegisterForm />*/}
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default Login;
