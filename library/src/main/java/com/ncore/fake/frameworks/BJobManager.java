package com.ncore.fake.frameworks;

import android.app.job.JobInfo;
import android.os.RemoteException;

import com.ncore.app.BActivityThread;
import com.ncore.core.system.ServiceManager;
import com.ncore.core.system.am.IBJobManagerService;
import com.ncore.entity.JobRecord;

/**
 * Created by @RIYAZXERO on 3/30/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class BJobManager extends BlackManager<IBJobManagerService> {
    private static final BJobManager sJobManager = new BJobManager();

    public static BJobManager get() {
        return sJobManager;
    }

    @Override
    protected String getServiceName() {
        return ServiceManager.JOB_MANAGER;
    }

    public JobInfo schedule(JobInfo info) {
        try {
            return getService().schedule(info, BActivityThread.getUserId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JobRecord queryJobRecord(String processName, int jobId) {
        try {
            return getService().queryJobRecord(processName, jobId, BActivityThread.getUserId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cancelAll(String processName) {
        try {
            getService().cancelAll(processName, BActivityThread.getUserId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int cancel(String processName, int jobId) {
        try {
            return getService().cancel(processName, jobId, BActivityThread.getUserId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
