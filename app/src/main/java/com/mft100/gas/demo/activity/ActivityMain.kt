package com.mft100.gas.demo.activity

import cn.mohjong.android.fragment.FragmentSplash
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.qmuiteam.qmui.skin.QMUISkinManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@DefaultFirstFragment(FragmentSplash::class)
internal class ActivityMain : ActivityBase(), QMUISkinManager.OnSkinChangeListener