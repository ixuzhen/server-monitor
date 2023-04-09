import { Button, Input, Modal, Space, Table } from 'antd';
import { API } from '../../../../request';
import { isSuccess, showError, showSuccess } from '../../../../helper/utils';
import React, { useEffect, useState } from 'react';

const WarningTable = () => {
  const [data, setData] = useState([]);
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const [warningLink, setWarningLink] = useState('');

  const handleLinkChange = (e) => {
    setWarningLink(e.target.value);
  };

  const showModal = () => {
    setOpen(true);
  };

  const handleOk = async () => {
    setConfirmLoading(true);
    let res;
    try {
      res = await API.post('/management/link', {
        linkUrl: warningLink,
        // "ser"
      });
      const { code, message, data } = res.data;
      if (isSuccess(code)) {
        showSuccess('添加成功');
        setData(data);
      } else {
        showError('添加失败');
      }
    } catch (error) {
      showError('添加失败');
    } finally {
      setOpen(false);
      setConfirmLoading(false);
      setWarningLink('');
    }
  };

  const handleCancel = () => {
    console.log('Clicked cancel button');
    setOpen(false);
  };

  const onClick = (id, link) => {
    return async () => {
      console.log(id);
      const res = await API.delete('/management/link', {
        data: {
          id: id,
          link: link,
        },
      });
      const { code, message, data } = res.data;
      if (isSuccess(code)) {
        showSuccess('删除成功');
        setData(data);
      } else {
        showError('删除失败');
      }
    };
  };

  const columns = [
    {
      title: 'Index',
      dataIndex: 'index',
      key: 'index',
    },
    {
      title: '链接',
      dataIndex: 'link',
      key: 'link',
    },
    {
      title: '删除',
      key: 'delete',
      render: (_, record) => (
        <Space size='middle'>
          <a onClick={onClick(record.id, record.link)}>Delete</a>
        </Space>
      ),
    },
  ];

  const loadLinkUrls = async () => {
    const res = await API.get('/management/link');
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      console.log(data);
      setData(data);
    } else {
      showError('获取链接失败');
    }
  };

  const getData = () => {
    return data.map((item, index) => {
      return {
        key: index,
        index: index + 1,
        id: item.id,
        link: item.link,
      };
    });
  };

  useEffect(() => {
    loadLinkUrls()
      .then()
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  return (
    <div>
      <div>
        {/*添加按钮*/}
        <Button onClick={showModal}>添加</Button>
        <Modal
          title='添加预警通知链接'
          open={open}
          onOk={handleOk}
          confirmLoading={confirmLoading}
          onCancel={handleCancel}
        >
          链接：
          <Input
            placeholder='通知链接'
            onChange={handleLinkChange}
            value={warningLink}
          />
        </Modal>
      </div>
      <Table columns={columns} dataSource={getData()} />;
    </div>
  );
};
export default WarningTable;
