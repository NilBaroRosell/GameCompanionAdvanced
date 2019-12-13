
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.companionapp.activities.LogInActivity
import com.example.companionapp.R
import com.example.companionapp.activities.SignUpActivity
import com.example.companionapp.models.UserData
import com.example.companionapp.utils.COLLECTION_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if(userId != null)
        {
            FirebaseFirestore.getInstance()
                    .collection(COLLECTION_USERS)
                    .document(userId?:"")
                    .get()
                    .addOnSuccessListener {documentSnapshot ->
                        val username = documentSnapshot.toObject(UserData::class.java)
                        if(username?.avatarUrl != null)
                        {
                            Glide
                                    .with(this)
                                    .load(username?.avatarUrl)
                                    .into(profileImg)
                        }
                    }
        }

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoutProfileButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            DisabledProfileView()
        }
        profileImg.setOnClickListener {
            takePicture()
        }

    }

    override fun onResume() {
        super.onResume()
        InitProfileFragment()
    }

    private fun InitProfileFragment() {
        if(FirebaseAuth.getInstance().currentUser == null){
            DisabledProfileView()
            //Vamos a la activity register
            signUp.setOnClickListener {
                startActivity(Intent(requireContext(), SignUpActivity::class.java))
            }

            logIn.setOnClickListener {
                startActivity(Intent(requireContext(), LogInActivity::class.java))
            }
        }
        else{
            EnableProfileView()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .document(userUid)
                .get()
                .addOnSuccessListener {documentSnapshot ->
                    val username = documentSnapshot.toObject(UserData::class.java)
                    usernameProfileTextView.text = username?.mail
                }
                .addOnFailureListener {
                }
        }
    }

    //
    private fun EnableProfileView() {
        //Escondemos el boton de register si hay usuario logeado
        signUp.visibility = View.GONE
        //Mostramos elementos del profile si hay usuario logeado
        logoutProfileButton.visibility = View.VISIBLE
        profileImg.visibility = View.VISIBLE
        usernameProfileTextView.visibility = View.VISIBLE
    }

    private fun DisabledProfileView(){
        //Escondemos todos los elementos de la pantalla profile siempre que no hay usuario logeado
        logoutProfileButton.visibility = View.GONE
        profileImg.visibility = View.GONE
        usernameProfileTextView.visibility = View.GONE
        //Si no tenemos usuario logeado en la app se esconde todo y mostramos el botón register
        signUp.visibility = View.VISIBLE
    }

    private fun takePicture()
    {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.resolveActivity(requireActivity().packageManager)
        startActivityForResult(cameraIntent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 10 && resultCode == Activity.RESULT_OK)
        {
            val image = data?.extras?.get("data") as? Bitmap
            image?.let {
                profileImg.setImageBitmap(it)

                //save image to cloud
                saveimageToCloud(it)
            }
        }
    }

    private fun saveimageToCloud(bitmap: Bitmap)
    {
        //convert Bitmap to bytes
        val imageBytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageBytes)
        // create references
        val folderReference = FirebaseStorage.getInstance().reference.child("public/images/avatars/")
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?:""
        val timestamp = System.currentTimeMillis()
        val imageReference = folderReference.child("avatar_${userId}_${timestamp}.jpg")
        imageReference.putBytes(imageBytes.toByteArray())
            .addOnSuccessListener {
                Log.i("HomeFragment", "Success uploading image")
                imageReference.downloadUrl
                    .addOnSuccessListener {uri ->
                        val avatarUrl = uri.toString()
                        FirebaseFirestore.getInstance()
                            .collection(COLLECTION_USERS)
                            .document(userId)
                            .update("avatarUrl",avatarUrl)
                            .addOnSuccessListener {

                            }
                            .addOnFailureListener {

                            }
                    }
            }
            .addOnFailureListener {
                Log.w("HomeFragment", "Error uploading image")
                it.printStackTrace()
            }
    }

    /*TODO: crear una funcion que muestre al usuario: es decir las letras e imagenes de las que hablamos en el TODO de mas
    arriba*/

}
