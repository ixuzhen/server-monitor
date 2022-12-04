import React from 'react';
import Header from './compoent/Header/Header'

import {Breadcrumb, Layout, Table} from 'antd';
import Sider from "./compoent/Sider/Sider";
import HostInfo from "./compoent/Content/HostInfo";
import Footer from "./compoent/Footer/Footer";
import GPUHost from "./compoent/Content/GPUHost";
import {Navigate, Route, Router, Routes, Link, useRoutes, Outlet} from "react-router-dom";
import routers from "./routers/routers";
const AntdContent = Layout.Content;

const App = () => {

    const element = useRoutes(routers)

    return (<Layout hasSider>
        <Sider/>
        <Layout
            className="site-layout"
            style={{
                marginLeft: 200,
            }}
        >

            <Header/>

            <Breadcrumb
                style={{
                    margin: '24px 16px 0',
                }}
            >
                <Breadcrumb.Item>Home</Breadcrumb.Item>
                <Breadcrumb.Item>
                    <a href="">Application Center</a>
                </Breadcrumb.Item>
                <Breadcrumb.Item>
                    <a href="">Application List</a>
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
                {element}
                {/*<Routes>*/}
                {/*    {console.log(123)}*/}
                {/*    <Route path="/gpuinfo/gpu" element={<div>12312313</div>}/>*/}
                {/*    /!*<Route path="/" element={<Navigate to="/gpuinfo"/>}/>*!/*/}
                {/*</Routes>*/}
                {/*<Outlet/>*/}

                {/*{element}*/}
            </AntdContent>


            {/*<HostInfo/>*/}

            {/*<Routes>*/}
            {/*    {console.log(123)}*/}
            {/*    <Route path="/content" element={<HostInfo></HostInfo>}/>*/}
            {/*    <Route path="/content1" element={<GPUHost></GPUHost>}/>*/}
            {/*    /!*<Route path="/" element={<Navigate to="/content1"/>}/>*!/*/}
            {/*</Routes>*/}
            {/*<Outlet/>*/}


            <Footer/>


        </Layout>
    </Layout>);
}

export default App;
