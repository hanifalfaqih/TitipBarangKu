package id.allana.titipbarangku.data.model.firebasemodel

data class ProductDepositModelFirebase(
    val id: Int = 0,
    val idDeposit: Long = 0L,
    val idProduct: Int = 0,
    val quantity: Int = 0,
    val returnQuantity: Int = 0,
    val totalProductSold: String = "",
    val isExpanded: Boolean = false
)
{
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "idDeposit" to idDeposit,
            "idProduct" to idProduct,
            "quantity" to quantity,
            "returnQuantity" to returnQuantity,
            "totalProductSold" to totalProductSold,
            "isExpanded" to isExpanded
        )
    }
}