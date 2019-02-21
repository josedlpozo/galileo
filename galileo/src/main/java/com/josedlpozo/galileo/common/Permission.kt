package com.josedlpozo.galileo.common

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import java.lang.reflect.InvocationTargetException

object Permission {

    private const val OP_SYSTEM_ALERT_WINDOW = 24

    fun canDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else checkOp(context, OP_SYSTEM_ALERT_WINDOW)
    }

    private fun checkOp(context: Context, op: Int): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val clazz = AppOpsManager::class.java
            try {
                val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Process.myUid(), context.packageName) as Int
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
        return true
    }

    fun requestDrawOverlays(context: Context) {
        val intent = Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.packageName))
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}