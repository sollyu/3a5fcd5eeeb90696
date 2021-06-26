package com.mft100.gas.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mft100.gas.demo.R
import com.mft100.gas.demo.databinding.FragmentGasContentBinding
import com.mft100.gas.demo.databinding.ItemGasWidget001Binding
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindUntilEvent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

@FragmentScoped
@AndroidEntryPoint
internal class FragmentGasContent : Fragment() {

    private lateinit var mViewBinding: FragmentGasContentBinding
    private val mRecyclerViewAdapt: MultipleItemQuickAdapter = MultipleItemQuickAdapter()

    private val mLogger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewBinding = FragmentGasContentBinding.inflate(inflater, container, false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLogger.info("LOG:FragmentGasContent:onViewCreated 1={}", 1)
        val context = view.context
        val mutableList = mutableListOf(
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "01一二三四五六七八大九十".repeat(n = 1), isFull = false),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "02一二三四五六七八大九十".repeat(n = 7), isFull = false),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "03一二三四五六七八大九十".repeat(n = 200), isFull = true),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "04一二三四五六七八大九十".repeat(n = 100), isFull = false),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "05一二三四五六七八大九十".repeat(n = 40), isFull = false),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "06一二三四五六七八大九十".repeat(n = 204), isFull = false),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "07一二三四五六七八大九十".repeat(n = 60), isFull = false),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "08一二三四五六七八大九十".repeat(n = 90), isFull = false),
            QuickMultipleEntity(QuickMultipleEntity.widget_001, text = "09一二三四五六七八大九十".repeat(n = 40), isFull = false),
        )

        mViewBinding.recyclerView.setHasFixedSize(true)
        mViewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mViewBinding.recyclerView.adapter = mRecyclerViewAdapt

        mRecyclerViewAdapt.animationEnable = true
        mRecyclerViewAdapt.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        mRecyclerViewAdapt.setList(mutableList)

        Observable.interval(3000, 50, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .bindUntilEvent(owner = this, event = Lifecycle.Event.ON_DESTROY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                (mRecyclerViewAdapt.getViewByPosition(2, R.id.tv) as AppCompatTextView?)?.text = it.toString()
                (mRecyclerViewAdapt.getViewByPosition(5, R.id.tv) as AppCompatTextView?)?.text = it.toString()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLogger.info("LOG:FragmentGasContent:onDestroyView 1={}", 1)
    }

    private inner class MultipleItemQuickAdapter : BaseMultiItemQuickAdapter<QuickMultipleEntity, BaseViewHolder>() {

        init {
            addItemType(QuickMultipleEntity.widget_001, R.layout.item_gas_widget_001)
        }

        override fun convert(holder: BaseViewHolder, item: QuickMultipleEntity) {
            val p = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            p.isFullSpan = item.isFull
            holder.itemView.layoutParams = p

            when (holder.itemViewType) {
                QuickMultipleEntity.widget_001 -> {
                    val viewBinding: ItemGasWidget001Binding = ItemGasWidget001Binding.bind(holder.itemView)
                    viewBinding.tv.text = item.text
                }
            }
        }
    }

    private class QuickMultipleEntity(val itemViewType: Int, var text: String, val isFull: Boolean = false) : MultiItemEntity {
        companion object {
            const val widget_001 = 0x001
        }

        override val itemType: Int
            get() = itemViewType

    }

}
