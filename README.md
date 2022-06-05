# Мини-проект - NoteService

1. Были удалены поля privacyView и privacyComment из методов add и edit

```kotlin
fun add (
        ownerId: Int,
        title : String,
        text : String,
        privacy : Int,
        commentPrivacy : Int,
    ) : Int {}

fun edit (
    noteId : Int,
    title : String,
    text : String,
    privacy : Int,
    commentPrivacy : Int,
) : Int {}
```

2. Было удалено поле offset из методов get и getComments

```kotlin
fun get (
    noteIds: String,
    userId: Int,
    count : Int,
    sort : Int
) : List<Note> {}

fun getComments (
    noteId: Int,
    ownerId: Int,
    sort: Int,
    count: Int
) : List<Comment> {}
```

3. Было удалено поле needWiki из метода getById

```kotlin
fun getById (
    noteId : Int,
    ownerId: Int
) : Note?  {}
```