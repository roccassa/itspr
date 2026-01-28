package com.itspr.micro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.itspr.micro.screens.UserProfile
import com.google.firebase.firestore.SetOptions
import android.net.Uri
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class AuthViewModel : ViewModel(){

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance() //nicializar Firestore
    private val storage : FirebaseStorage = FirebaseStorage.getInstance() // Inicializar Storage

    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> = _authState

    // LiveData para observar el perfil en la UI
    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> = _userProfile

    init {
        checkAuthState()
    }

    //CHECAR EL ESTADO DE USUARIO: autenticado o no
    fun checkAuthState(){
        if (auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            resetAuthState()
            _authState.value = AuthState.Error("El correo y la contraseña no pueden estar vacíos")
            return
        }
        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    // Recargar y verificar el estado del email
                    user?.reload()?.addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful) {
                            if (user.isEmailVerified) {
                                // El usuario está verificado. Le permitimos el acceso.
                                _authState.value = AuthState.Authenticated
                            } else {
                                // El usuario NO está verificado. Bloquear acceso.
                                auth.signOut()
                                _authState.value = AuthState.Error("Por favor, verifica tu email para iniciar sesión.")
                            }
                        } else {
                            // Error al recargar la información del usuario
                            auth.signOut()
                            _authState.value = AuthState.Error("Error al verificar el estado del usuario.")
                        }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Algo salió mal")
                }
            }
    }


    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("El correo y la contraseña no pueden estar vacíos")
            resetAuthState()
            return
        }
        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Obtener el usuario recién creado
                    val user = auth.currentUser

                    // Enviar el correo de verificación
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                // Notificar que el email ha sido enviado y desautenticar
                                auth.signOut() // Cierra sesión para forzar la verificación al iniciar
                                _authState.value = AuthState.VerificationSent // Nuevo estado
                            } else {
                                // Si falla el envío de email, tratamos como error de registro
                                resetAuthState()
                                _authState.value = AuthState.Error("Registro exitoso, pero falló el envío del email de verificación.")
                            }
                        }

                } else {
                    resetAuthState()
                    _authState.value = AuthState.Error(task.exception?.message ?: "Algo salió mal")
                }
            }
    }


    fun sendPasswordReset(email: String) {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("El correo electrónico no puede estar vacío.")
            resetAuthState()
            return
        }

        _authState.value = AuthState.Loading

        // Llama al método de Firebase para enviar el email de restablecimiento
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Notifica el éxito usando un nuevo estado
                    _authState.value = AuthState.PasswordResetSent
                } else {
                    // Notifica el error (ej: el email no está registrado)
                    resetAuthState()
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error al enviar el correo de restablecimiento.")
                }
            }
    }


    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetAuthState() {
        _authState.value = AuthState.Unauthenticated
    }


    //USERPROFILE
    fun fetchUserProfile() {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            _userProfile.value = null
            return
        }

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Mapea el documento a nuestro Data Class UserProfile
                    _userProfile.value = document.toObject(UserProfile::class.java)
                } else {
                    // Si el documento no existe (usuario nuevo sin perfil creado)
                    _userProfile.value = UserProfile(userId = userId)
                }
            }
            .addOnFailureListener {
                // Manejar error de lectura
                _userProfile.value = null
            }
    }

    fun saveUserProfile(profile: UserProfile, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // Usamos el UID de Firebase Auth como ID del documento en Firestore
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            onError("Usuario no autenticado.")
            return
        }

        // Usamos SetOptions.merge() para actualizar solo los campos provistos
        db.collection("users").document(userId)
            .set(profile, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess()
                // Opcional: Recargar el perfil para que la UI se actualice
                fetchUserProfile()
            }
            .addOnFailureListener { e ->
                onError("Error al guardar los datos: ${e.localizedMessage}")
            }
    }

    fun convertImageToBase64(contentResolver: ContentResolver, imageUri: Uri): String? {
        try {
            // Obtener el Bitmap (imagen)
            val inputStream = contentResolver.openInputStream(imageUri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // Comprimir el Bitmap y codificarlo
            val byteArrayOutputStream = ByteArrayOutputStream()

            // Compresión al 50% para reducir el tamaño y caber en el límite de 1MB de Firestore
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)

            val byteArray = byteArrayOutputStream.toByteArray()

            // Codificar a Base64
            return Base64.encodeToString(byteArray, Base64.NO_WRAP)

        } catch (e: Exception) {
            return null
        }
    }

    fun saveProfileWithBase64(
        contentResolver: ContentResolver,
        profile: UserProfile,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            onError("Usuario no autenticado.")
            return
        }

        var finalProfile = profile

        // Solo convierte si hay una URI local nueva (no es la URL de Firebase)
        if (imageUri != null) {
            val base64String = convertImageToBase64(contentResolver, imageUri)
            if (base64String != null) {
                // Actualiza la Base64 en el perfil
                finalProfile = finalProfile.copy(photoBase64 = base64String)
            } else {
                onError("Error al codificar la imagen. La imagen podría ser demasiado grande.")
                return
            }
        }

        // Guarda el perfil (que ahora tiene el campo photoBase64) en Firestore
        db.collection("users").document(userId)
            .set(finalProfile, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess()
                fetchUserProfile() // Recargar para actualizar la UI
            }
            .addOnFailureListener { e ->
                onError("Error al guardar los datos en Firestore: ${e.localizedMessage}")
            }
    }

}//class

// En AuthViewModel.kt (al final)

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object VerificationSent : AuthState()
    object PasswordResetSent : AuthState()
    data class Error (val mensaje : String) : AuthState()
}