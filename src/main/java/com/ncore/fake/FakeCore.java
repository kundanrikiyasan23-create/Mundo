package com.ncore.fake;

import com.ncore.jnihook.ReflectCore;

/**
 * Created by @RIYAZXERO on 3/7/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class FakeCore {
    public static void init() {
        ReflectCore.set(android.app.ActivityThread.class);
    }
}
