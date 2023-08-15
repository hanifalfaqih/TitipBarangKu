package id.allana.titipbarangku.data.model.firebasemodel

data class ProductDepositModelFirebase(
    val id: Int,
    val idDeposit: Long,
    val idProduct: Int,
    val quantity: Int,
    val returnQuantity: Int,
    val totalProductSold: String,
    val isExpanded: Boolean
)
//{
//    fun toMap(): Map<String, Any> {
//        return mapOf(
//            "id" to id,
//            "idDeposit" to idDeposit,
//            "idProduct" to idProduct,
//            "quantity" to quantity,
//            "returnQuantity" to returnQuantity,
//            "totalProductSold" to totalProductSold,
//            "isExpanded" to isExpanded
//        )
//    }
//}