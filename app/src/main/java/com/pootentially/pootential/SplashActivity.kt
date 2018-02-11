package com.pootentially.pootential

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.mapboxsdk.Mapbox
import com.pootentially.pootential.viewModels.RestroomViewModel

/**
 * Created by nick on 2/11/18.
 */
class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("secret keys").document("mapbox").get().addOnCompleteListener {
            if(it.isSuccessful) {
                val intent: Intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("mapboxKey", it.result.get("key").toString())
                startActivity(intent)
                this.finish()
            }
        }

    }
}