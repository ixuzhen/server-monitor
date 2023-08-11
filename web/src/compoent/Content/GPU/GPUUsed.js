import React, { useEffect, useState } from 'react';
import { Space, Table, Tag } from 'antd';
import axios from 'axios';
import { isSuccess, paringDate } from '../../../helper/utils';
import { Link } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';
import { API } from '../../../request';

const columns = [
  {
    title: 'IP',
    dataIndex: 'ip',
    render: (text) => <Link to={`/gpuinfo/${text}`}>{text}</Link>,
  },
  {
    title: '索引',
    dataIndex: 'indexGpu',
  },
  {
    title: 'IP 显卡数',
    dataIndex: 'countGpu',
  },
  {
    title: '显卡名称',
    dataIndex: 'nameGpu',
  },
  {
    title: '显存',
    dataIndex: 'memory',
  },
  {
    title: '电源功率',
    dataIndex: 'power',
  },
  // {
  //   title: '显卡利用率',
  //   dataIndex: 'utilizationRate',
  // },
  {
    title: '更新日期',
    dataIndex: 'dateGpu',
  },
];

const GPUUsed = (props) => {
  const [GPUInfos, setGPUInfos] = useState([]);

  const loadHosts = async () => {
    const res = await API({
      method: 'GET',
      url: '/server/usedGPU',
    }).then((response) => {
      const { code, message, data } = response.data;
      if (isSuccess(code)) {
        setGPUInfos(data);
      }
    });
  };

  useEffect(() => {
    loadHosts()
      .then(() => {})
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  const getData = (GPUInfos) => {
    if (GPUInfos === undefined) return null;
    // console.log(GPUInfos);
    return GPUInfos.filter((data) => {
      return data.isUsed === props.isUsed;
    }).map((data) => {
      data.key = uuidv4();
      data.memory =
        data['memoryUsed'].toString() + ' MB / ' + data['memoryTotal'] + ' MB';
      data.power =
        data['powerUsage'].toString() +
        ' W / ' +
        data['enforcedPowerLimit'] +
        ' W';
      data.dateGpu = paringDate(data['dateGpu'].toString(), 'YYYY-MM-DD HH:mm');
      return data;
    });
  };

  return (
    <Table
      pagination={{ hideOnSinglePage: true }}
      columns={columns}
      dataSource={getData(GPUInfos)}
    />
  );
};
export default GPUUsed;
