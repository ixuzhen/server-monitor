import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import axios from "axios";
import {isSuccess, paringDate} from "../../helper/utils";

import {Card, List, Table} from 'antd';
import dayjs from "dayjs";

const columns = [
    {
        title: '索引',
        dataIndex: 'index',
    },
    {
        title: '显卡名称',
        dataIndex: 'name',

    },
    {
        title: '显存',
        dataIndex: 'memory',
    },
    {
        title: '电源功率',
        dataIndex: 'power',
    },
    {
        title: '显卡利用率',
        dataIndex: 'utilization',

    },
    {
        title: '风扇',
        dataIndex: 'fan',

    },
    {
        title: '温度',
        dataIndex: 'temperature',
    },

];




const GPUInfo = () => {


    const {ip} = useParams();

    const [gpuinfos, setGpuInfos] = useState([]);


    const loadHosts = async ()=>{
        const res = await axios({
            method: 'GET',
            url: '/server/gpu?ip='+ip
        }).then(response=>{
            const { code, message, data } = response.data;
            // console.log(data)
            if(isSuccess(code)){
                setGpuInfos(data);
            }
        })
    }


    useEffect(()=>{
        loadHosts()
            .then(()=>{

            })
            .catch((reason)=>{
                console.log(reason)
            })
    },[])
    const columns_name = ['key', 'index', 'name', 'memory', 'power', 'utilization', 'fan','temperature'];
    const gpuinfo_name = ['indexGpu', 'indexGpu', 'nameGpu', 'memory','power', 'utilizationRate', 'fanSpeed','temperature']
    const suffix = ['', '', '', '', '', '%','%','℃',]
    const getData = (gpuinfos)=>{
        if(gpuinfos !== undefined){
            const res =  gpuinfos.map((g)=>{
                let tabel_g = {};
                for(let i=0; i<columns_name.length; i++){
                    if(columns_name[i]==='memory'){
                        tabel_g[columns_name[i]] = g['memoryUsed'].toString() + 'MiB / ' + g['memoryTotal'] +'MiB'
                        // tabel_g[columns_name[i]] = 123
                    }else if(columns_name[i]==='power'){
                        tabel_g[columns_name[i]] = g['powerUsage'].toString() + 'W / ' + g['enforcedPowerLimit'] +'W'
                        // tabel_g[columns_name[i]] = g['powerUsage'].toString() + 'W / ' + g['enforcedPowerLimit'] +'W'
                        // tabel_g[columns_name[i]] = 123412
                    }else if(columns_name[i]==='date'){

                        tabel_g[columns_name[i]] =  paringDate(g[gpuinfo_name[i]].toString(),"YYYY-MM-DD HH:mm") ;
                    }else{
                            tabel_g[columns_name[i]] = g[gpuinfo_name[i]].toString() + suffix[i];
                    }

                }
                return tabel_g;
            })
            return res;
        }
        return null;

    }


    return (
        <div>
            <List
                grid={{ gutter: 9, column: 3 }}
                dataSource={[{name:'IP',content:ip},
                    {name:'驱动版本', content: gpuinfos.length>0 ? gpuinfos[0].driverVersion:"" },
                    {name:'最新上报日期', content: gpuinfos.length>0 ? paringDate(gpuinfos[0].dateGpu,"YYYY-MM-DD HH:mm"):"" }]}
                renderItem={(item) => (
                    <List.Item>
                        <Card title={item.name}>{item.content}</Card>
                    </List.Item>
                )}
                    />

            {/*IP: {ip}*/}
            {/*驱动版本：{gpuinfos.length>0 ? gpuinfos[0].driverVersion:""}*/}
            <Table columns={columns} dataSource={getData(gpuinfos)}  />
        </div>
    );
};

export default GPUInfo;