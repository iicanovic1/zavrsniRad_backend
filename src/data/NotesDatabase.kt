package ba.unsa.etf.zavrsniRad.data

import ba.unsa.etf.zavrsniRad.data.collections.Note
import ba.unsa.etf.zavrsniRad.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

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