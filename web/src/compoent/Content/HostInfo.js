import React, { useEffect, useState } from 'react';
import { Layout, Space, Table, Tag } from 'antd';
import axios from 'axios';
import { isSuccess } from '../../helper/utils';
import { Link } from 'react-router-dom';

const AntdContent = Layout.Content;

const columns = [
  {
    title: 'IP',
    dataIndex: 'ip',
    key: 'ip',
    render: (text) => <Link to={`/commoninfo/${text}`}>{text}</Link>,
  },
  {
    title: '更新日期',
    dataIndex: 'date',
    key: 'date',
  },

  {
    title: '状态',
    key: 'tags',
    dataIndex: 'tags',
    render: (_, { tags }) => (
      <>
        {tags.map((tag) => {
          let color = tag.length > 5 ? 'geekblue' : 'green';
          if (tag === 'loser') {
            color = 'volcano';
          }
          return (
            <Tag color={color} key={tag}>
              {tag.toUpperCase()}
            </Tag>
          );
        })}
      </>
    ),
  },
  {
    title: 'Action',
    key: 'action',
    render: (_, record) => (
      <Space size='middle'>
        <a>Ping</a>
      </Space>
    ),
  },
];

function HostInfo(props) {
  const [hosts, setHosts] = useState(undefined);

  const loadHosts = async () => {
    const res = await axios({
      method: 'GET',
      url: '/server/hosts',
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
    if (hosts !== undefined) {
      const res = hosts.map((h) => {
        h.key = h.idHost;
        h.date = '2022-12-04 22:53';
        h.tags = ['ok'];
        return h;
      });
      return res;
    }
    return null;
  };

  return <Table columns={columns} dataSource={getData(hosts)} />;
}

export default HostInfo;
