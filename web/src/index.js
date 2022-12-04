import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import Test from "./Test";
import {BrowserRouter} from "react-router-dom";
import axios from "axios";

axios.defaults.baseURL = 'http://localhost:8080';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
      <BrowserRouter>
          <App />
      </BrowserRouter>
    {/*<Test/>*/}
  </React.StrictMode>
);

