package com.mft100.gas.demo.app

import android.app.Application
import android.content.res.Configuration
import com.google.common.base.Stopwatch
import com.mft100.gas.demo.R
import com.mft100.gas.demo.constant.ConstantApp
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.qmuiteam.qmui.skin.QMUISkinManager
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltAndroidApp
internal class ApplicationMain : Application(), Thread.UncaughtExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Inject internal lateinit var qmuiSkinManager: QMUISkinManager

    companion object {
        lateinit var shareInstance: Application
            private set
    }

    init {
        shareInstance = this
    }

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler(this)
        RxJavaCompletable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(RxJavaObserver())
    }

    private inner class RxJavaCompletable : Completable() {
        val application: Application = this@ApplicationMain
        override fun subscribeActual(observer: CompletableObserver) {
            QMUISwipeBackActivityManager.init(application)
            qmuiSkinManager.addSkin(ConstantApp.THEME_LIGHT, R.style.AppThemeLight)
            qmuiSkinManager.addSkin(ConstantApp.THEME_DARK, R.style.AppThemeDark)
            val isDarkMode: Boolean = (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            qmuiSkinManager.changeSkin(if (isDarkMode) ConstantApp.THEME_DARK else ConstantApp.THEME_LIGHT)
        }
    }

    private inner class RxJavaObserver : DisposableCompletableObserver() {
        private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
        private val stopwatch: Stopwatch = Stopwatch.createUnstarted()

        override fun onStart() {
            super.onStart()
            stopwatch.start()
        }

        override fun onComplete() {
            stopwatch.stop()
            logger.info("LOG:RxJavaObserver:onComplete stopwatch={}", stopwatch)
        }

        override fun onError(e: Throwable) {
            logger.error("LOG:RxJavaObserver:onError", e)
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        logger.error("LOG:ApplicationMain:uncaughtException t={}", t, e)
        exitProcess(status = -1)
    }

}