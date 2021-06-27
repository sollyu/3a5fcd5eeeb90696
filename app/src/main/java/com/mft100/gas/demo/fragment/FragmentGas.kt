package com.mft100.gas.demo.fragment

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.google.gson.Gson
import com.mft100.gas.demo.R
import com.mft100.gas.demo.databinding.FragmentGasBinding
import com.mft100.gas.demo.fragment.gas.pojo.GasPojoSummary
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.ref.SoftReference
import javax.inject.Inject

@FragmentScoped
@AndroidEntryPoint
internal class FragmentGas : FragmentBase(), View.OnClickListener {

    private lateinit var mViewBinding: FragmentGasBinding
    private lateinit var mTopBarButtonBack: AppCompatImageButton

    @Inject internal lateinit var mGson: Gson

    override fun onCreateView(): View {
        mViewBinding = FragmentGasBinding.inflate(LayoutInflater.from(requireContext()), baseFragmentActivity.fragmentContainerView, false)
        return mViewBinding.root
    }

    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        val context: Context = rootView.context
        mViewBinding.qmuiTopBarLayout.setTitle(R.string.app_name)
        mTopBarButtonBack = mViewBinding.qmuiTopBarLayout.addLeftBackImageButton()
        mTopBarButtonBack.setOnClickListener(this)

        mViewBinding.btn001.setOnClickListener(this)
        mViewBinding.btn002.setOnClickListener(this)
        mViewBinding.btn003.setOnClickListener(this)

        val orientation = context.resources.configuration.orientation
        val layoutParams = mViewBinding.mainContent.layoutParams as SlidingPaneLayout.LayoutParams
        layoutParams.marginStart = if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) 0 else QMUIDisplayHelper.dp2px(context, 63)
        mViewBinding.mainContent.layoutParams = layoutParams

        Observable.just(300L, 1000L)
            .subscribeOn(Schedulers.io())
            .map { Thread.sleep(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Unit>() {
                override fun onNext(t: Unit) {
                    if (mViewBinding.slidingPaneLayout.isOpen)
                        mViewBinding.slidingPaneLayout.closePane()
                    else
                        mViewBinding.slidingPaneLayout.openPane()
                }

                override fun onComplete() {
                    mViewBinding.btn001.performClick()
                }

                override fun onError(e: Throwable?) {
                    logger.error("LOG:FragmentGas:onError", e)
                }
            })
    }

    override fun onClick(@NotNull view: View) {
        when (view) {
            mTopBarButtonBack   -> onClickTopBarBack(view)
            mViewBinding.btn001 -> onClickLeftButton001(view)
            mViewBinding.btn002 -> onClickLeftButton002(view)
            mViewBinding.btn003 -> onClickLeftButton003(view)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val context: Context = requireActivity()
        logger.info("LOG:FragmentGas:onResume:orient={} ", newConfig.orientation)
        val s = mViewBinding.mainContent.layoutParams as SlidingPaneLayout.LayoutParams
        s.marginStart = if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) 0 else QMUIDisplayHelper.dp2px(context, 63)
        mViewBinding.mainContent.layoutParams = s
    }

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var softReference: SoftReference<FragmentGasContent>? = null

    private fun onClickLeftButton001(view: View) {
        val context: Context = view.context
        val jsonData = context.assets.open("json/data01.json").reader(charset = Charsets.UTF_8).readText()
        val gasDataList = mGson.fromJson(jsonData, Array<GasPojoSummary>::class.java)
        val delay: Long = if (mViewBinding.slidingPaneLayout.isOpen) 300L else 1L

        val content = FragmentGasContent(gasDataList.toMutableList(), delay)
        softReference = SoftReference(content)
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, content)
        fragmentTransaction.commit()

        mViewBinding.slidingPaneLayout.closePane()
    }

    private fun onClickLeftButton002(view: View) {
        val context: Context = view.context
        val jsonData = context.assets.open("json/data02.json").reader(charset = Charsets.UTF_8).readText()
        val gasDataList = mGson.fromJson(jsonData, Array<GasPojoSummary>::class.java)
        val delay: Long = if (mViewBinding.slidingPaneLayout.isOpen) 300L else 1L

        val content = FragmentGasContent(gasDataList.toMutableList(), delay)
        softReference = SoftReference(content)
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, content)
        fragmentTransaction.commit()

        mViewBinding.slidingPaneLayout.closePane()
    }

    private fun onClickLeftButton003(view: View) {
        val context: Context = view.context
        val jsonData = context.assets.open("json/data03.json").reader(charset = Charsets.UTF_8).readText()
        val gasDataList = mGson.fromJson(jsonData, Array<GasPojoSummary>::class.java)
        val delay: Long = if (mViewBinding.slidingPaneLayout.isOpen) 300L else 1L

        val content = FragmentGasContent(gasDataList.toMutableList(), delay)
        softReference = SoftReference(content)
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, content)
        fragmentTransaction.commit()

        mViewBinding.slidingPaneLayout.closePane()
    }

    private fun onClickTopBarBack(view: View) {
        if (softReference?.get() != null) {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.remove(softReference!!.get()!!)
            fragmentTransaction.commit()
        }
        popBackStack()
    }

}
