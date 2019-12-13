package com.example.companionapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.companionapp.R
import com.example.companionapp.models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sign_up_button.setOnClickListener {
            //Leemos los textos de los bloques de register
            val username = usrnameProfileEditText.text?.toString().orEmpty()
            val email = emailProfileEditText.text?.toString().orEmpty()
            val password = passwordProfileEditText.text?.toString().orEmpty()

            if(username.trim().isEmpty()){
                Toast.makeText(this, "User not valid", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Email not valid", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(password.isBlank() || !passwordValid(password)){
                Toast.makeText(this, "Password not valid", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener { task ->
                Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show()
                val userData = UserData(
                    uid = task.user?.uid ?: "",
                    username = username,
                    mail = email,
                    avatarUrl = null
                )
                FirebaseFirestore.getInstance().collection("users").document(task.user?.uid ?: "").set(userData).addOnSuccessListener {
                        Toast.makeText(this, "You created a new user", Toast.LENGTH_LONG).show()
                        finish()
                }.addOnFailureListener{
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun passwordValid(password: String): Boolean{
        if(password.length < 4) return false

        var digit = 0
        var letra = 0

        for(letter in password){
            if(letter.isDigit()) digit++
            else if(letter.isLetter()) letra++
        }

        if(digit == password.length) return false
        if(letra == password.length) return false

        return true
    }
}
