package com.example.telaslivros

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class EditProfileActivity : BaseActivity() {
    override fun getBottomNavItemId() = 0;

    lateinit var userName : TextInputEditText
    lateinit var phone : TextInputEditText
    lateinit var cpf : TextInputEditText
    lateinit var cep : TextInputEditText
    lateinit var saveButton: Button
    lateinit var profileImage: ImageView
    lateinit var editIcon: ImageView
    private var selectedImageBytes: ByteArray? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {

            val bitmap = uriToBitmap(uri)
            profileImage.setImageBitmap(bitmap) // Mostra na tela
            selectedImageBytes = bitmapToBytes(bitmap) // Converte para bytes para salvar depois
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_perfil)
        userName = findViewById(R.id.etNome)
        phone = findViewById(R.id.etTelefone)
        cpf = findViewById(R.id.etCpf)
        cep = findViewById(R.id.etCep)
        saveButton = findViewById(R.id.ButtonSave)
        profileImage = findViewById(R.id.imageViewProfile)
        editIcon = findViewById(R.id.imageViewCameraIcon)

        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        val sessionPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sessionPrefs.getInt("USER_ID_INT", 0)
        var userEmail = ""
        lifecycleScope.launch(Dispatchers.IO) {
            val user = DatabaseHelper.getUser(userId)

            withContext(Dispatchers.Main) {
                if (user != null) {
                    userEmail = user.email

                    userName.setText(user.nomeCompleto)
                    phone.setText(user.telefone)
                    cpf.setText(user.cpf)
                    cep.setText(user.cep)
                    if (user.fotoPerfil != null) {

                        selectedImageBytes = user.fotoPerfil

                        val bitmap = BitmapFactory.decodeByteArray(user.fotoPerfil, 0, user.fotoPerfil.size)
                        profileImage.setImageBitmap(bitmap)
                    }

                }
            }
        }


        saveButton.setOnClickListener {
            val userToUpdate = User(

                nomeCompleto = userName.text.toString(),
                email = userEmail,
                senhaHash = "",
                telefone = phone.text.toString(),
                cpf = cpf.text.toString(),
                cep = cep.text.toString(),
                fotoPerfil = selectedImageBytes
            )
            lifecycleScope.launch(Dispatchers.IO) {
                val success = DatabaseHelper.changeProfile(userToUpdate)

                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(
                            applicationContext,
                            "Perfil atualizado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent =
                            Intent(this@EditProfileActivity, PerfilActivityUser::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Erro ao atualizar perfil.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
        editIcon.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = android.graphics.ImageDecoder.createSource(contentResolver, uri)
            android.graphics.ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }


    private fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        return stream.toByteArray()
    }
}