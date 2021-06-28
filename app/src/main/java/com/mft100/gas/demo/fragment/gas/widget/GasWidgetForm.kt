package com.mft100.gas.demo.fragment.gas.widget

import android.content.Context
import android.view.KeyEvent
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mft100.gas.demo.R
import com.mft100.gas.demo.constant.ConstantGasType
import com.mft100.gas.demo.databinding.*
import com.mft100.gas.demo.fragment.gas.pojo.GasPojoSummary

internal object GasWidgetForm {

    fun convert(holder: BaseViewHolder, viewBinding: ItemGasWidgetFormBinding, item: GasPojoSummary) {
        val context: Context = viewBinding.root.context
        val adapter = viewBinding.recyclerView.getTag(R.id.tag_adapter) as QuickAdapter? ?: QuickAdapter()

        viewBinding.tvTitle.text = item.title
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewBinding.recyclerView.setTag(R.id.tag_adapter, adapter)
        adapter.setList(item.items)
    }

    internal class QuickAdapter : BaseMultiItemQuickAdapter<GasPojoSummary.Item, BaseViewHolder>(), TextView.OnEditorActionListener {

        init {
            addItemType(ConstantGasType.FORM_TEXT, R.layout.item_gas_widget_form_text)
            addItemType(ConstantGasType.FORM_SWITCH, R.layout.item_gas_widget_form_switch)
            addItemType(ConstantGasType.FORM_TOGGLE, R.layout.item_gas_widget_form_toggle)
            addItemType(ConstantGasType.FORM_EDIT, R.layout.item_gas_widget_form_edit)
        }

        override fun convert(holder: BaseViewHolder, item: GasPojoSummary.Item) {
            when (item.itemType) {
                ConstantGasType.FORM_TEXT   -> convert(ItemGasWidgetFormTextBinding.bind(holder.itemView), item)
                ConstantGasType.FORM_SWITCH -> convert(ItemGasWidgetFormSwitchBinding.bind(holder.itemView), item)
                ConstantGasType.FORM_TOGGLE -> convert(ItemGasWidgetFormToggleBinding.bind(holder.itemView), item)
                ConstantGasType.FORM_EDIT   -> convert(ItemGasWidgetFormEditBinding.bind(holder.itemView), item)
            }
        }

        private fun convert(viewBinding: ItemGasWidgetFormTextBinding, item: GasPojoSummary.Item) {
            viewBinding.tvTitle.text = item.title
            viewBinding.tvValue.text = item.value
        }

        private fun convert(viewBinding: ItemGasWidgetFormSwitchBinding, item: GasPojoSummary.Item) {
            viewBinding.tvTitle.text = item.title
            viewBinding.tvValue.isChecked = item.value.toBoolean()
        }

        private fun convert(viewBinding: ItemGasWidgetFormToggleBinding, item: GasPojoSummary.Item) {
            viewBinding.tvTitle.text = item.title
            viewBinding.tvValue.isChecked = item.value.toBoolean()
        }

        private fun convert(viewBinding: ItemGasWidgetFormEditBinding, item: GasPojoSummary.Item) {
            viewBinding.tvTitle.text = item.title
            viewBinding.tvValue.setText(item.value)
            viewBinding.tvValue.setOnEditorActionListener(this)
        }

        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            v?.clearFocus()
            return false
        }

    }
}