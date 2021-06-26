package com.mft100.gas.demo.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.mft100.gas.demo.constant.ConstantApp
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import javax.inject.Inject

internal open class ActivityBase : QMUIFragmentActivity(), QMUISkinManager.OnSkinChangeListener {
    @Inject internal lateinit var qmuiSkinManager: QMUISkinManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skinManager = qmuiSkinManager
        qmuiSkinManager.addSkinChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        qmuiSkinManager.removeSkinChangeListener(this)
    }

    override fun onSkinChange(skinManager: QMUISkinManager?, oldSkin: Int, newSkin: Int) {
        if (newSkin == ConstantApp.THEME_LIGHT) {
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }
    }

    override fun onResume() {
        super.onResume()

        val context: Context = this
        val isDarkMode: Boolean = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        when {
            isDarkMode.not() && qmuiSkinManager.currentSkin != ConstantApp.THEME_LIGHT -> qmuiSkinManager.changeSkin(ConstantApp.THEME_LIGHT)
            isDarkMode && qmuiSkinManager.currentSkin != ConstantApp.THEME_DARK        -> qmuiSkinManager.changeSkin(ConstantApp.THEME_DARK)
        }
    }


}