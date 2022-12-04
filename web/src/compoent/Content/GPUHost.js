import React, {useEffect, useState} from 'react';
import {Layout, Space, Table, Tag} from 'antd';
import axios from "axios";
import {isSuccess} from "../../helper/utils";
import {Link, Navigate, Outlet, Route, Routes, useRoutes} from "react-router-dom";

// import routers from "../../routers/routers";

const AntdContent = Layout.Content;
const columns = [
    {
        title: 'IP',
        dataIndex: 'ip',
        key: 'ip',
        render: (text) => <Link to={`/gpuinfo/${text}` }>{text}</Link>,
    },
    {
        title: '更新日期',
        dataIndex: 'date',
        key: 'date',
    },

];

function GPUHost(props) {

    // const element = useRoutes(routers)

    const [hosts, setHosts] = useState(undefined);

    const loadHosts = async ()=>{
        const res = await axios({
            method: 'GET',
            url: '/server/hosts'
        }).then(response=>{
            const { code, message, data } = response.data;
            if(isSuccess(code)){
                setHosts(data);
            }
        })
    }


    useEffect(()=>{
        loadHosts()
            .then()
            .catch((reason)=>{
                console.log(reason)
            })
    },[])

    const getData = (hosts)=>{
        if(hosts !== undefined){
            const res =  hosts.map((h)=>{
                h.key = h.idHost;
                h.date = '2022-12-04 22:53';
                return h;
            })
            return res;
        }
        return null;

    }



    return (

            <Table columns={columns} dataSource={getData(hosts)} />

    );
}

export default GPUHost;