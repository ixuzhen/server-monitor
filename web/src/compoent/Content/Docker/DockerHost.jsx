import React, { useEffect, useState } from 'react';
import { Layout, Space, Table, Tag } from 'antd';
import { isSuccess, paringDate } from '../../../helper/utils';
import { Link } from 'react-router-dom';
import { API } from '../../../request';

const AntdContent = Layout.Content;

const columns = [
  {
    title: 'IP',
    dataIndex: 'ip',
    key: 'ip',
    render: (text) => <Link to={`/dockerinfo/${text}`}>{text}</Link>,
  },
  {
    title: '容器数量',
    dataIndex: 'dockerCount',
    key: 'dockerCount',
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
    dataIndex: 'date',
    key: 'date',
  },
  // {
  //   title: 'Action',
  //   key: 'action',
  //   render: (_, record) => (
  //     <Space size='middle'>
  //       <a>Ping</a>
  //     </Space>
  //   ),
  // },
];

function DockerHost(props) {
  const [hosts, setHosts] = useState(undefined);

  const loadHosts = async () => {
    const res = await API({
      method: 'GET',
      url: '/server/dockerhosts',
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

  const getData = (datas) => {
    // console.log(datas);
    if (datas !== undefined) {
      return datas.map((h) => {
        h.key = h['idHost'];
        // paringDate(data['dateGpu'].toString(), 'YYYY-MM-DD HH:mm')
        h.date =
          h['dateHost'] === null
            ? ' '
            : paringDate(h['dateHost'].toString(), 'YYYY-MM-DD HH:mm');
        return h;
      });
    }
    return null;
  };

  return <Table columns={columns} dataSource={getData(hosts)} />;
}

export default DockerHost;
