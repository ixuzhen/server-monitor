import React, { useEffect } from 'react';

import { useRoutes } from 'react-router-dom';
import routers from './routers/routers';

const App = () => {
  const outlet = useRoutes(routers);
  return <div>{outlet}</div>;
};

export default App;
