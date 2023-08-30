import React, { useEffect, useState } from 'react';
import './SshHostCard.css';
import { CodeTwoTone, DeleteOutlined, EditOutlined } from '@ant-design/icons';
import { Button, Popconfirm, Tooltip } from 'antd';
import { API } from '../../../request';
import { isSuccess, showError } from '../../../helper/utils';

const SshHostCard = (props) => {
  // 通过props对象访问从父组件传递的数据对象
  const data = props.data;
  const {id, address, port, username} = props.data;

  const clickConnect = () => {
    buildWebSocket();
  }
  function buildWebSocket() {
    const data = { id, address, port, username };
    const queryParams = new URLSearchParams(data).toString();
    const w = window.open('_black') //这里是打开新窗口
    let url = `/webssh?${queryParams}`
    w.location.href = url //这样就可以跳转了
  }

  return (
    <div className="ssh-host-card">
      <div>
        <CodeTwoTone style={{fontSize: "30px"}}/>
      </div>
      <div>
        <div>{address}</div>
       <div style={{display: "flex", justifyContent:"space-between", fontSize:"10px", color: 'rgb(140, 146, 164)'}}>
         <span>{username}</span>
         <span>{port}</span>
       </div>
      </div>
      <div style={{cursor: 'pointer'}} >
        <Popconfirm
          title="删除"
          description="是否要删除该 Host?"
          onConfirm={()=>{
            props.deleteHost(id)
          }}
          // onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <Tooltip title="删除">
            <DeleteOutlined style={{fontSize: "20px", color: "red"}}/>
          </Tooltip>
        </Popconfirm>


      </div>
      <div style={{cursor: 'pointer'}} onClick={()=>{
        props.editHost(data);
      }}>
        <Tooltip title="编辑">
          <EditOutlined style={{fontSize: "20px"}}/>
        </Tooltip>

      </div>
      <div>
        <Button type="primary" onClick={clickConnect}>连接</Button>
      </div>
    </div>
  );
};

export default SshHostCard;