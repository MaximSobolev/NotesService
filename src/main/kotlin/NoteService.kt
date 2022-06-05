import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object NoteService {
    private var notes = emptyArray<Note>()
    private var comments = emptyArray<Comment>()

    fun add (
        ownerId: Int,
        title : String,
        text : String,
        privacy : Int,
        commentPrivacy : Int,
    ) : Int {
        val time = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val date = (time.format(formatter)).toInt()

        val newNote = Note(notes.size + 1, ownerId, title, text, date, "url", privacy.toString(), commentPrivacy)
        notes += newNote
        return notes.last().id
    }

    fun createComment (
        noteId : Int,
        ownerId : Int,
        replyTo : Int,
        message : String,
        guid : String
    ) : Int {
        val time = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val date = (time.format(formatter)).toInt()

        val newComment = Comment (noteId, comments.size + 1, ownerId, replyTo, message, date, guid)
        comments += newComment
        return comments.last().commentId
    }

    fun delete (
        noteId : Int
    ) : Int {
        for ((index, comment) in comments.withIndex()) {
            if (noteId == comment.noteId) {
                comments[index] = comment.copy(delete = true)
            }
        }
        for ((index, note) in notes.withIndex()) {
            if (noteId == note.id) {
                notes[index] = note.copy(delete = true)
                return 1
            }
        }
        return 0
    }

    fun deleteComment (
        commentId: Int,
        ownerId: Int
    ) : Int {
        for ((index, comment) in comments.withIndex()) {
            if ((commentId == comment.commentId) and (ownerId == comment.ownerId)) {
                if (comment.delete) {
                    throw CommentHasBeenDeletedException("Comment with id = ${comment.commentId} already removed")
                }
                comments[index] = comment.copy(delete = true)
                return 1
            }
        }
        return 0
    }

    fun edit (
        noteId : Int,
        title : String,
        text : String,
        privacy : Int,
        commentPrivacy : Int,
    ) : Int {
        for ((index, note) in notes.withIndex()) {
            if (noteId == note.id) {
                notes[index] = note.copy(
                    title = title,
                    text = text,
                    privacyView = privacy.toString(),
                    canComment = commentPrivacy,
                )
                return 1
            }
        }
        throw NoteNotFoundException("Note with id = $noteId not found")
    }

    fun editComment (
        commentId: Int,
        ownerId: Int,
        message: String
    ) : Int {
        for ((index, comment) in comments.withIndex()) {
            if ((commentId == comment.commentId) and (ownerId == comment.ownerId) and (!comment.delete)) {
                comments[index] = comment.copy(message = message)
                return 1
            }
        }
        throw CommentNotFoundException("Comment with id = $commentId not found")
    }

    fun get (
        noteIds: String,
        userId: Int,
        count : Int,
        sort : Int
    ) : List<Note> {
        val arrayNoteIds = noteIds.split(", ")
        var arrayNoteIdsInt = emptyArray<Int>()
        var noteList = ArrayList<Note>()
        var amountNote = 0

        for (noteId in arrayNoteIds) {
            arrayNoteIdsInt += noteId.toInt()
        }

        for (note in notes) {
            for (noteId in arrayNoteIdsInt) {
                if ((noteId == note.id) and (userId == note.ownerId) and (count > amountNote)) {
                    noteList.add(note)
                    amountNote =+ 1
                }
            }
        }

        if (noteList.size  == 0) {
            throw NoteNotFoundException("Notes with id = $noteIds not found")
        }

        when {
            sort == 0 -> noteList.sortByDescending { it.date }
            sort == 1 -> noteList.sortBy { it.date }
        }

        return noteList
    }

    fun getById (
        noteId : Int,
        ownerId: Int
    ) : Note?  {
        for ((index, note) in notes.withIndex()) {
            if ((noteId == note.id) and (ownerId == note.ownerId)) {
                return note
            }
        }
        return null
    }

    fun getComments (
        noteId: Int,
        ownerId: Int,
        sort: Int,
        count: Int
    ) : List<Comment> {
        val commentsList = ArrayList<Comment>()
        var amountComments = 0

        for (comment in comments) {
            if (amountComments == count) {
                break
            }
            if ((noteId == comment.noteId) and (ownerId == comment.ownerId) and (!comment.delete)) {
                commentsList.add(comment)
                amountComments =+ 1
            }
        }
        when {
            sort == 0 -> commentsList.sortByDescending {it.date}
            sort == 1 -> commentsList.sortBy {it.date}
        }
        return commentsList
    }

    fun restoreComment (
        commentId: Int,
        ownerId: Int
    ) : Int {
        for ((index, comment) in comments.withIndex()) {
            if ((commentId == comment.commentId) and (ownerId == comment.ownerId)) {
                if (!comment.delete) {
                    throw CommentNotRemovedException("Ñomment with id = ${comment.commentId} not removed")
                }
                comments[index] = comment.copy(delete = false)
                return 1
            }
        }
        return 0
    }

    fun clear() {
        notes = emptyArray()
        comments = emptyArray()
    }
}