package com.mft100.gas.demo.fragment.gas.widget

import android.content.Context
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mft100.gas.demo.R
import com.mft100.gas.demo.constant.ConstantGasType
import com.mft100.gas.demo.databinding.ItemGasWidgetDashboardBinding
import com.mft100.gas.demo.databinding.ItemGasWidgetDashboardImageBinding
import com.mft100.gas.demo.databinding.ItemGasWidgetDashboardProgressBinding
import com.mft100.gas.demo.fragment.gas.pojo.GasPojoSummary

internal object GasWidgetDashboard {

    fun convert(holder: BaseViewHolder, viewBinding: ItemGasWidgetDashboardBinding, item: GasPojoSummary) {
        holder.setIsRecyclable(false)

        val context: Context = viewBinding.root.context
        val adapter = MultipleItemQuickAdapter()
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = flexboxLayoutManager
        viewBinding.tvTitle.text = item.title
        viewBinding.recyclerView.setTag(R.id.tag_adapter, adapter)
        adapter.setList(item.items)
    }

    internal class MultipleItemQuickAdapter : BaseMultiItemQuickAdapter<GasPojoSummary.Item, BaseViewHolder>() {
        init {
            addItemType(ConstantGasType.DASHBOARD_PROGRESS, R.layout.item_gas_widget_dashboard_progress)
            addItemType(ConstantGasType.DASHBOARD_IMAGE, R.layout.item_gas_widget_dashboard_image)
        }

        override fun convert(holder: BaseViewHolder, item: GasPojoSummary.Item) {
            when (item.itemType) {
                ConstantGasType.DASHBOARD_PROGRESS -> convert(ItemGasWidgetDashboardProgressBinding.bind(holder.itemView), item)
                ConstantGasType.DASHBOARD_IMAGE    -> convert(ItemGasWidgetDashboardImageBinding.bind(holder.itemView), item)
            }
        }

        private fun convert(viewBinding: ItemGasWidgetDashboardImageBinding, item: GasPojoSummary.Item) {
            viewBinding.tvTitle.text = item.title
        }

        private fun convert(viewBinding: ItemGasWidgetDashboardProgressBinding, item: GasPojoSummary.Item) {
            viewBinding.qmuiProgressBar.maxValue = item.max?.toIntOrNull() ?: 100
            viewBinding.qmuiProgressBar.progress = item.value.toInt()
            viewBinding.tvTitle.text = item.title
        }

    }
}