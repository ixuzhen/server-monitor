// 创建axios实例
import axios from 'axios';
import { isSuccess, showError } from '../helper/utils';

// const baseURL = 'http://125.216.243.209:8080';
// const baseURL = 'http://127.0.0.1:8080';
// const baseURL = '';
// 获取协议部分
const protocol = window.location.protocol;    // 输出: "http:"
// 获取主机部分（域名和端口）
const host = window.location.host;            // 输出: "127.0.0.1:8080"
// 获取主机部分（域名）
const hostname = window.location.hostname;    // 输出: "127.0.0.1"
// 获取端口部分
const port = window.location.port;            // 输出: "8080"

const baseURL = process.env.REACT_APP_SERVER
  ? process.env.REACT_APP_SERVER
  : protocol + '//' + hostname + ':8080';
// 把REACT_APP_SERVER前边的http://或者https://去掉
let endpointHostAddress = process.env.REACT_APP_SERVER
  ? baseURL.replace(/(http|https):\/\//, '')
  : hostname + ':8080';

// const reactURL = process.env.REACT_APP_SERVER;

const LoginAPI = axios.create({
  // 基本请求路径的抽取
  baseURL,
  // 这个时间是你每次请求的过期时间，这次请求认为2秒之后这个请求就是失败的
  timeout: 5000,
});

// 请求相应
LoginAPI.interceptors.response.use(
  (response) => response,
  (error) => {
    showError(error);
  }
);

const API = axios.create({
  // 基本请求路径的抽取
  baseURL,
  // 这个时间是你每次请求的过期时间，这次请求认为5秒之后这个请求就是失败的
  timeout: 5000,
});

// 请求拦截器，用来加token
API.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    config.headers['token'] = token;
    // TODO: 如果获取不到token怎么办
    // 后端做处理
    // console.log('@' + reactURL);

    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);

// 响应拦截器
API.interceptors.response.use(
  (response) => {
    const data = response.data;
    // console.log(data);
    if (isSuccess(data['code'])) return response;
    if (data['code'] === 401) {
      localStorage.removeItem('token');
    }
    showError(data['message']);
  },
  (error) => {
    showError(error);
  }
);

export { LoginAPI, API ,endpointHostAddress};
