import React, { useEffect, useState } from 'react';
import { Card, Divider, List, Table, Tag } from 'antd';
import { isSuccess, paringDate, showError } from '../../helper/utils';
import { useParams } from 'react-router-dom';
import { API } from '../../request';

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

// 端口信息表
const columnsPort = [
  {
    title: '连接类型',
    dataIndex: 'type',
  },
  {
    title: '本地地址',
    dataIndex: 'localAddress',
  },
  {
    title: '本地端口',
    dataIndex: 'localPort',
  },
  {
    title: '远程地址',
    dataIndex: 'remoteAddress',
  },
  {
    title: '远程端口',
    dataIndex: 'remotePort',
  },
  {
    title: '状态',
    dataIndex: 'state',
    render: pasringSocketState,
  },
  {
    title: 'pid',
    dataIndex: 'pid',
  },
  {
    title: '进程名称',
    dataIndex: 'pname',
  },
];

var socketState = { 1: 'established', 10: 'listening' }; // 定义一个字典

const CommonInfo = () => {
  const { ip } = useParams();
  const [datas, setDatas] = useState([]);

  // 不用该方法了
  const loadCommonInfo_old = async () => {
    await API({
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

  const loadCommonInfo = async () => {
    const res = await API.get('/server/common?ip=' + ip);
    console.log(res);
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      setDatas(data);
      // console.log(datas);
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
  // 不用该方法了
  const parsingDataOld = (datas) => {
    if (datas === undefined) return [];
    return datas.map((data) => {
      data.key = data['mountpoint'];
      return data;
    });
  };

  const parsingData = (diskInfo) => {
    if (diskInfo === undefined) return [];
    // console.log(diskInfo);
    return diskInfo.map((data) => {
      data.key = data['mountpoint'];
      return data;
    });
  };

  const parsingPortData = (PortInfo) => {
    if (PortInfo === undefined) return [];
    // console.log(PortInfo);
    return PortInfo.map((data) => {
      data.key = data['idPort'];
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
              datas['diskUsageList'] !== undefined &&
              datas['diskUsageList'].length > 0
                ? paringDate(
                    datas['diskUsageList'][0].dateDisk,
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

      <h1 style={{ textAlign: 'left' }}>磁盘信息</h1>
      <Table
        columns={columns}
        dataSource={parsingData(datas['diskUsageList'])}
        pagination={{ hideOnSinglePage: true }}
      />
      <Divider />
      <h1 style={{ textAlign: 'left' }}>端口信息</h1>
      <Table
        columns={columnsPort}
        dataSource={parsingPortData(datas['portLists'])}
        pagination={{ hideOnSinglePage: true }}
      />
    </div>
  );
};

export default CommonInfo;
