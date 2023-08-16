import React, { useEffect, useState } from 'react';
import { Layout, Space, Table, Tag } from 'antd';
import {
  isSuccess,
  paringDate,
  showError,
  showSuccess,
} from '../../helper/utils';
import { Link } from 'react-router-dom';
import { API } from '../../request';

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
          let color = 'geekblue';
          if (tag === '在线') {
            color = 'green';
          }
          if (tag === '掉线') {
            color = 'volcano';
          }
          return (
            <Tag color={color} key={tag}>
              {tag}
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
        <a onClick={() => ping(record)}>Ping</a>
      </Space>
    ),
  },
];
const ping = async (record) => {
  const ip = record.ip;
  const res = await API.get('/server/ping/' + ip);
  console.log(res);
  const { code, message, data } = res.data;
  if (isSuccess(code)) {
    if (data === true) showSuccess(ip + '可达');
    else showError(ip + '不可达');
    // console.log(datas);
  } else {
    showError(ip + '不可达');
  }
};

function HostInfo(props) {
  const [hosts, setHosts] = useState(undefined);

  const loadHosts = async () => {
    const res = await API({
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
        if (h['isOnline']) {
          h.tags = ['在线'];
        } else {
          h.tags = ['掉线'];
        }
        return h;
      });
    }
    return null;
  };

  return (
    <div>
      {/*<h2>主机信息</h2>*/}
      <Table columns={columns} dataSource={getData(hosts)} />
    </div>
  );
}

export default HostInfo;
