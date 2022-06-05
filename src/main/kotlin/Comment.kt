data class Comment(
    val noteId : Int,
    val commentId : Int,
    val ownerId : Int,
    val replyTo : Int,
    val message : String,
    val date : Int,
    val guid : String,
    val delete : Boolean = false
)