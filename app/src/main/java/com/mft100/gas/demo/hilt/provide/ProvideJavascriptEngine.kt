package com.mft100.gas.demo.hilt.provide

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.lang.ref.SoftReference
import java.nio.charset.Charset
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProvideJavascriptEngine @Inject constructor() {
    private var mRxJavaCompletableExecuteScript: SoftReference<RxJavaCompletableExecuteScript>? = null

    private val mLogger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun executeScript(inputStream: InputStream, charset: Charset = Charsets.UTF_8) =
        executeScript(inputStream.reader(charset = charset).readText())

    fun executeScript(script: String): Completable {
        val rxJavaCompletableExecuteScript = RxJavaCompletableExecuteScript(script)
        mRxJavaCompletableExecuteScript = SoftReference(rxJavaCompletableExecuteScript)
        return rxJavaCompletableExecuteScript
    }

    fun onExit() {
        mLogger.info("LOG:ProvideJavascriptEngine:onExit:1={} ", 1)
        mLogger.info("LOG:ProvideJavascriptEngine:onExit:mRxJavaCompletableExecuteScript={} ", mRxJavaCompletableExecuteScript)
        mLogger.info("LOG:ProvideJavascriptEngine:onExit:mRxJavaCompletableExecuteScript={} ", mRxJavaCompletableExecuteScript?.get())
        mRxJavaCompletableExecuteScript?.get()?.onExit()
    }

    fun interrupt() {
        mRxJavaCompletableExecuteScript?.get()?.interrupt()
    }

    private fun createJavascriptEngine(): V8 {
        val v8Engine = V8.createV8Runtime()
        val touchJs = V8Object(v8Engine)

        v8Engine.add("TouchJs", touchJs)
        return v8Engine
    }

    private inner class RxJavaCompletableExecuteScript(private val script: String) : Completable(), Handler.Callback {

        private lateinit var mHandler: Handler
        private lateinit var mJavascriptEngine: V8
        private lateinit var mObserver: CompletableObserver
        private lateinit var mThread: Thread

        private val mLogger: Logger = LoggerFactory.getLogger(this.javaClass)

        override fun subscribeActual(observer: CompletableObserver) {
            mThread = Thread.currentThread()
            mObserver = observer
            mJavascriptEngine = createJavascriptEngine()

            mLogger.info("LOG:RxJavaCompletableExecuteScript:subscribeActual:1={} ", Thread.currentThread().name)
            try {
                Executors.newCachedThreadPool()
                // 执行脚本
                mJavascriptEngine.executeVoidScript(script)
                Looper.prepare()
                mHandler = Handler(Looper.myLooper()!!, this)
                mHandler.obtainMessage(Event.EMPTY.ordinal).sendToTarget()
                Looper.loop()
                mJavascriptEngine.release(false)
                observer.onComplete()
                mLogger.info("LOG:RxJavaCompletableExecuteScript:subscribeActual:1={} ", 1)
            } catch (e: Exception) {
                mJavascriptEngine.release(false)
                observer.onError(e)
            } finally {
                mRxJavaCompletableExecuteScript = null
            }
        }

        fun onExit() {
            mLogger.info("LOG:RxJavaCompletableExecuteScript:onExit:1={} ", 1)
            mHandler.obtainMessage(Event.EXIT.ordinal).sendToTarget()
        }

        fun interrupt() {
            mThread.interrupt()
        }

        override fun handleMessage(msg: Message): Boolean {
            mLogger.info("LOG:RxJavaCompletableExecuteScript:handleMessage:msg={} Thread={}", msg, Thread.currentThread().name)
            when (msg.what) {
                Event.EXIT.ordinal -> callToJavascriptEventExit()
            }
            return true
        }

        private fun callToJavascriptEventExit() {
            mLogger.info("LOG:RxJavaCompletableExecuteScript:callToJavascriptEventExit:1={} ", 11)
            Looper.myLooper()?.quit()
            mLogger.info("LOG:RxJavaCompletableExecuteScript:callToJavascriptEventExit:2={} ", 2)
        }


    }

    private enum class Event {
        EMPTY, EXIT
    }

}