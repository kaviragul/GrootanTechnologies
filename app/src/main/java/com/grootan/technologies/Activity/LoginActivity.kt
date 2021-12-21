package com.grootan.technologies.Activity

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.grootan.technologies.Utils.SessionManagerSP
import android.os.Bundle
import com.grootan.technologies.R
import android.os.Build
import android.view.WindowManager
import com.grootan.technologies.BroadCast.ConnectivityReceiver
import com.grootan.technologies.Utils.CustomToast
import android.content.Intent
import android.text.method.PasswordTransformationMethod
import com.bumptech.glide.Glide
import android.text.method.HideReturnsTransformationMethod
import android.view.View
import android.widget.*
import android.util.Patterns
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    var edt_email: EditText? = null
    var edt_pass: EditText? = null
    var lin_eye: LinearLayout? = null
    var lin_login: LinearLayout? = null
    var img_eye: ImageView? = null
    var txt_signIn: TextView? = null
    var txt_sign_up: TextView? = null
    var checkSignIn = false
    var progressBar: ProgressBar? = null
    var auth: FirebaseAuth? = null
    var sessionManagerSP: SessionManagerSP? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
        edt_email = findViewById(R.id.edt_email)
        edt_pass = findViewById(R.id.edt_pass)
        lin_eye = findViewById(R.id.lin_eye)
        lin_login = findViewById(R.id.lin_login)
        txt_signIn = findViewById(R.id.txt_signIn)
        txt_sign_up = findViewById(R.id.txt_sign_up)
        progressBar = findViewById(R.id.progressBar)
        img_eye = findViewById(R.id.img_eye)
        auth = FirebaseAuth.getInstance()
        sessionManagerSP = SessionManagerSP(this@LoginActivity)
        txt_sign_up!!.setOnClickListener(View.OnClickListener {
            checkSignIn = !checkSignIn
            if (checkSignIn) {
                txt_signIn!!.setText("SIGN UP")
                txt_sign_up!!.setText("SIGN IN")
            } else {
                txt_signIn!!.setText("SIGN IN")
                txt_sign_up!!.setText("SIGN UP")
            }
        })
        lin_eye!!.setOnClickListener(View.OnClickListener { view -> showHidePass(view) })
        txt_signIn!!.setOnClickListener(View.OnClickListener {
            if (checkSignIn) {
                // For Sign Up
                if (validateEmail(edt_email) && validatePass(edt_pass)) {
                    progressBar!!.setVisibility(View.VISIBLE)
                    lin_login!!.setVisibility(View.GONE)
                    val isConnected = ConnectivityReceiver.isConnected
                    if (isConnected) {
                        auth!!.createUserWithEmailAndPassword(
                            edt_email!!.getText().toString().trim { it <= ' ' },
                            edt_pass!!.getText().toString().trim { it <= ' ' })
                            .addOnCompleteListener(this@LoginActivity) { task ->
                                if (task.isSuccessful) {
                                    CustomToast.getInstance(this@LoginActivity)
                                        ?.showSmallCustomToast("Registered Successfully!")
                                    sessionManagerSP!!.setlogin("1")
                                    val intent =
                                        Intent(this@LoginActivity, HomeScreenActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                } else {
                                    CustomToast.getInstance(this@LoginActivity)
                                        ?.showSmallCustomToast("Something went wrong try again!!")
                                }
                                progressBar!!.setVisibility(View.GONE)
                                lin_login!!.setVisibility(View.VISIBLE)
                            }
                    } else {
                        CustomToast.getInstance(this@LoginActivity)
                            ?.showSmallCustomToast("Please check your internet connection")
                        progressBar!!.setVisibility(View.GONE)
                        lin_login!!.setVisibility(View.VISIBLE)
                    }
                }
            } else {
                // For Sign Up
                if (validateEmail(edt_email) && validatePass(edt_pass)) {
                    progressBar!!.setVisibility(View.VISIBLE)
                    lin_login!!.setVisibility(View.GONE)
                    val isConnected = ConnectivityReceiver.isConnected
                    if (isConnected) {
                        auth!!.signInWithEmailAndPassword(
                            edt_email!!.getText().toString().trim { it <= ' ' },
                            edt_pass!!.getText().toString().trim { it <= ' ' })
                            .addOnCompleteListener(this@LoginActivity) { task ->
                                if (task.isSuccessful) {
                                    CustomToast.getInstance(this@LoginActivity)
                                        ?.showSmallCustomToast("Logged Successfully!")
                                    sessionManagerSP!!.setlogin("1")
                                    val intent =
                                        Intent(this@LoginActivity, HomeScreenActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                } else {
                                    CustomToast.getInstance(this@LoginActivity)
                                        ?.showSmallCustomToast("Invalid User Name & Password")
                                }
                                progressBar!!.setVisibility(View.GONE)
                                lin_login!!.setVisibility(View.VISIBLE)
                            }
                    } else {
                        progressBar!!.setVisibility(View.GONE)
                        lin_login!!.setVisibility(View.VISIBLE)
                        CustomToast.getInstance(this@LoginActivity)
                            ?.showSmallCustomToast("Please check your internet connection")
                    }
                }
            }
        })
    }

    fun showHidePass(view: View) {
        if (view.id == R.id.lin_eye) {
            if (edt_pass!!.transformationMethod == PasswordTransformationMethod.getInstance()) {
                Glide.with(this@LoginActivity).load(R.drawable.ic_hide_eye).dontTransform().into(
                    img_eye!!
                )
                //Show Password
                edt_pass!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                Glide.with(this@LoginActivity).load(R.drawable.ic_show_eye).dontTransform().into(
                    img_eye!!
                )
                //Hide Password
                edt_pass!!.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun validateEmail(et: EditText?): Boolean {
        // Always assume false until proven otherwise
        var bHasContent = false
        var emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val email = et!!.text.toString()
        if (isValidEmail(email)) {
            // Got content
            bHasContent = true
        } else {
            et.error = "Enter valid Email-Id"
        }
        return bHasContent
    }

    private fun validatePass(et: EditText?): Boolean {
        // Always assume false until proven otherwise
        var bHasContent = false
        if (et!!.text.toString().length > 5) {
            // Got content
            bHasContent = true
        } else {
            et.error = "Password should be at least 6 characters"
        }
        return bHasContent
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}


