package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.Status
import com.vincentzhangz.maverickcount.models.User
import com.vincentzhangz.maverickcount.models.UserData
import com.vincentzhangz.maverickcount.utilities.OtherUtil
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN: Int = 0
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Login"
        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener {
            var userEmail = email.text.toString()
            var userPassword = password.text.toString()
            if (userEmail == "") {
                Toast.makeText(applicationContext, "Email must be filled !", Toast.LENGTH_SHORT)
                    .show()
            } else if (userPassword == "") {
                Toast.makeText(applicationContext, "Password must be filled !", Toast.LENGTH_SHORT)
                    .show()
            } else {
                firebaseAuth()
            }
        }

        goto_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        sign_in_button.setSize(SignInButton.SIZE_WIDE)
        sign_in_button.setOnClickListener {
            signIn()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            val account = completedTask!!.getResult(ApiException::class.java)
//            toast(this, account?.id.toString())
            toast(this, "Login success.")
            account!!.id?.let {
                val database = FirebaseDatabase.getInstance()
                account.id?.let { it1 ->
                    database.getReference("users").child(it1)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(ds: DataSnapshot) {
                                if (ds.getValue(UserData::class.java) == null) {
                                    toast(applicationContext, "Create New")
                                    database.getReference("users").child(it1).setValue(
                                        account.id?.let { it2 ->
                                            account.email?.let { it3 ->
                                                User(
                                                    it2,
                                                    it3.split("@")[0],
                                                    0,
                                                    Status(0, 0, 0),
                                                    ArrayList()
                                                )
                                            }
                                        }
                                    )
                                }
                            }

                        })
                }
            }
            OtherUtil.setUserToken(account?.id.toString())
            val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("userId", account?.id).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } catch (e: ApiException) {
            toast(this, "Authentication failed.")
        }
    }

    private fun firebaseAuth() {
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
//                    toast(this, user?.uid.toString())
                    toast(this, "Login success.")
                    OtherUtil.setUserToken(user?.uid.toString())
                    val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("userId", user?.uid).apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    toast(this, "Authentication failed.")
                }
            }
    }
}
