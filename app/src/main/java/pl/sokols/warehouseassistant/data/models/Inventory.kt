package pl.sokols.warehouseassistant.data.models

data class Inventory(
    var timestampId: String,
    var missingItems: List<CountedItem>? = null,
    var extraItems: List<CountedItem>? = null,
    var confirmedItems: List<CountedItem>? = null
)