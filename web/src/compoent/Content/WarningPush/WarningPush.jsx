import React, { useState } from 'react';
import { Button, Modal } from 'antd';
import WarningTable from './WarningTable/WarningTable';
const WarningPush = () => {
  return (
    <div>
      {/*<WarningHeader />*/}
      <div style={{ marginTop: 20 }}>
        <WarningTable />
      </div>
    </div>
  );
};

export default WarningPush;
