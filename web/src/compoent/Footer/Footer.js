import React from 'react';
import {Layout} from "antd";
const  AntdFooter  = Layout.Footer;
function Footer(props) {
    return (
        <AntdFooter
            style={{
                textAlign: 'center',
            }}
        >
            Ant Design ©2018 Created by Ant UED
        </AntdFooter>
    );
}

export default Footer;