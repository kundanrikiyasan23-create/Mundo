package com.ncore.core.system.pm.installer;

import com.ncore.core.env.BEnvironment;
import com.ncore.core.system.pm.BPackageSettings;
import com.ncore.entity.pm.InstallOption;
import com.ncore.utils.FileUtils;

public class RemoveAppExecutor implements Executor {
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        FileUtils.deleteDir(BEnvironment.getAppDir(ps.pkg.packageName));
        return 0;
    }
}
