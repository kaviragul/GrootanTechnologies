package com.grootan.technologies

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.grootan.technologies.Activity.LoginActivity
import com.grootan.technologies.Module.LoginModel
import com.grootan.technologies.Utils.CustomToast

class AppViewModel(var context: Context) : BaseObservable() {
    // creating object of Model class
    private val model: LoginModel

    var checkSignIn = false

    // getter and setter methods
    // for email variable
    @get:Bindable
    var userEmail: String?
        get() = model.email
        set(email) {
            model.email = email
            notifyPropertyChanged(BR.userEmail)
        }

    // getter and setter methods
    // for password variable
    @get:Bindable
    var userPassword: String?
        get() = model.password
        set(password) {
            model.password = password
            notifyPropertyChanged(BR.userPassword)
        }

    @get:Bindable
    var signInTxt: String?
        get() = model.signInTxt
        set(signInTxt) {
            model.signInTxt = signInTxt
            notifyPropertyChanged(BR.signInTxt)
        }

    @get:Bindable
    var signUpTxt: String?
        get() = model.signUpTxt
        set(signUpTxt) {
            model.signUpTxt = signUpTxt
            notifyPropertyChanged(BR.signUpTxt)
        }

    @get:Bindable
    var progressBar: Boolean
        get() = model.progressBar
        set(progressBar) {
            model.progressBar = progressBar
            notifyPropertyChanged(BR.progressBar)
        }

    // actions to be performed
    // when user clicks
    // the LOGIN button
    fun onButtonClicked() {
        if (isValid) {
            (context as LoginActivity).checkSignInLogin()
        } else {
            CustomToast.getInstance(context)!!
                .showSmallCustomToast("Email or Password is not valid")
        }
    }

    fun checkSignIn(): Boolean {
        return checkSignIn
    }

    fun onButtonSignUpClicked() {
        checkSignIn = !checkSignIn
        if (checkSignIn()) {
            model.signInTxt = "SIGN UP"
            model.signUpTxt = "SIGN IN"
            notifyPropertyChanged(BR.signInTxt)
            notifyPropertyChanged(BR.signUpTxt)
        } else {
            model.signInTxt = "SIGN IN"
            model.signUpTxt = "SIGN UP"
            notifyPropertyChanged(BR.signInTxt)
            notifyPropertyChanged(BR.signUpTxt)
        }
    }

    // method to keep a check
    // that variable fields must
    // not be kept empty by user
    val isValid: Boolean
        get() = (!TextUtils.isEmpty(userEmail) && Patterns.EMAIL_ADDRESS.matcher(
            userEmail
        ).matches()
                && userPassword!!.length > 5)

    // constructor of ViewModel class
    init {

        // instantiating object of
        // model class
        model = LoginModel("", "", "SIGN IN", "SIGN UP", false)
    }
}