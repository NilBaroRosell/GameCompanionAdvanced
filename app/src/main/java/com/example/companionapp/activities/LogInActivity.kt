package com.example.companionapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.companionapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        log_in_button.setOnClickListener {
            //Leemos los textos de los bloques de register
            val email = logInEmailProfileEditText.text?.toString().orEmpty()
            val password = logInPasswordProfileEditText.text?.toString().orEmpty()

            if(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(this, "Email not valid", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(password.isBlank() || !passwordValid(password)){
                Toast.makeText(this, "Password not valid", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener { task ->
                Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show()
                finish()
            }.addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
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
