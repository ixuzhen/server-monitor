import dayjs from 'dayjs';
import { toastConstants } from '../constants';
import { toast } from 'react-toastify';
import { createBrowserHistory } from 'history';

export function isSuccess(code) {
  if (code === 200) {
    return true;
  }
  return false;
}
export function isMobile() {
  return window.innerWidth <= 600;
}

export function paringDate(date, template) {
  return dayjs(date).format(template);
}

let showErrorOptions = {
  autoClose: toastConstants.ERROR_TIMEOUT,
};
let showSuccessOptions = { autoClose: toastConstants.SUCCESS_TIMEOUT };
let showInfoOptions = { autoClose: toastConstants.INFO_TIMEOUT };
let showNoticeOptions = { autoClose: false };

if (isMobile()) {
  showErrorOptions.position = 'top-center';
  // showErrorOptions.transition = 'flip';

  showSuccessOptions.position = 'top-center';
  // showSuccessOptions.transition = 'flip';

  showInfoOptions.position = 'top-center';
  // showInfoOptions.transition = 'flip';

  showNoticeOptions.position = 'top-center';
  // showNoticeOptions.transition = 'flip';
}

export function showError(error) {
  toast.error('错误：' + error, showErrorOptions);
  console.error(error);
}
// 创建一个自定义的history对象
const history = createBrowserHistory();

// 在某个事件或条件满足时执行路由跳转
function redirectToNewPage(path) {
  history.push(path); // 这里的 '/new-page' 是你要跳转的目标路径
}

export function showErrorWithCode(code, error) {

  if (code === 401) {
    localStorage.removeItem('token');
    redirectToNewPage('/login')
    toast.error('请重新登录');
  }else{
    toast.error('错误：' + error, showErrorOptions);
  }
}

export function showSuccess(message) {
  toast.success(message, showSuccessOptions);
}
