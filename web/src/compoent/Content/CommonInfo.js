import React, { useEffect, useState } from 'react';
import { Card, List, Space, Table, Tag } from 'antd';
import axios from 'axios';
import { isSuccess, paringDate } from '../../helper/utils';
import { useParams } from 'react-router-dom';

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

const columns = [
  {
    title: '挂载点',
    dataIndex: 'mountpoint',
    render: (text) => <a>{text}</a>,
  },
  {
    title: '总容量',
    dataIndex: 'total',
    render: pasringByte,
  },
  {
    title: '已使用',
    dataIndex: 'used',
    render: pasringByte,
  },
  {
    title: '剩余容量',
    dataIndex: 'free',
    render: pasringByte,
  },
  {
    title: '使用百分比',
    dataIndex: 'percent',
    render: (percent) => {
      let color = parseFloat(percent) > 80 ? 'red' : 'green';
      return (
        <Tag color={color} key={percent}>
          {percent}
        </Tag>
      );
    },
  },
  {
    title: '文件系统',
    dataIndex: 'fstype',
  },
  {
    title: '设备名称',
    dataIndex: 'device',
  },
];

const CommonInfo = () => {
  const { ip } = useParams();
  const [datas, setDatas] = useState([]);

  const loadCommonInfo = async () => {
    await axios({
      method: 'GET',
      url: '/server/disk/usage?ip=' + ip,
    })
      .then((response) => {
        const { code, message, data } = response.data;
        // console.log(data);
        if (isSuccess(code)) {
          setDatas(data);
        }
      })
      .catch((reason) => {
        console.log(reason);
      });
  };

  useEffect(() => {
    loadCommonInfo()
      .then(() => {})
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  const parsingData = (datas) => {
    if (datas === undefined) return [];
    return datas.map((data) => {
      data.key = data['mountpoint'];
      return data;
    });
  };

  return (
    <div>
      <List
        grid={{ gutter: 12, column: 4 }}
        dataSource={[
          { name: 'IP', content: ip },

          {
            name: '最新上报日期',
            content:
              datas.length > 0
                ? paringDate(datas[0].dateDisk, 'YYYY-MM-DD HH:mm')
                : '',
          },
          { name: '系统名称', content: 'Ubuntu' },
          { name: '待定', content: '待定' },
        ]}
        renderItem={(item) => (
          <List.Item>
            <Card title={item.name}>{item.content}</Card>
          </List.Item>
        )}
      />

      <h1 style={{ textAlign: 'left' }}>磁盘信息</h1>
      <Table
        columns={columns}
        dataSource={parsingData(datas)}
        pagination={{ hideOnSinglePage: true }}
      />
    </div>
  );
};

export default CommonInfo;
