package com.mft100.gas.demo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mft100.gas.demo.R
import com.mft100.gas.demo.constant.ConstantGasType
import com.mft100.gas.demo.databinding.FragmentGasContentBinding
import com.mft100.gas.demo.databinding.ItemGasWidgetDashboardBinding
import com.mft100.gas.demo.databinding.ItemGasWidgetFormBinding
import com.mft100.gas.demo.databinding.ItemGasWidgetHtmlBinding
import com.mft100.gas.demo.fragment.gas.pojo.GasPojoSummary
import com.mft100.gas.demo.fragment.gas.widget.GasWidgetDashboard
import com.mft100.gas.demo.fragment.gas.widget.GasWidgetForm
import com.mft100.gas.demo.fragment.gas.widget.GasWidgetHtml
import com.mft100.gas.demo.hilt.provide.ProvideJavascriptEngine
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindToLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@FragmentScoped
@AndroidEntryPoint
internal class FragmentGasContent(private val mutableList: MutableList<GasPojoSummary>) : Fragment() {

    private lateinit var mViewBinding: FragmentGasContentBinding
    private val mRecyclerViewAdapt: MultipleItemQuickAdapter = MultipleItemQuickAdapter()

    private val mLogger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Inject internal lateinit var mJavascriptEngine: ProvideJavascriptEngine

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewBinding = FragmentGasContentBinding.inflate(inflater, container, false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLogger.info("LOG:FragmentGasContent:onViewCreated 1={}", 1)

        val context: Context = view.context

        mViewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mViewBinding.recyclerView.adapter = mRecyclerViewAdapt

        mRecyclerViewAdapt.animationEnable = true
        mRecyclerViewAdapt.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        mRecyclerViewAdapt.setList(mutableList)

        Observable.interval(5000, 100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .bindToLifecycle(owner = this)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                fun updateFunction(positionMajor: Int, positionMinor: Int): Unit {
                    mRecyclerViewAdapt.data[positionMajor].items[positionMinor].value = Random.nextInt(from = 1, until = 99).toString()
                    val recyclerView: RecyclerView = mRecyclerViewAdapt.getViewByPosition(positionMajor, R.id.recycler_view) as RecyclerView
                    val adapter = recyclerView.getTag(R.id.tag_adapter) as GasWidgetDashboard.MultipleItemQuickAdapter
                    adapter.notifyItemChanged(positionMinor)
                }
                updateFunction(positionMajor = 0, positionMinor = 1)
                updateFunction(positionMajor = 3, positionMinor = 2)
            }

//
//        mJavascriptEngine.executeScript(context.assets.open("scripts/demo.js"), charset = Charsets.UTF_8)
//            .subscribeOn(Schedulers.newThread())
//            .bindUntilEvent(owner = this, event = Lifecycle.Event.ON_DESTROY)
//            .delay(300, TimeUnit.MILLISECONDS)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : DisposableCompletableObserver() {
//                private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
//                override fun onComplete() {
//                    logger.info("LOG:FragmentGasContent:onComplete:1={} ", 1)
//                }
//
//                override fun onError(e: Throwable) {
//                    logger.error("LOG:FragmentGasContent:onError", e)
//                }
//            })
    }

    override fun onDestroy() {
        super.onDestroy()
        mLogger.info("LOG:FragmentGasContent:onDestroy:1={} ", 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLogger.info("LOG:FragmentGasContent:onDestroyView 1={}", 1)
    }

    private inner class MultipleItemQuickAdapter : BaseMultiItemQuickAdapter<GasPojoSummary, BaseViewHolder>() {

        init {
            addItemType(ConstantGasType.DASHBOARD, R.layout.item_gas_widget_dashboard)
            addItemType(ConstantGasType.FORM, R.layout.item_gas_widget_form)
            addItemType(ConstantGasType.HTML, R.layout.item_gas_widget_html)
        }

        override fun convert(holder: BaseViewHolder, item: GasPojoSummary) {
            val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = item.isFull
            holder.itemView.layoutParams = layoutParams


            when (item.itemType) {
                ConstantGasType.DASHBOARD -> GasWidgetDashboard.convert(holder, ItemGasWidgetDashboardBinding.bind(holder.itemView), item)
                ConstantGasType.FORM      -> GasWidgetForm.convert(holder, ItemGasWidgetFormBinding.bind(holder.itemView), item)
                ConstantGasType.HTML      -> GasWidgetHtml.convert(holder, ItemGasWidgetHtmlBinding.bind(holder.itemView), item)
            }
        }
    }
}
