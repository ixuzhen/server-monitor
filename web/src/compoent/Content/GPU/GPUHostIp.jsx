import React, { useEffect, useState } from 'react';
import { Layout, Table, Tag, Collapse } from 'antd';
import { isSuccess, paringDate } from '../../../helper/utils';
import {
  Link,
  Navigate,
  Outlet,
  Route,
  Routes,
  useRoutes,
} from 'react-router-dom';
import GPUUsed from './GPUUsed';
import { API } from '../../../request';

const { Panel } = Collapse;

// import routers from "../../routers/routers";

const AntdContent = Layout.Content;
const columns = [
  {
    title: 'IP',
    dataIndex: 'ip',
    key: 'ip',
    render: (text) => <Link to={`/gpuinfo/${text}`}>{text}</Link>,
  },
  {
    title: '显卡数量',
    dataIndex: 'gpuCount',
    key: 'gpuCount',
  },
  {
    title: '状态',
    key: 'isOnline',
    dataIndex: 'isOnline',
    render: (_, { isOnline }) => (
      <>
        <Tag color={isOnline ? 'green' : 'volcano'} key={isOnline.toString()}>
          {isOnline ? '在线' : '掉线'}
        </Tag>
      </>
    ),
  },
  {
    title: '更新日期',
    dataIndex: 'dateHost',
    key: 'dateHost',
    render: (text) => (
      <span>{paringDate(text.toString(), 'YYYY-MM-DD HH:mm')}</span>
    ),
  },
];

function GpuHostIp(props) {
  // const element = useRoutes(routers)

  const [hosts, setHosts] = useState(undefined);

  const loadHosts = async () => {
    const res = await API({
      method: 'GET',
      url: '/server/gpuhosts',
    }).then((response) => {
      const { code, message, data } = response.data;
      if (isSuccess(code)) {
        setHosts(data);
      }
    });
  };

  useEffect(() => {
    loadHosts()
      .then()
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  const getData = (hosts) => {
    console.log(hosts);
    if (hosts !== undefined) {
      const res = hosts.map((h) => {
        h.key = h.idHost;
        // h.date = paringDate(h.dateHost.toString(), 'YYYY-MM-DD HH:mm');
        return h;
      });
      console.log(res);
      return res;
    }
    return null;
  };
  const onChange = (key) => {
    // console.log(key);
  };

  return (
    <Collapse
      defaultActiveKey={['1']}
      onChange={onChange}
      style={{
        textAlign: 'left',
      }}
    >
      <Panel header='主机ip' key='1'>
        <Table
          pagination={{ hideOnSinglePage: true }}
          columns={columns}
          dataSource={getData(hosts)}
        />
      </Panel>

      {/*<Panel header='主机ip' key='1'></Panel>*/}
      {/*<Panel header='空闲显卡' key='2'>*/}
      {/*  <GPUUsed isUsed={false}></GPUUsed>*/}
      {/*</Panel>*/}
      {/*<Panel header='已使用显卡' key='3'>*/}
      {/*  <GPUUsed isUsed={true}></GPUUsed>*/}
      {/*</Panel>*/}
    </Collapse>
  );
}
export default GpuHostIp;
