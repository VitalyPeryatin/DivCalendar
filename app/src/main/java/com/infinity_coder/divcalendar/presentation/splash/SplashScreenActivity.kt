package com.infinity_coder.divcalendar.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.infinity_coder.divcalendar.presentation.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAccess()
    }

    private fun getAccess() {
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .get()
            .addOnCompleteListener {
                if (!it.isSuccessful || it.result == null) return@addOnCompleteListener

                if (it.result!!.isHaveAccess()) {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
    }

    private fun QuerySnapshot.isHaveAccess(): Boolean {
        return (documents.find { it.id == DOC_NAME }?.data?.get(FIELD_NAME) as? Boolean) ?: false
    }

    companion object {
        private const val COLLECTION_NAME = "test_application"
        private const val DOC_NAME = "access"
        private const val FIELD_NAME = "haveAccess"
    }
}