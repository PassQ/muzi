package com.ocwvar.muzi.Utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Project AnHe_Business
 * Created by 区成伟
 * On 2017/1/4 9:38
 * File Location com.ocwvar.anhe_business.Helper
 * 简单文件下载工具
 */

public class SimpleDownloader {

    private OkHttpClient httpClient;

    public SimpleDownloader() {
        httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        httpClient.setReadTimeout(10, TimeUnit.SECONDS);
        httpClient.setWriteTimeout(10, TimeUnit.SECONDS);
    }

    public interface OnDownloadCallback {

        /**
         * 下载文件成功
         *
         * @param downloadedFile 下载得到的文件对象
         */
        void onDownloadSuccess(File downloadedFile);

        /**
         * 下载文件失败
         */
        void onDownloadFailed();

    }

    /**
     * 下载文件
     *
     * @param url        文件网址
     * @param folderPath 文件保存路径
     * @param fileName   文件保存名称
     * @param callback   下载回调接口
     */
    public void downloadFile(String url, String folderPath, String fileName, OnDownloadCallback callback) {
        //按队列依次执行下载任务
        new DownloadThread(url, folderPath, fileName, callback).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private class DownloadThread extends AsyncTask<Void, Integer, File> {

        private String url;
        private String folderPath;
        private String fileName;

        private OnDownloadCallback callback;

        DownloadThread(@NonNull String url, @NonNull String folderPath, @NonNull String fileName, @NonNull OnDownloadCallback callback) {
            this.url = url;
            this.folderPath = folderPath;
            this.fileName = fileName;
            this.callback = callback;
        }

        @Override
        protected File doInBackground(Void... params) {

            //获取可用的文件对象
            final File downloadFile = checkValues(url, folderPath, fileName);

            //直接传递下载结果
            return download(url, downloadFile);
        }

        @Override
        protected void onPostExecute(File resultFile) {
            super.onPostExecute(resultFile);
            if (resultFile != null && callback != null) {
                callback.onDownloadSuccess(resultFile);
            } else if (resultFile == null && callback != null) {
                callback.onDownloadFailed();
            }
        }

        /**
         * 检查变量的正确性以及目录的可用性
         *
         * @param url        文件网址
         * @param folderPath 文件保存路径
         * @param fileName   文件保存名称
         * @return 成功：下载对象文件        失败：NULL
         */
        private File checkValues(String url, String folderPath, String fileName) {
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(folderPath) || TextUtils.isEmpty(fileName)) {
                //文字对象必须有效
                return null;
            } else if (folderPath.charAt(folderPath.length() - 1) != '/') {
                //如果保存路径的最后一位不是   "/"  则补上
                folderPath = folderPath + "/";
            }

            try {
                //检查下载目录的可用性
                final File folder = new File(folderPath);
                if ((!folder.exists() && folder.mkdirs()) || folder.exists()) {
                    //检测目录的可用性，如果不存在，则创建
                    if (folder.canRead() && folder.canWrite()) {
                        //确认目录可用后，则创建文件对象
                        return new File(folderPath + fileName);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 下载
         *
         * @param url          文件网址
         * @param downloadFile 文件保存对象
         * @return 成功：下载得到的文件      失败：NULL
         */
        private File download(String url, File downloadFile) {
            if (downloadFile == null) {
                return null;
            }

            if (httpClient == null) {
                httpClient = new OkHttpClient();
            }

            try {
                final Request request = new Request.Builder().url(url).build();
                final Response response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    final ResponseBody responseBody = response.body();
                    final InputStream inputStream = responseBody.byteStream();
                    final FileOutputStream outputStream = new FileOutputStream(downloadFile, false);
                    final long fileTotalLength = responseBody.contentLength();
                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    responseBody.close();
                    if (downloadFile.length() == fileTotalLength) {
                        return downloadFile;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }

    }

}
