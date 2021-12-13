package pl.sokols.warehouseassistant.data.models

data class Inventory(
    var timestampId: String,
    var missingItems: List<Item>? = null,
    var extraItems: List<Item>? = null,
    var confirmedItems: List<Item>? = null
)