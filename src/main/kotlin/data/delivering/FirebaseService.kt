package data.delivering

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import models.test.Test
import models.test.questions.Answer
import java.io.FileInputStream


class FirebaseService : DataDeliveringService {
    init {
        val serviceAccount = FileInputStream("firebase_key/testdiplomadevelop-firebase-adminsdk-qnuio-485a92411c.json")

        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://testdiplomadevelop-default-rtdb.firebaseio.com")
            .build()

        val firebaseApp = FirebaseApp.initializeApp(options, FirebaseService::class.qualifiedName)
        println("Firebase: ${firebaseApp.name} initialized")


        val firebaseAuth = FirebaseAuth.getInstance(firebaseApp)
        FirebaseDatabase.getInstance(firebaseApp)
    }

    override fun addTest(test: Test) {
        FirebaseDatabase.getInstance(FirebaseApp.getInstance(FirebaseService::class.qualifiedName))
            .reference.child(TESTS_REPOSITORY)
            .push().setValue(test.toMap(), ::onResult)
    }

    override val tests: List<Test>
        get() = TODO("Not yet implemented")

    override fun addAnswer(answer: Answer<*>) {
        TODO("Not yet implemented")
    }

    override val answers: List<Answer<*>>
        get() = TODO("Not yet implemented")

    private fun onResult(error: DatabaseError?, ref: DatabaseReference?){
        error?.let {
            println(error.message)
        }
    }

    companion object {
        private const val TESTS_REPOSITORY = "tests_repo"
        private const val ANSWERS_REPOSITORY = "answers_repo"
    }
}