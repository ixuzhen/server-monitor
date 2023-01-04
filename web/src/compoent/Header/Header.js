import React, {Component} from 'react';
import { Layout } from 'antd';
// const { Header} = Layout;
const  AntdHeader = Layout.Header;


class Header extends Component {
    render() {
        return (
            <AntdHeader
                className="site-layout-background"

                style={{
                    // position: 'sticky',
                    top: 0, zIndex: 1,
                    padding: 0,
                    backgroundColor: "white"
                }}
            ></AntdHeader>
        );
    }
}

export default Header;