import React, { useEffect, useState } from 'react';
import { Card, Divider, List, Table } from 'antd';
import { isSuccess, paringDate, showError } from '../../../helper/utils';
import { useParams } from 'react-router-dom';
import { API } from '../../../request';

const pasringByte = (text) => {
  const kb = parseInt(text) / 1024;
  const mb = kb / 1024;
  const gb = mb / 1024;
  const tb = gb / 1024;
  const precision = 2;
  if (gb < 1) return <>{mb.toFixed(precision)} M</>;
  if (tb < 1) return <>{gb.toFixed(precision)} G</>;
  else return <>{tb.toFixed(precision)} T</>;
};

const pasringSocketState = (state) => {
  var str_state = socketState[state];
  if (str_state === undefined) str_state = state;
  return <>{str_state}</>;
};

// 磁盘信息表
const columns = [
  {
    title: '容器名字',
    dataIndex: 'name',
    render: (text) => <a>{text}</a>,
  },
  {
    title: '容器ID',
    dataIndex: 'containerId',
  },
  {
    title: 'CPU',
    dataIndex: 'cpu',
  },
  {
    title: '内存',
    dataIndex: 'memery',
  },
  {
    title: '网络(发送/接收)',
    dataIndex: 'network',
  },
  {
    title: '磁盘(读/写)',
    dataIndex: 'disk',
  },
];

var socketState = { 1: 'established', 10: 'listening' }; // 定义一个字典

const DockerInfo = () => {
  const { ip } = useParams();
  const [datas, setDatas] = useState([]);

  const loadCommonInfo = async () => {
    const res = await API.get('/server/docker?ip=' + ip);
    console.log(res);
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      setDatas(data);
      console.log(datas);
    } else {
      showError(message);
    }
  };

  useEffect(() => {
    loadCommonInfo()
      .then(() => {})
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  const parsingData = (diskInfo) => {
    if (diskInfo === undefined) return [];
    // console.log(diskInfo);
    return diskInfo.map((data) => {
      data.key = data['containerId'];
      data['memery'] = data['memeryUsage'] + ' / ' + data['memeryLimit'];
      // 网络
      data['network'] = data['netIOSend'] + ' / ' + data['netIOReceive'];
      // 磁盘
      data['disk'] = data['blockIORead'] + ' / ' + data['blockIOWrite'];
      return data;
    });
  };

  function parsingMemory() {
    const memoryInfo = datas['memoryInfo'];
    if (memoryInfo === undefined) return '';
    const total = memoryInfo['total'] / 1024 / 1024 / 1024;
    const available = memoryInfo['available'] / 1024 / 1024 / 1024;
    const used = total - available;
    return '' + used.toFixed(2) + ' G / ' + total.toFixed(2) + ' G';
  }

  return (
    <div>
      <List
        grid={{ gutter: 12, column: 4 }}
        dataSource={[
          { name: 'IP', content: ip },

          {
            name: '最新上报日期',
            content:
              datas['dockerInfo'] !== undefined &&
              datas['dockerInfo'].length > 0
                ? paringDate(
                    datas['dockerInfo'][0].dateDisk,
                    'YYYY-MM-DD HH:mm'
                  )
                : '',
          },
          { name: '系统名称', content: 'Ubuntu' },
          { name: '内存使用', content: parsingMemory() },
        ]}
        renderItem={(item) => (
          <List.Item>
            <Card title={item.name}>{item.content}</Card>
          </List.Item>
        )}
      />

      {/*<Memory></Memory>*/}

      <h1 style={{ textAlign: 'left' }}>容器信息</h1>
      <Table
        columns={columns}
        dataSource={parsingData(datas['dockerInfo'])}
        pagination={{ hideOnSinglePage: true }}
      />
      <Divider />
    </div>
  );
};

export default DockerInfo;
