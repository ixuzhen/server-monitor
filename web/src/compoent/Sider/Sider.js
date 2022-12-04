import React from 'react';
import { Layout, Menu } from 'antd';
import MyMemu from "./Menu/MyMenu";

import {
    AppstoreOutlined,
    BarChartOutlined,
    CloudOutlined, ShopOutlined, TeamOutlined,
    UploadOutlined,
    UserOutlined,
    VideoCameraOutlined
} from "@ant-design/icons";

const AntdSider = Layout.Sider;
const items = [
    UserOutlined,
    VideoCameraOutlined,
    UploadOutlined,
    BarChartOutlined,
    CloudOutlined,
    AppstoreOutlined,
    TeamOutlined,
    ShopOutlined,
].map((icon, index) => ({
    key: String(index + 1),
    icon: React.createElement(icon),
    label: `nav ${index + 1}`,
}));



function Sider() {
    return (
        <AntdSider
            style={{
                overflow: 'auto',
                height: '100vh',
                position: 'fixed',
                left: 0,
                top: 0,
                bottom: 0,
            }} theme="light"
        >
            <div className="logo" style={{paddingTop:'4px'}}>
                <img src="/logo192.png" alt="some_text"
                     style={{width: 64, height: 64, display: "block", margin: "auto"}}
                />
            </div>
            {/*<Menu theme="light" mode="inline" defaultSelectedKeys={['4']} items={items} />*/}
            <MyMemu/>
        </AntdSider>
    );
}

export default Sider;