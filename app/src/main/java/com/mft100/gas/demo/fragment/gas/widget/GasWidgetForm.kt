package com.mft100.gas.demo.fragment.gas.widget

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mft100.gas.demo.R
import com.mft100.gas.demo.databinding.ItemGasWidgetFormBinding
import com.mft100.gas.demo.databinding.ItemGasWidgetFormItemBinding
import com.mft100.gas.demo.fragment.gas.pojo.GasPojoSummary

internal object GasWidgetForm {

    fun convert(holder: BaseViewHolder, viewBinding: ItemGasWidgetFormBinding, item: GasPojoSummary) {
        holder.setIsRecyclable(false)
        val context: Context = viewBinding.root.context
        val adapter = QuickAdapter(R.layout.item_gas_widget_form_item, item.items.toMutableList())
        viewBinding.tvTitle.text = item.title
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.tvTitle.text = item.title
        adapter.setList(item.items)
    }

    private class QuickAdapter(layoutResId: Int, data: MutableList<GasPojoSummary.Item>?) : BaseQuickAdapter<GasPojoSummary.Item, BaseViewHolder>(layoutResId, data) {
        override fun convert(holder: BaseViewHolder, item: GasPojoSummary.Item) {
            val viewBinding = ItemGasWidgetFormItemBinding.bind(holder.itemView)
            viewBinding.tvTitle.text = item.title
            viewBinding.tvValue.text = item.value
        }
    }
}