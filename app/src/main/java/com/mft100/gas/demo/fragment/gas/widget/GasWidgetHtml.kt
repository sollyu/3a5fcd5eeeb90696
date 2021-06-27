package com.mft100.gas.demo.fragment.gas.widget

import android.content.Context
import androidx.core.text.HtmlCompat
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mft100.gas.demo.databinding.ItemGasWidgetHtmlBinding
import com.mft100.gas.demo.fragment.gas.pojo.GasPojoSummary
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal object GasWidgetHtml {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    fun convert(holder: BaseViewHolder, bind: ItemGasWidgetHtmlBinding, item: GasPojoSummary) {
        val context: Context = holder.itemView.context
        holder.setIsRecyclable(true)

        bind.tvTitle.text = item.title
        bind.tvValue.text = HtmlCompat.fromHtml(item.items.first().value, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}