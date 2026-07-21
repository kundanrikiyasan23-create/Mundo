package com.ncore.core.system;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ncore.BlackBoxCore;
import com.ncore.core.env.AppSystemEnv;
import com.ncore.core.env.BEnvironment;
import com.ncore.core.system.accounts.BAccountManagerService;
import com.ncore.core.system.am.BActivityManagerService;
import com.ncore.core.system.am.BJobManagerService;
import com.ncore.core.system.location.BLocationManagerService;
import com.ncore.core.system.notification.BNotificationManagerService;
import com.ncore.core.system.os.BStorageManagerService;
import com.ncore.core.system.pm.BPackageInstallerService;
import com.ncore.core.system.pm.BPackageManagerService;
import com.ncore.core.system.pm.BXposedManagerService;
import com.ncore.core.system.user.BUserHandle;
import com.ncore.core.system.user.BUserManagerService;
import com.ncore.entity.pm.InstallOption;
import com.ncore.utils.FileUtils;

public class BlackBoxSystem {

    private static volatile BlackBoxSystem sBlackBoxSystem;
    private final List<ISystemService> mServices = new ArrayList<>();
    private static final AtomicBoolean isStartup = new AtomicBoolean(false);

    private BlackBoxSystem() { }

    public static BlackBoxSystem getSystem() {
        if (sBlackBoxSystem == null) {
            synchronized (BlackBoxSystem.class) {
                if (sBlackBoxSystem == null) {
                    sBlackBoxSystem = new BlackBoxSystem();
                }
            }
        }
        return sBlackBoxSystem;
    }

    public void startup() {
        if (isStartup.getAndSet(true)) {
            return;
        }
        // Load virtual environment
        BEnvironment.load();
        // Register core system services
        mServices.add(BPackageManagerService.get());
        mServices.add(BUserManagerService.get());
        mServices.add(BActivityManagerService.get());
        mServices.add(BJobManagerService.get());
        mServices.add(BStorageManagerService.get());
        mServices.add(BPackageInstallerService.get());
        mServices.add(BXposedManagerService.get());
        mServices.add(BProcessManagerService.get());
        mServices.add(BAccountManagerService.get());
        mServices.add(BLocationManagerService.get());
        mServices.add(BNotificationManagerService.get());
        // Notify system ready
        for (ISystemService service : mServices) {
            try {
                service.systemReady();
            } catch (Throwable ignored) {
                // Never break virtual startup
            }
        }

        // Pre-install system apps
        List<String> preInstallPackages = AppSystemEnv.getPreInstallPackages();
        for (String pkg : preInstallPackages) {
            try {
                if (!BPackageManagerService.get().isInstalled(pkg, BUserHandle.USER_ALL)) {
                    PackageInfo info = BlackBoxCore.getPackageManager().getPackageInfo(pkg, 0);
                    BPackageManagerService.get().installPackageAsUser(info.applicationInfo.sourceDir,InstallOption.installBySystem(),BUserHandle.USER_ALL);
                }
            } catch (PackageManager.NameNotFoundException ignored) {
            } catch (Throwable ignored) {
            }
        }
        // Init jar environment (SAFE)
        //initJarEnv();
    }
    
    private void initJarEnv() {
        // OPTIONAL: junit.jar (ignore if missing)
        try {
            InputStream junit = BlackBoxCore.getContext().getAssets().open("junit.jar");
            FileUtils.copyFile(junit,android.MetaCore.RemoteManager.JUNIT_JAR);
        } catch (Throwable ignored) {
            // junit.jar not present → safe to ignore
        }

        // REQUIRED: empty.jar
        try {
            InputStream empty = BlackBoxCore.getContext().getAssets().open("empty.jar");
            FileUtils.copyFile(empty,android.MetaCore.RemoteManager.EMPTY_JAR);
        } catch (Throwable e) {
            // empty.jar missing is a REAL problem
            e.printStackTrace();
        }
    }
}
