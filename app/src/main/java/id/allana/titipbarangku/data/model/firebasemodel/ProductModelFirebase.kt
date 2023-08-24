package id.allana.titipbarangku.data.model.firebasemodel

data class ProductModelFirebase(
    val id: Int = 0,
    val idCategory: Int = 0,
    val name: String = "",
    val price: Int = 0
)
{
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "idCategory" to idCategory,
            "name" to name,
            "price" to price
        )
    }
}
