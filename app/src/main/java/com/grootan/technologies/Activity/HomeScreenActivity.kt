package com.grootan.technologies.Activity

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.grootan.technologies.Utils.SessionManagerSP
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.grootan.technologies.Adapter.MatrixAdapter
import com.grootan.technologies.Module.CubeModel
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.os.CountDownTimer
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.grootan.technologies.Module.UserModel
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.os.Bundle
import com.grootan.technologies.R
import android.os.Build
import android.view.WindowManager
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.grootan.technologies.Utils.CustomToast
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.grootan.technologies.Fragments.AttemptsFragment
import java.util.*

class HomeScreenActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var sessionManagerSP: SessionManagerSP? = null
    var authListener: AuthStateListener? = null
    var matrixAdapter: MatrixAdapter? = null
    var cubeModelList: MutableList<CubeModel?>? = null
    var recyclerview: RecyclerView? = null
    var img_sign_out: ImageView? = null
    var txt_scramble: TextView? = null
    var txt_timer: TextView? = null
    var txt_Attempts: TextView? = null
    var lin_attempts: LinearLayout? = null
    val time = intArrayOf(45)
    var cTimer: CountDownTimer? = null
    var noOfAttemts = 0
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var userModel: UserModel? = null
    var progressBar: ProgressBar? = null
    var lin_scramble: LinearLayout? = null
    var noOfMoves = ""
    var userModelList: MutableList<UserModel?>? = null
    var checkSwap = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(
                this@HomeScreenActivity,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                true
            )
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(
                this@HomeScreenActivity,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                false
            )
            window.statusBarColor = Color.TRANSPARENT
        }
        recyclerview = findViewById(R.id.recyclerview)
        img_sign_out = findViewById(R.id.img_sign_out)
        txt_scramble = findViewById(R.id.txt_scramble)
        txt_timer = findViewById(R.id.txt_timer)
        progressBar = findViewById(R.id.progressBar)
        lin_scramble = findViewById(R.id.lin_scramble)
        txt_Attempts = findViewById(R.id.txt_Attempts)
        lin_attempts = findViewById(R.id.lin_attempts)
        cubeModelList = ArrayList()
        userModelList = ArrayList()
        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("GrootanUserInfo")
        userModel = UserModel()
        sessionManagerSP = SessionManagerSP(this@HomeScreenActivity)
        authListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                sessionManagerSP!!.setlogin("0")
                val intent = Intent(this@HomeScreenActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        with(img_sign_out) {
            this?.setOnClickListener(View.OnClickListener {
                signOut()
            })
        }
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(10, "#FF0000", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(10, "#FF0000", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(10, "#FF0000", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(20, "#FFFF00", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(20, "#FFFF00", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(20, "#FFFF00", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(30, "#0000FF", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(30, "#0000FF", false))
        (cubeModelList as ArrayList<CubeModel?>).add(CubeModel(30, "#0000FF", false))
        val mLayoutManager = GridLayoutManager(this@HomeScreenActivity, 3)
        with(recyclerview) {
            this?.setLayoutManager(mLayoutManager)
        }
        matrixAdapter = MatrixAdapter(
            this@HomeScreenActivity, cubeModelList as ArrayList<CubeModel?>
        )
        with(recyclerview) { this?.setAdapter(matrixAdapter) }
        matrixAdapter!!.notifyDataSetChanged()

        txt_scramble!!.setOnClickListener(View.OnClickListener {
            Collections.shuffle(cubeModelList)
            matrixAdapter!!.setCheckSwap(true)
            matrixAdapter!!.notifyDataSetChanged()
            if (cTimer != null) {
                cTimer!!.cancel()
            }
            startTimer()
            if (checkSwap) {
                noOfAttemts++
                txt_Attempts!!.setText("No.of Attempts : $noOfAttemts")
//                val isConnected = ConnectivityReceiver.isConnected
//                if (isConnected) {
//                    addDatatoFirebase(
//                        user!!.email,
//                        txt_scramble!!.getText().toString(),
//                        noOfMoves,
//                        noOfAttemts.toString()
//                    )
//                } else {
//                    CustomToast.getInstance(this@HomeScreenActivity)
//                        ?.showSmallCustomToast("Please check your internet connection")
//                }
            }
            checkSwap = true
        })

        lin_attempts!!.setOnClickListener {
            unMappedAddSellerFragment()
        }
    }

    fun resetList() {
        if (cubeModelList!!.size > 0) {
            cubeModelList!!.clear()
        }
        cubeModelList!!.add(CubeModel(10, "#FF0000", false))
        cubeModelList!!.add(CubeModel(10, "#FF0000", false))
        cubeModelList!!.add(CubeModel(10, "#FF0000", false))
        cubeModelList!!.add(CubeModel(20, "#FFFF00", false))
        cubeModelList!!.add(CubeModel(20, "#FFFF00", false))
        cubeModelList!!.add(CubeModel(20, "#FFFF00", false))
        cubeModelList!!.add(CubeModel(30, "#0000FF", false))
        cubeModelList!!.add(CubeModel(30, "#0000FF", false))
        cubeModelList!!.add(CubeModel(30, "#0000FF", false))
        matrixAdapter!!.setCheckSwap(false)
        matrixAdapter!!.notifyDataSetChanged()
    }

    fun signOut() {
        auth!!.signOut()
    }

    fun getNoOfMoves(count: Int) {
        noOfMoves = count.toString()
    }

    fun startTimer() {
        cTimer = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txt_timer!!.text = "0:" + millisUntilFinished % 45000 / 1000
                time[0]--
            }

            override fun onFinish() {
                txt_timer!!.text = "Finished"
                resetList()
                noOfAttemts++
                txt_Attempts!!.text = "No.of Attempts : $noOfAttemts"
//                val isConnected = ConnectivityReceiver.isConnected
//                if (isConnected) {
//                    addDatatoFirebase(
//                        user!!.email,
//                        txt_scramble!!.text.toString(),
//                        noOfMoves,
//                        noOfAttemts.toString()
//                    )
//                } else {
//                    CustomToast.getInstance(this@HomeScreenActivity)
//                        ?.showSmallCustomToast("Please check your internet connection")
//                }
            }
        }.start()
    }

    fun checkDigit(number: Int): String {
        return if (number <= 9) "0$number" else number.toString()
    }

    public override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }

    fun addDatatoFirebase(name: String?, time: String?, noOfMoves: String?, attemptsNum: String?) {
        // below 3 lines of code is used to set
        // data in our object class.
        lin_scramble!!.visibility = View.GONE
        progressBar!!.visibility = View.VISIBLE
        userModel!!.name = name.toString()
        userModel!!.time = time.toString()
        userModel!!.attemptsNum = attemptsNum.toString()
        userModel!!.noOfMoves = noOfMoves.toString()

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference!!.setValue(userModel)
                lin_scramble!!.visibility = View.VISIBLE
                progressBar!!.visibility = View.GONE
                // after adding this data we are showing toast message.
                CustomToast.getInstance(this@HomeScreenActivity)?.showSmallCustomToast("Data Added")
            }

            override fun onCancelled(error: DatabaseError) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                lin_scramble!!.visibility = View.VISIBLE
                progressBar!!.visibility = View.GONE
                CustomToast.getInstance(this@HomeScreenActivity)
                    ?.showSmallCustomToast("Fail to add data")
            }
        })
    }

    val attemptsList: Unit
        get() {
            databaseReference = databaseReference!!.child("GrootanUserInfo")
            databaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val userModel = snapshot.getValue(UserModel::class.java)
                        Log.d("GrootanUserInfo: ", userModel!!.name + " " + userModel.time)
                        userModelList!!.add(userModel)
                        if (userModel.attemptsNum != null) {
                            txt_Attempts!!.text = "No.of Attempts : " + userModel.attemptsNum
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    fun unMappedAddSellerFragment() {
        val addSellerFragment = AttemptsFragment()
        val bundle = Bundle()
        bundle.putString("email", "xyz@gmail.com")
        bundle.putBoolean("fullScreen", true)
        bundle.putBoolean("notAlertDialog", true)
        addSellerFragment.arguments = bundle
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        addSellerFragment.show(ft, "dialog")
    }

}