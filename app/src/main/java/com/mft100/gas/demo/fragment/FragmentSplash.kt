package cn.mohjong.android.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import com.google.common.base.Stopwatch
import com.google.gson.Gson
import com.mft100.gas.demo.databinding.FragmentSplashBinding
import com.mft100.gas.demo.fragment.FragmentBase
import com.mft100.gas.demo.fragment.FragmentMain
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindUntilEvent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScoped
@AndroidEntryPoint
internal class FragmentSplash : FragmentBase() {

    private lateinit var mViewBinding: FragmentSplashBinding

    @Inject internal lateinit var gson: Gson

    override fun onCreateView(): View {
        mViewBinding = FragmentSplashBinding.inflate(LayoutInflater.from(requireContext()), baseFragmentActivity.fragmentContainerView, false)
        return mViewBinding.root
    }

    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)

        val context: Context = rootView.context
        val isDarkMode: Boolean = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (isDarkMode)
            QMUIStatusBarHelper.setStatusBarDarkMode(requireActivity())
        else
            QMUIStatusBarHelper.setStatusBarLightMode(requireActivity())
    }

    override fun onQueueIdle(): Boolean {
        Observable.interval(1, 1, TimeUnit.SECONDS)
            .bindUntilEvent(owner = this, event = Lifecycle.Event.ON_DESTROY)
            .take(2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(RxJavaObserverInterval())
        return false
    }


    private inner class RxJavaObserverInterval : DisposableObserver<Long>(), DialogInterface.OnDismissListener {

        private val context: Context = requireContext()
        private val stopwatch: Stopwatch = Stopwatch.createUnstarted()

        override fun onStart() {
            super.onStart()
            stopwatch.start()
        }

        override fun onDismiss(dialog: DialogInterface) {
            dialog.dismiss()
            requireActivity().finish()
        }

        override fun onError(e: Throwable) {
            requireActivity().finish()
        }

        override fun onNext(t: Long) {
        }

        override fun onComplete() {
            stopwatch.stop()
            startFragmentAndDestroyCurrent(FragmentMain())
        }
    }
}