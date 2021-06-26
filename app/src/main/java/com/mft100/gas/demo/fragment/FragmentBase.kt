package com.mft100.gas.demo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Looper
import android.os.MessageQueue
import android.view.View
import androidx.annotation.IntDef
import com.qmuiteam.qmui.arch.QMUIFragment

internal abstract class FragmentBase : QMUIFragment(), MessageQueue.IdleHandler {

    @IntDef(flag = true, value = [Activity.RESULT_OK, Activity.RESULT_FIRST_USER, Activity.RESULT_CANCELED])
    @Retention(AnnotationRetention.SOURCE)
    annotation class FragmentResultCode

    /**
     * 当UI线程出现空闲时
     *
     * @since 0.0.1
     * @see   queueIdle
     */
    open fun onQueueIdle(): Boolean = false

    /**
     * 页面初始化
     *
     * @since 0.0.1
     */
    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        Looper.myQueue().addIdleHandler(this)
    }

    /**
     * 页面返回
     *
     * @param resultCode 返回值
     * @param data       内容
     * @since 0.0.1
     */
    @Suppress(names = ["DEPRECATION"])
    fun popBackStackResult(@FragmentResultCode resultCode: Int = Activity.RESULT_OK, data: Intent? = null) {
        this.setFragmentResult(resultCode, data)
        this.popBackStack()
    }

    /**
     * IdleHandler 默认回调
     *
     * @since 0.0.1
     */
    final override fun queueIdle(): Boolean = onQueueIdle()

}