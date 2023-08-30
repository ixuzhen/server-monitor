import React, { useEffect } from 'react';
import { Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import 'xterm/css/xterm.css'
import { showError } from '../../helper/utils';

const TerminalPage = () => {
  let term = null;
  let socket = null;

  const queryParams = new URLSearchParams(window.location.search);
  const id = queryParams.get('id');
  const host = queryParams.get('address');
  const port = queryParams.get('port');
  const username = queryParams.get('username');
  // const password = queryParams.get('password');

  // let host = "113.106.111.75";
  // let port = "2151";
  // let username = "root";
  // let password = "Osroot123";
  let operate = "connect";
  // todo: 这里要改
  let url = "127.0.0.1:8080/webssh"


  function generateEndpoint() {
    let protocol;
    if (window.location.protocol === 'https:') {
       protocol = 'wss://';
    } else {
       protocol = 'ws://';
    }
    const endpoint = protocol + url;
    return endpoint;
  }

  // const initTerm = function() {
  function initTerm() {
    let element = document.getElementById('terminal');
    // 设置了cols或者fitAddon.fit(); 当一行字符超过80个过会替换现在的内容，否则换行
    // term = new Terminal({
    //   cursorBlink: true, // 关标闪烁
    //   cursorStyle: "underline", // 光标样式 'block' | 'underline' | 'bar'
    //   scrollback: 800, // 当行的滚动超过初始值时保留的行视窗，越大回滚能看的内容越多，
    //   cols: 97,
    //   rows: 50,
    //   screenKeys: true, // 是否启用应用程序光标键（DECCKM）
    // });

    // 获取容器宽高/字号大小，定义行数和列数
    const viewportHeight = window.innerHeight;
    // 向下取整
    const rows = Math.floor(viewportHeight / 16) - 3;
    term = new Terminal({
      rendererType: "canvas", //渲染类型
      pid: 1,
      name: 'terminal',
      // rows: 60, //行数
      rows: rows, //行数
      cols: 80,
      convertEol: true, //启用时，光标将设置为下一行的开头
      scrollback: 500,//终端中的回滚量
      disableStdin: false, //是否应禁用输入。
      cursorStyle: 'underline', //光标样式
      cursorBlink: true, //光标闪烁
      tabStopWidth: 8, //制表宽度
      screenKeys: true, //Xterm下的快捷键
      theme: {
        // foreground: '#58a6ff', //字体,LightGreen,Orange,SlateBlue,Magenta Purple Red Violet White Yellow
        foreground: 'LightGreen', //字体,LightGreen,Orange,SlateBlue,Magenta Purple Red Violet White Yellow
        background: '#2B2B2B', //背景色
        // foreground: "#7e9192", //字体
        // background: "#002833", //背景色
        cursor: "Orange", //设置光标
        lineHeight: 16
      }
    });


    const fitAddon = new FitAddon();
    term.loadAddon(fitAddon);


    term.onData(data => {  // 粘贴的情况
      sendClientData(data);
    })

    term.open(element);
    fitAddon.fit();
    term.write(username + '@'+ host+':'+port+'  connecting...');

    webSocketConnect({id, host, port, username, operate});
    term.focus();
  }

  function sendInitData(options) {
    socket.send(JSON.stringify(options));
  }

  function sendClientData(data) {
    socket.send(JSON.stringify({"operate": "command", "command": data}));
  }

  function webSocketConnect(options) {
    const endpoint = generateEndpoint();
    if (window.WebSocket) {
      socket = new WebSocket(endpoint);
    } else {
      showError('您的浏览器不支持WebSocket！请更换浏览器再试。');
      return;
    }
    socket.onopen = function () {
      //连接成功回调
      sendInitData(options);
    }
    socket.onmessage = function (evt) {
      let data = evt.data.toString();
      term.write(data);
    }
    socket.onclose = function (evt) {
      //连接关闭回调
      term.write("\rconnection closed");
    }
  }


  // 在需要的时候关闭连接
  function closeWebSocket() {
    socket.close();
  }


  useEffect(() => {
    initTerm();
    return () => {
      closeWebSocket();
    }
  }, []);



  return (
    <div>
      <div id="terminal"></div>
      {/*<div id="terminal">123124</div>*/}

    </div>
  );
};

export default TerminalPage;