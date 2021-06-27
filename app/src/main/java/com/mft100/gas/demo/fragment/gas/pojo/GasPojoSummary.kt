package com.mft100.gas.demo.fragment.gas.pojo


import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName


data class GasPojoSummary(
    @SerializedName(value = "title")
    var title: String = "",

    @SerializedName(value = "type")
    override val itemType: Int = 0,

    @SerializedName(value = "is_full")
    var isFull: Boolean = false,

    @SerializedName(value = "items")
    var items: List<Item> = listOf()

) : MultiItemEntity {
    data class Item(
        @SerializedName(value = "title")
        var title: String = "",

        @SerializedName(value = "type")
        override val itemType: Int = 0,

        @SerializedName(value = "unit")
        var unit: String? = null,

        @SerializedName(value = "value")
        var value: String = "",

        @SerializedName(value = "max")
        var max: String? = null,

        @SerializedName(value = "min")
        var min: String? = null

    ) : MultiItemEntity
}
