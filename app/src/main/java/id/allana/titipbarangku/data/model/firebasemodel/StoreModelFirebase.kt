package id.allana.titipbarangku.data.model.firebasemodel

data class StoreModelFirebase(
    val id: Int,
    val name: String,
    val address: String,
    val ownerName: String,
    val ownerPhoneNumber: String
)
//{
//    fun toMap(): Map<String, Any> {
//        return mapOf(
//            "id" to id,
//            "name" to name,
//            "address" to address,
//            "ownerName" to ownerName,
//            "ownerPhoneNumber" to ownerPhoneNumber
//        )
//    }
//}
