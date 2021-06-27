package com.mft100.gas.demo.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import com.google.gson.Gson
import com.mft100.gas.demo.R
import com.mft100.gas.demo.databinding.FragmentGasBinding
import com.mft100.gas.demo.fragment.gas.pojo.GasPojoSummary
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.ref.SoftReference
import java.util.concurrent.TimeUnit
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

        Single.just(Unit)
            .subscribeOn(Schedulers.io())
            .delay(300, TimeUnit.MILLISECONDS)
            .map { mViewBinding.slidingPaneLayout.openPane() }
            .delay(1, TimeUnit.SECONDS)
            .map { mViewBinding.slidingPaneLayout.closePane() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Boolean>() {
                override fun onSuccess(t: Boolean) {
                }

                override fun onError(e: Throwable?) {
                }
            })
    }

    override fun onClick(@NotNull view: View) {
        when (view) {
            mTopBarButtonBack   -> onClickTopBarBack(view)
            mViewBinding.btn001 -> onClickLeftButton001(view)
            mViewBinding.btn002 -> onClickLeftButton002(view)
        }
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
        val isSlideable = mViewBinding.slidingPaneLayout.isOpen
        val delay: Long = if (mViewBinding.slidingPaneLayout.isOpen) 300L else 1L
        logger.info("LOG:FragmentGas:onClickLeftButton002:isSlideable={} ", isSlideable)

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
