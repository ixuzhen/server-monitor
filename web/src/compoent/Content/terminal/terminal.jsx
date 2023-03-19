import React from 'react';
import { Button } from 'antd';

const Terminal = () => {
  function buttonClick() {
    if (true) {
      buildWebSocket();
    }
  }

  function buildWebSocket() {}
  var ws = new WebSocket('ws://localhost:8080/terminal');
  ws.onopen = function (en) {
    console.log('onopen');
  };

  return (
    <div>
      <Button onClick={buttonClick}>连接</Button>
    </div>
  );
};

export default Terminal;
