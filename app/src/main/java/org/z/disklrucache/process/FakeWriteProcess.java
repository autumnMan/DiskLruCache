package org.z.disklrucache.process;

import android.os.AsyncTask;
import android.text.TextUtils;

public class FakeWriteProcess {
    public interface WriteListener {
        void onSuccess(String msg);
        void onFailed(Exception e);
    }

    public static class DefWriteListener implements WriteListener {
        public String mMsg;
        public Exception mException;

        public DefWriteListener() {

        }

        @Override
        public void onSuccess(String msg) {
            mMsg = msg;
        }

        @Override
        public void onFailed(Exception e) {
            mException = e;
        }
    }

    private CusWriteTask mTask;

    public FakeWriteProcess() {
    }

    public void setWriteTask(CusWriteTask task) {
        mTask = task;
    }

    public void doWrite(final String msg, WriteListener listener) {
        if (mTask == null) {
            mTask = new CusWriteTask();
        }
        mTask.setListener(listener);
        mTask.execute(msg);
    }

    public void doWrite2(final String msg, final WriteListener listener) {
        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(String result) {
                if (listener != null) {
                    if (!TextUtils.isEmpty(result)) {
                        listener.onSuccess(result);
                    } else {
                        listener.onFailed(new NullPointerException("just a test!"));
                    }
                }
            }
        }.execute(msg);
    }

    static class CusWriteTask extends AsyncTask<String, Void, String> {

        private WriteListener mListener;

        public CusWriteTask() {
        }

        public void setListener(WriteListener listener) {
            mListener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            if (mListener != null) {
                if (!TextUtils.isEmpty(result)) {
                    mListener.onSuccess(result);
                } else {
                    mListener.onFailed(new NullPointerException("just a test!"));
                }
            }
        }
    }
}
