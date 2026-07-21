package com.ncore.fake.hook;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import com.ncore.BlackBoxCore;

public abstract class MethodHook {
    protected String getMethodName() {
        return null;
    }

    protected Object afterHook(Object result) throws Throwable {
        return result;
    }

    protected Object beforeHook(Object who, Method method, Object[] args) throws Throwable {
        return null;
    }

    protected abstract Object hook(Object who, Method method, Object[] args) throws Throwable;

    protected boolean isEnable() {
        return BlackBoxCore.get().isBlackProcess();
    }
}
