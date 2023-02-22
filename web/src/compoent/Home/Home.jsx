import React from 'react';
import { Outlet, useRoutes } from 'react-router-dom';
import routers from '../../routers/routers';
import { Breadcrumb, Layout } from 'antd';
import Sider from '../Sider/Sider';
import Header from '../Header/Header';
import Footer from '../Footer/Footer';

const AntdContent = Layout.Content;
const Home = () => {
  const element = useRoutes(routers);
  return (
    // <div>Home</div>
    <Layout hasSider>
      <Sider />

      <Layout
        className='site-layout'
        style={{
          marginLeft: 200,
        }}
      >
        <Header />

        <Breadcrumb
          style={{
            margin: '24px 16px 0',
          }}
        >
          <Breadcrumb.Item>Home</Breadcrumb.Item>
          <Breadcrumb.Item>
            <a href=''>Application Center</a>
          </Breadcrumb.Item>
          <Breadcrumb.Item>
            <a href=''>Application List</a>
          </Breadcrumb.Item>
          <Breadcrumb.Item>An Application</Breadcrumb.Item>
        </Breadcrumb>

        <AntdContent
          style={{
            margin: '24px 16px 0',
            overflow: 'initial',
            minHeight: 'calc(100vh - 64px)',
            textAlign: 'center',
          }}
        >
          {/*{element}*/}
          {/*<Routes>*/}
          {/*    {console.log(123)}*/}
          {/*    <Route path="/gpuinfo/gpu" element={<div>12312313</div>}/>*/}
          {/*    /!*<Route path="/" element={<Navigate to="/gpuinfo"/>}/>*!/*/}
          {/*</Routes>*/}
          <Outlet />
          {/*1234*/}
          {/*{element}*/}
        </AntdContent>

        {/*<HostInfo/>*/}

        {/*<Routes>*/}
        {/*    {console.log(123)}*/}
        {/*    <Route path="/content" element={<HostInfo></HostInfo>}/>*/}
        {/*    <Route path="/content1" element={<GPUHost></GPUHost>}/>*/}
        {/*    /!*<Route path="/" element={<Navigate to="/content1"/>}/>*!/*/}
        {/*</Routes>*/}
        {/*<Outlet />*/}

        <Footer />
      </Layout>
    </Layout>
  );
};

export default Home;
