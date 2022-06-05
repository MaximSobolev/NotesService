import NoteService.add
import NoteService.clear
import NoteService.createComment
import NoteService.delete
import NoteService.deleteComment
import NoteService.edit
import NoteService.editComment
import NoteService.get
import NoteService.getById
import NoteService.getComments
import NoteService.restoreComment
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteServiceTest {
    @Before
    fun before() {
        clear()
    }
    @Test
    fun addTest() {
        val ownerId = 1
        val title = "Note"
        val text = "Content"
        val privacy = 0
        val commentPrivacy = 0

        val result = add (ownerId, title, text, privacy, commentPrivacy)

        assertEquals(1, result)
    }

    @Test
    fun createCommentTest() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"

        val result = createComment(noteId, ownerId, replyTo, message, guid)

        assertEquals(1, result)
    }

    @Test
    fun deleteWithoutComments() {
        val ownerId = 1
        val title = "Note"
        val text = "Content"
        val privacy = 0
        val commentPrivacy = 0
        val noteId = add (ownerId, title, text, privacy, commentPrivacy)

        val result = delete(noteId)

        assertEquals(1, result)
    }

    @Test
    fun deleteWithComments() {
        val ownerId = 1
        val title = "Note"
        val text = "Content"
        val privacy = 0
        val commentPrivacy = 0
        val noteId = add (ownerId, title, text, privacy, commentPrivacy)
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentIdFirst = createComment(noteId, ownerId, replyTo, message, guid)
        val commentIdSecond = createComment(noteId, ownerId, replyTo, message, guid)
        val sort = 1
        val count = 5

        val listCommentBeforeDelete = getComments(noteId, ownerId, sort, count)

        assertEquals(2, listCommentBeforeDelete.size)

        val result = delete(noteId)
        val listCommentsAfterDelete = getComments(noteId, ownerId, sort, count)

        assertEquals(1, result)
        assertEquals(0, listCommentsAfterDelete.size)

    }

    @Test
    fun deleteCommentTest() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentId = createComment(noteId, ownerId, replyTo, message, guid)

        val result = deleteComment(commentId, ownerId)

        assertEquals(1, result)
    }

    @Test(expected = CommentHasBeenDeletedException::class)
    fun deleteCommentWithException() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentId = createComment(noteId, ownerId, replyTo, message, guid)

        deleteComment(commentId, ownerId)
        deleteComment(commentId, ownerId)
    }

    @Test
    fun editTest() {
        val ownerId = 1
        val title = "Note"
        val text = "Content"
        val privacy = 0
        val commentPrivacy = 0
        val noteId = add (ownerId, title, text, privacy, commentPrivacy)
        val titleEdit = "NoteEdit"
        val textEdit = "Content Edit"
        val privacyEdit = 1
        val commentPrivacyEdit = 1

        val result = edit(noteId, titleEdit, textEdit, privacyEdit, commentPrivacyEdit)

        assertEquals(1, result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun editTestWithException() {
        val noteId = 1
        val titleEdit = "NoteEdit"
        val textEdit = "Content Edit"
        val privacyEdit = 1
        val commentPrivacyEdit = 1

        edit(noteId, titleEdit, textEdit, privacyEdit, commentPrivacyEdit)
    }

    @Test
    fun editCommentTest() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentId = createComment(noteId, ownerId, replyTo, message, guid)
        val messageEdit = "content Edit"

        val result = editComment(commentId, ownerId, messageEdit)

        assertEquals(1, result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun editCommentWithException() {
        val commentId = 1
        val ownerId = 1
        val messageEdit = "Edit content"

        editComment(commentId, ownerId, messageEdit)
    }

    @Test
    fun getTestFoundNote() {
        val ownerId = 1
        val titleFirst = "First note"
        val textFirst = "Content"
        val privacyFirst = 0
        val commentPrivacyFirst = 0
        val noteIdFirst = add (ownerId, titleFirst, textFirst, privacyFirst, commentPrivacyFirst)
        val titleSecond = "Second note"
        val textSecond = "Content"
        val privacySecond = 1
        val commentPrivacySecond = 1
        val noteIdSecond = add (ownerId, titleSecond, textSecond, privacySecond, commentPrivacySecond)
        val noteIds = "$noteIdFirst, $noteIdSecond"
        val count = 3
        val sort = 0

        val result = get(noteIds, ownerId, count, sort)

        assertEquals(2, result.size)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getTestWithException(){
        val noteIds = "1, 2"
        val userId = 1
        val count = 10
        val sort = 1

        get(noteIds, userId, count, sort)
    }

    @Test
    fun getByIdFoundNote() {
    val ownerId = 1
    val title = "Note"
    val text = "Content"
    val privacy = 0
    val commentPrivacy = 0
    val noteId = add (ownerId, title, text, privacy, commentPrivacy)
    val time = LocalDateTime.now()
    val formatter = DateTimeFormatter.BASIC_ISO_DATE
    val date = (time.format(formatter)).toInt()
    val noteExample = Note(noteId, ownerId,title, text, date, "url", privacy.toString(), commentPrivacy)
    val result = getById(noteId, ownerId)

    assertEquals(noteExample, result)
    }

    @Test
    fun getByIdNotFoundNote() {
        val ownerId = 1
        val noteId = 1

        val result = getById(noteId, ownerId)

        assertNull(result)
    }

    @Test
    fun getCommentTestWithCount() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentIdFirst = createComment(noteId, ownerId, replyTo, message, guid)
        val commentIdSecond = createComment(noteId, ownerId, replyTo, message, guid)
        val sort = 0
        val count = 1

        val result = getComments(noteId, ownerId, sort, count)

        assertEquals(commentIdFirst, result.get(0).commentId)
        assertEquals(count, result.size)
    }

    @Test
    fun getCommentTest() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentIdFirst = createComment(noteId, ownerId, replyTo, message, guid)
        val commentIdSecond = createComment(noteId, ownerId, replyTo, message, guid)
        val sort = 0
        val count = 3

        val result = getComments(noteId, ownerId, sort, count)

        assertEquals(commentIdFirst, result.get(0).commentId)
        assertEquals(commentIdSecond, result.get(1).commentId)
    }

    @Test
    fun restoreCommentTest() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentId = createComment(noteId, ownerId, replyTo, message, guid)

        deleteComment(commentId, ownerId)

        val result = restoreComment(commentId, ownerId)

        assertEquals(1, result)
    }

    @Test(expected = CommentNotRemovedException::class)
    fun restoreCommentWithException() {
        val noteId = 1
        val ownerId = 1
        val replyTo = 1
        val message = "content"
        val guid = "guid"
        val commentId = createComment(noteId, ownerId, replyTo, message, guid)

        restoreComment(commentId, ownerId)
    }
}