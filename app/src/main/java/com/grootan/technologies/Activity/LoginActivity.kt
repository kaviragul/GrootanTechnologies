package com.grootan.technologies.Activity

import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.grootan.technologies.Utils.SessionManagerSP
import com.grootan.technologies.AppViewModel
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.grootan.technologies.R
import android.os.Build
import android.view.WindowManager
import com.grootan.technologies.BroadCast.ConnectivityReceiver
import com.grootan.technologies.Utils.CustomToast
import android.content.Intent
import android.view.View
import com.grootan.technologies.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    var lin_login: LinearLayout? = null
    var progressBar: ProgressBar? = null
    var auth: FirebaseAuth? = null
    var sessionManagerSP: SessionManagerSP? = null
    var appViewModel: AppViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = AppViewModel(this@LoginActivity)
        val activityLoginBinding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        activityLoginBinding.viewModel = appViewModel
        activityLoginBinding.executePendingBindings()
        if (Build.VERSION.SDK_INT >= 19) {
            val window = window
            getWindow().decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
            window.statusBarColor = this.resources.getColor(R.color.white)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        lin_login = findViewById(R.id.lin_login)
        progressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()
        sessionManagerSP = SessionManagerSP(this@LoginActivity)
    }

    fun checkSignInLogin() {
        progressBar!!.visibility = View.VISIBLE
        lin_login!!.visibility = View.GONE
        if (appViewModel!!.checkSignIn()) {
            // For Sign Up
            val isConnected: Boolean = ConnectivityReceiver.isConnected
            if (isConnected) {
                auth!!.createUserWithEmailAndPassword(
                    appViewModel!!.userEmail.toString(),
                    appViewModel!!.userPassword.toString()
                )
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            CustomToast.getInstance(this@LoginActivity)!!
                                .showSmallCustomToast("Registered Successfully!")
                            sessionManagerSP!!.setlogin("1")
                            val intent = Intent(this@LoginActivity, HomeScreenActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            CustomToast.getInstance(this@LoginActivity)!!
                                .showSmallCustomToast("Something went wrong try again!!")
                        }
                        progressBar!!.visibility = View.GONE
                        lin_login!!.visibility = View.VISIBLE
                    }
            } else {
                CustomToast.getInstance(this@LoginActivity)!!
                    .showSmallCustomToast("Please check your internet connection")
                progressBar!!.visibility = View.GONE
                lin_login!!.visibility = View.VISIBLE
            }
        } else {
            // For Sign IN
            val isConnected: Boolean = ConnectivityReceiver.isConnected
            if (isConnected) {
                auth!!.signInWithEmailAndPassword(
                    appViewModel!!.userEmail.toString(),
                    appViewModel!!.userPassword.toString()
                )
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            CustomToast.getInstance(this@LoginActivity)!!
                                .showSmallCustomToast("Logged Successfully!")
                            sessionManagerSP!!.setlogin("1")
                            val intent = Intent(this@LoginActivity, HomeScreenActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            CustomToast.getInstance(this@LoginActivity)!!
                                .showSmallCustomToast("Invalid User Name & Password")
                        }
                        progressBar!!.visibility = View.GONE
                        lin_login!!.visibility = View.VISIBLE
                    }
            } else {
                progressBar!!.visibility = View.GONE
                lin_login!!.visibility = View.VISIBLE
                CustomToast.getInstance(this@LoginActivity)!!
                    .showSmallCustomToast("Please check your internet connection")
            }
        }
    }
}