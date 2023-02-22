import dayjs from 'dayjs';
import { toastConstants } from '../constants';
import { toast } from 'react-toastify';

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
