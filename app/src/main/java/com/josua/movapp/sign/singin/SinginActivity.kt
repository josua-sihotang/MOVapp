package com.josua.movapp.sign.singin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.josua.movapp.home.HomeActivity
import com.josua.movapp.R
import com.josua.movapp.sign.signup.SignUpActivity
import com.josua.movapp.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in.*


class SinginActivity : AppCompatActivity() {

    lateinit var  iUsername:String
    lateinit var  iPassword:String

    lateinit var mDatabase : DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance("https://movapp-5f54f-default-rtdb.firebaseio.com/").getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")){
            finishAffinity()

            var goHome = Intent(this@SinginActivity, HomeActivity::class.java)
            startActivity(goHome)
        }
        btn_home.setOnClickListener {
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if(iUsername.equals("")) {
                et_username.error = "Silahkan tulis username Anda"
                et_username.requestFocus()
            } else if(iPassword.equals("")) {
                et_password.error = "Silahkan tulis password Anda"
                et_password.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        btn_daftar.setOnClickListener {
            var goSingup = Intent(this@SinginActivity, SignUpActivity::class.java)
            startActivity(goSingup)
        }

    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SinginActivity, databaseError.message,
                    Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var user = dataSnapshot.getValue(User::class.java)
                if(user == null) {
                    Toast.makeText(this@SinginActivity, "User tidak ditemukan",
                        Toast.LENGTH_LONG).show()
                } else {

                    if(user.password.equals(iPassword)) {

                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("saldo", user.saldo.toString())

                        preferences.setValues("status", "1")

                        var intent = Intent(this@SinginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this@SinginActivity, "Password anda salah",
                            Toast.LENGTH_LONG).show()
                    }

                }

            }
        })
    }

}
