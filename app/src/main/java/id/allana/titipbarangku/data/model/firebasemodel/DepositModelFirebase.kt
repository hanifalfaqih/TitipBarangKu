package id.allana.titipbarangku.data.model.firebasemodel

data class DepositModelFirebase(
    val id: Long = 0L,
    val idStore: Int = 0,
    val startDateDeposit: String = "",
    val finishDateDeposit: String = "",
    val status: String = ""
)
{
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "idStore" to idStore,
            "startDateDeposit" to startDateDeposit,
            "finishDateDeposit" to finishDateDeposit,
            "status" to status
        )
    }
}
