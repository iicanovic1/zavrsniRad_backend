package ba.unsa.etf.zavrsniRad.data

import ba.unsa.etf.zavrsniRad.data.collections.Note
import ba.unsa.etf.zavrsniRad.data.collections.User
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine // objašnjava da bazi pristupamo sa korutinama
private val database = client.getDatabase("DnevnikDatabase") // kreira bazu
private val users = database.getCollection<User>() // ako nema kolekcije korisnika ono je kreira
private val notes = database.getCollection<Note>()

suspend fun registerUser(user: User) : Boolean{ // ubacuje novog korisnika u bazu
    return users.insertOne(user).wasAcknowledged() // ubacuje korisnika i vraća da li je uspješno
}

suspend fun checkIfUserExists(email : String) :Boolean{ // testira potojanje korisnika u bazi
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, passwordToCheck : String ) : Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password?: return false
    return  actualPassword == passwordToCheck
}

suspend fun getNotesForUser(email : String) : List<Note> {
    return notes.find(Note::owners contains email).toList()
}

suspend fun saveNote (note : Note) : Boolean{
    val noteExists = notes.findOneById(note.id) != null
    return if(noteExists){
        notes.updateOneById(note.id, note).wasAcknowledged()
    }else{
        notes.insertOne(note).wasAcknowledged()
    }
}

suspend fun deleteNoteForUser (email: String, noteID : String) : Boolean{
    val note = notes.findOne(Note::id eq noteID, Note::owners contains  email)
    note?.let { note ->
        if(note.owners.size > 1){
            // ima više vlasnika samo brišemo email iz Liste vlasnika
            val newOwners = note.owners - email
            val updateResult = notes.updateOne(Note::id eq note.id, setValue(Note::owners, newOwners))
            return updateResult.wasAcknowledged()
        }
        return notes.deleteOneById(note.id).wasAcknowledged()
    }?: return false
}

suspend fun isOwnerOfNote(id : String, owner: String): Boolean{
    val note = notes.findOneById(id)?: return false
    return owner in note.owners
}

suspend fun addOwnerToNote (id: String, owner: String): Boolean{
    val  owners = notes.findOneById(id)?.owners?: return false
    return notes.updateOneById(id, setValue(Note::owners, owners+owner)).wasAcknowledged()
}