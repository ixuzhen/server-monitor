import { Button, Divider, Input, Select, Tooltip } from 'antd';
import { CopyOutlined } from '@ant-design/icons';
import { useEffect, useState } from 'react';
import { API, baseURL, LoginAPI } from '../../../request';
import {
  isSuccess,
  showError,
  showErrorWithCode,
  showSuccess,
} from '../../../helper/utils';

const MessagePush = () => {
  // TODO: 获取邮箱和其他信息，放入输入框中

  const [pushWay, setPushWay] = useState('1');
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [feishuUrl, setFeishuUrl] = useState('');
  const [feishuSecret, setFeishuSecret] = useState('');
  const [dingUrl, setDingUrl] = useState('');
  const [dingSecret, setDingSecret] = useState('');
  const [wechatUrl, setwechatUrl] = useState('');

  const loadMessageUserInfo = async () => {
    const res = await API.get('/message/info');
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      console.log(code);
      const {
        email,
        username,
        feishuWebhookUrl,
        feishuWebhookSecret,
        dingWebhookUrl,
        dingWebhookSecret,
        wechatWebhookUrl,
      } = data;
      setEmail(email);
      setUsername(username);
      setFeishuUrl(feishuWebhookUrl);
      // setFeishuSecret(feishuWebhookSecret);
      setDingUrl(dingWebhookUrl);
      // setDingSecret(dingWebhookSecret);
      setwechatUrl(wechatWebhookUrl);
    } else {
      showErrorWithCode(code, '加载数据失败' + message);
    }
  };

  useEffect(() => {
    loadMessageUserInfo()
      .then()
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  // 邮箱输入框处理
  const inputChange = function (setValue) {
    return (event) => {
      setValue(event.target.value);
      // console.log(username);
    };
  };

  const usernameClick = async function () {
    const res = await API.post('/message/username', {
      username: username,
    });
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      showSuccess('设置用户名成功');
    } else {
      showError('设置用户名失败，用户名重复');
    }
  };

  async function emailClick() {
    const res = await API.post('/message/email', {
      email: email,
    });
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      showSuccess('设置邮箱成功');
    } else {
      showError('设置邮箱失败');
    }
  }

  async function feishuClick() {
    const res = await API.post('/message/feishu', {
      url: feishuUrl,
      secret: feishuSecret,
      // "ser"
    });
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      showSuccess('设置飞书成功');
    } else {
      showError('设置飞书失败');
    }
  }

  async function dingClick() {
    const res = await API.post('/message/dingding', {
      url: dingUrl,
      secret: dingSecret,
      // "ser"
    });
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      showSuccess('设置钉钉成功');
    } else {
      showError('设置钉钉失败');
    }
  }

  async function wechatClick() {
    const res = await API.post('/ma/wechat', {
      url: wechatUrl,
      // "ser"
    });
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      showSuccess('设置企业微信成功');
    } else {
      showError('设置企业微信失败');
    }
  }

  const pushWayChange = (value) => {
    setPushWay(value);
    // console.log(pushWay);
  };

  async function pushWayClick() {
    const res = await API.post('/message/pushway', {
      pushway: pushWay,
      // "ser"
    });
    console.log(res);
    const { code, message, data } = res.data;
    if (isSuccess(code)) {
      showSuccess('设置推送方式成功');
    } else {
      showError('设置推送方式失败');
    }
  }

  function testClick(testWay) {
    return async () => {
      const res = await LoginAPI(
        '/push/' +
          username +
          '?title=这是标题&content=这是内容&channel=' +
          testWay
      );
      const { code, message, data } = res.data;
      if (isSuccess(code)) {
        showSuccess('发送成功');
      } else {
        showError('设置失败');
      }
    };
  }
  const handleCopy = () => {
    const value = baseURL + '/push/' + username + '?title=标题&content=内容';
    // const value = 1'https://monitoring.luckynow.cn:8080/push/' + username + '?title=标题&content=内容';
    const textarea = document.createElement('textarea');
    textarea.value = value;
    document.body.appendChild(textarea);
    textarea.select();
    document.execCommand('copy');
    document.body.removeChild(textarea);
  };

  return (
    <div style={{ textAlign: 'left', marginLeft: 20 }}>
      <Divider />
      {/*设置推送使用的用户名*/}
      <h2>默认推送方式</h2>
      <div style={{ marginLeft: 20, marginTop: 30 }}>
        推送方式选择：
        <Select
          // defaultValue={pushWay}
          value={pushWay}
          style={{
            width: 200,
            marginLeft: 20,
          }}
          onChange={pushWayChange}
          options={[
            {
              value: '1',
              label: '邮箱推送',
            },
            {
              value: '2',
              label: '飞书群机器人推送',
            },
            {
              value: '3',
              label: '钉钉群机器人推送',
            },
            {
              value: '4',
              label: '企业微信群机器人推送',
            },
          ]}
        />
        <Button
          type='primary'
          style={{ marginLeft: 10 }}
          onClick={pushWayClick}
        >
          更改
        </Button>
      </div>

      <Divider />
      {/*设置推送使用的用户名*/}
      <h2>推送用户名设置</h2>
      <div style={{ marginLeft: 20, marginTop: 30 }}>
        用户名：
        <Input
          style={{ width: '30%', marginLeft: 10 }}
          placeholder='用户名'
          onChange={inputChange(setUsername)}
          value={username}
        />
        <Button
          type='primary'
          style={{ marginLeft: 10 }}
          onClick={usernameClick}
        >
          更改
        </Button>
        <Input.Group compact style={{ marginTop: 20 }}>
          推送地址：
          <Input
            style={{
              width: '50%',
            }}
            // value={
            //   'https://monitoring.luckynow.cn/api/push/' +
            //   username +
            //   '?title=标题&content=内容'
            // }
            value={baseURL + '/push/' + username + '?title=标题&content=内容'}
          />
          <Tooltip title='复制'>
            <Button icon={<CopyOutlined />} onClick={handleCopy} />
          </Tooltip>
        </Input.Group>
      </div>

      <Divider />
      {/*邮箱相关配置*/}
      <h2>邮箱推送</h2>
      <div style={{ marginLeft: 20, marginTop: 30 }}>
        邮箱地址：
        <Input
          style={{ width: '30%', marginLeft: 10 }}
          placeholder='邮箱地址'
          onChange={inputChange(setEmail)}
          value={email}
        />
        <Button type='primary' style={{ marginLeft: 10 }} onClick={emailClick}>
          更改
        </Button>
        <Button
          style={{ marginLeft: 20 }}
          type='dashed'
          onClick={testClick('email')}
        >
          测试
        </Button>
      </div>
      <Divider />
      {/*飞书群机器人推送*/}
      <h2>飞书群机器人推送</h2>
      <div style={{ marginLeft: 20, marginTop: 30 }}>
        <div>
          群机器人 Webhook 的 URL：
          <Input
            style={{ width: '30%', marginLeft: 22 }}
            placeholder='webhook url'
            onChange={inputChange(setFeishuUrl)}
            value={feishuUrl}
          />
        </div>
        <div style={{ marginTop: 25 }}>
          群机器人 Webhook 的 secret：
          <Input
            style={{ width: '30%', marginLeft: 10 }}
            placeholder='webhook secret'
            onChange={inputChange(setFeishuSecret)}
            // style={{ marginTop: 10 }}
          />
        </div>

        <Button
          type='primary'
          style={{ marginLeft: 300, marginTop: 25 }}
          onClick={feishuClick}
        >
          更改
        </Button>
        <Button
          style={{ marginLeft: 20 }}
          type='dashed'
          onClick={testClick('feishu')}
        >
          测试
        </Button>
      </div>
      <Divider />
      {/*钉钉群机器人推送*/}
      <h2>钉钉群机器人推送</h2>
      <div style={{ marginLeft: 20, marginTop: 30 }}>
        <div>
          群机器人 Webhook 的 URL：
          <Input
            style={{ width: '30%', marginLeft: 22 }}
            placeholder='webhook url'
            onChange={inputChange(setDingUrl)}
            value={dingUrl}
          />
        </div>
        <div style={{ marginTop: 25 }}>
          群机器人 Webhook 的 secret：
          <Input
            style={{ width: '30%', marginLeft: 10 }}
            placeholder='webhook secret'
            onChange={inputChange(setDingSecret)}
            // style={{ marginTop: 10 }}
          />
        </div>

        <Button
          type='primary'
          style={{ marginLeft: 300, marginTop: 25 }}
          onClick={dingClick}
        >
          更改
        </Button>
        <Button
          style={{ marginLeft: 20 }}
          type='dashed'
          onClick={testClick('dingding')}
        >
          测试
        </Button>
      </div>
      <Divider />
      {/*企业微信群机器人推送*/}
      <h2>企业微信群机器人推送</h2>
      <div style={{ marginLeft: 20, marginTop: 30 }}>
        <div>
          群机器人 Webhook 的 URL：
          <Input
            style={{ width: '30%', marginLeft: 22 }}
            placeholder='webhook url'
            onChange={inputChange(setwechatUrl)}
            value={wechatUrl}
          />
        </div>

        <Button
          type='primary'
          style={{ marginLeft: 300, marginTop: 25 }}
          onClick={wechatClick}
        >
          更改
        </Button>
        <Button
          style={{ marginLeft: 20 }}
          type='dashed'
          onClick={testClick('wechat')}
        >
          测试
        </Button>
      </div>
      <Divider />
    </div>
  );
};
export default MessagePush;
