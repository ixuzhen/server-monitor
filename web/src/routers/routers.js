import HostInfo from '../compoent/Content/HostInfo';
import GPUHost from '../compoent/Content/GPU/GPUHost';
import { Navigate } from 'react-router-dom';
import React from 'react';
import GPUInfo from '../compoent/Content/GPU/GPUInfo';
import CommonInfo from '../compoent/Content/CommonInfo';
import Login from '../compoent/Login/Login';
import Home from '../compoent/Home/Home';
import Register from '../compoent/Login/Register';
// 懒加载
// const Home = lazy(()=>import("./compoent/Home/Home"))
import { history } from '../helper/history';
import MessagePush from '../compoent/Content/message/MessagePush';
import Terminal from '../compoent/Content/terminal/terminal';
import WarningPush from '../compoent/Content/WarningPush/WarningPush';
import GpuHostIp from '../compoent/Content/GPU/GPUHostIp';
import GpuUsedPage from '../compoent/Content/GPU/GPUUsedPage';
import GpuUnUsedPage from '../compoent/Content/GPU/GPUUnUsedPage';
import LoginForm from '../compoent/Login/LoginForm';
import DockerHost from '../compoent/Content/Docker/DockerHost';
import DockerInfo from '../compoent/Content/Docker/DockerInfo';

const withLoadingComponent = (comp) => (
  <React.Suspense fallback={<div>Loading...</div>}>{comp}</React.Suspense>
);

function PrivateRoute({ children }) {
  if (!localStorage.getItem('token')) {
    return <Navigate to='/login' state={{ from: history.location }} />;
  }
  return children;
}

export default [
  {
    path: '/',
    element: <Navigate to='/hostinfo' />,
  },
  {
    path: '/',
    element: <Home />,
    children: [
      {
        path: '/hostinfo',
        element: withLoadingComponent(
          <PrivateRoute>
            <HostInfo />
          </PrivateRoute>
        ),
      },
      // {
      //   path: '/gpuhost',
      //   element: withLoadingComponent(
      //     <PrivateRoute>
      //       <GpuHostIp />
      //     </PrivateRoute>
      //   ),
      // },
      {
        path: '/gpuhostip',
        element: withLoadingComponent(
          <PrivateRoute>
            <GpuHostIp />
          </PrivateRoute>
        ),
      },
      {
        path: '/gpuused',
        element: withLoadingComponent(
          <PrivateRoute>
            <GpuUsedPage />
          </PrivateRoute>
        ),
      },
      {
        path: '/gpuunused',
        element: withLoadingComponent(
          <PrivateRoute>
            <GpuUnUsedPage></GpuUnUsedPage>
          </PrivateRoute>
        ),
      },
      {
        path: '/gpuinfo/:ip',
        element: withLoadingComponent(
          <PrivateRoute>
            <GPUInfo />
          </PrivateRoute>
        ),
      },
      {
        path: '/commoninfo/:ip',
        element: withLoadingComponent(
          <PrivateRoute>
            <CommonInfo />
          </PrivateRoute>
        ),
      },
      {
        path: '/message',
        element: withLoadingComponent(
          <PrivateRoute>
            <MessagePush />
          </PrivateRoute>
        ),
      },
      {
        path: '/terminal',
        element: withLoadingComponent(
          <PrivateRoute>
            <Terminal />
          </PrivateRoute>
        ),
      },
      {
        path: '/warningPush',
        element: withLoadingComponent(
          <PrivateRoute>
            <WarningPush />
          </PrivateRoute>
        ),
      },
      {
        path: '/dockerhost',
        element: withLoadingComponent(
          <PrivateRoute>
            <DockerHost />
          </PrivateRoute>
        ),
      },
      {
        path: '/dockerinfo/:ip',
        element: withLoadingComponent(
          <PrivateRoute>
            <DockerInfo />
          </PrivateRoute>
        ),
      },
    ],
  },
  {
    path: '/',
    element: <Login></Login>,
    children: [
      {
        path: '/login',
        element: <LoginForm />,
      },
      {
        path: '/register',
        element: <Register />,
      },
    ],
  },

  {
    path: '*',
    element: <Navigate to='/hostinfo' />,
  },
];

// export default [
//   {
//     path: '/',
//     element: <Home></Home>,
//   },
//   {
//     path: '/hostinfo',
//     element: withLoadingComponent(<HostInfo />),
//     // element: <HostInfo />,
//   },
//   {
//     path: '/gpuhost',
//     element: withLoadingComponent(<GPUHost />),
//     // children:[
//     //     {
//     //         path:'gpu/:ip',
//     //         element:<div>123123333</div>
//     //     }
//     // ]
//   },
//   {
//     path: '/gpuinfo/:ip',
//     element: withLoadingComponent(<GPUInfo />),
//   },
//   {
//     path: '/commoninfo/:ip',
//     element: withLoadingComponent(<CommonInfo />),
//   },
//   {
//     path: '/',
//     element: <Navigate to='/hostinfo' />,
//   },
// ];
