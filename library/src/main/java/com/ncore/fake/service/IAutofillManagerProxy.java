package com.ncore.fake.service;

import android.content.ComponentName;

import java.lang.reflect.Method;

import black.android.os.BRServiceManager;
import black.android.view.BRIAutoFillManagerStub;
import com.ncore.BlackBoxCore;
import com.ncore.app.BActivityThread;
import com.ncore.fake.hook.BinderInvocationStub;
import com.ncore.fake.hook.MethodHook;
import com.ncore.fake.hook.ProxyMethod;
import com.ncore.proxy.ProxyManifest;
import com.ncore.utils.MethodParameterUtils;

/**
 * Created by @RIYAZXERO on 4/8/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class IAutofillManagerProxy extends BinderInvocationStub {
	public static final String TAG = "AutofillManagerStub";

	public IAutofillManagerProxy() {
		super(BRServiceManager.get().getService("autofill"));
	}

	@Override
	protected Object getWho() {
		return BRIAutoFillManagerStub.get().asInterface(BRServiceManager.get().getService("autofill"));
	}

	@Override
	protected void inject(Object baseInvocation, Object proxyInvocation) {
		replaceSystemService("autofill");
	}

	@Override
	public boolean isBadEnv() {
		return false;
	}

	@ProxyMethod("startSession")
	public static class StartSession extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					if (args[i] == null) continue;
					if (args[i] instanceof ComponentName) {
						args[i] = new ComponentName(BlackBoxCore.getHostPkg(),ProxyManifest.getProxyActivity(BActivityThread.getAppPid()));
					}
				}
			}
			return method.invoke(who, args);
		}
	}
}
