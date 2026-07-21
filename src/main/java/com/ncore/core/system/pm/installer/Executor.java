package com.ncore.core.system.pm.installer;

import com.ncore.core.system.pm.BPackageSettings;
import com.ncore.entity.pm.InstallOption;

public interface Executor {
    public static final String TAG = "InstallExecutor";

    int exec(BPackageSettings bPackageSettings, InstallOption installOption, int i);
}
