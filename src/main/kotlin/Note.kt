data class Note (
    val id : Int,
    val ownerId : Int,
    val title : String,
    val text : String,
    val date : Int,
    val viewUrl : String,
    val privacyView : String,
    val canComment : Int,
    val comments : Int = 0,
    val readComments : Int = 0,
    val delete : Boolean = false
        )