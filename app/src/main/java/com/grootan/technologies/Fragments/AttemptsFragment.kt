package com.grootan.technologies.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.grootan.technologies.R
import com.grootan.technologies.Utils.SessionManagerSP


class AttemptsFragment : DialogFragment() {

    val sessionManagerSP: SessionManagerSP? = null

    fun AttemptsFragment() {
        // Required empty public constructor
    }

    override fun getTheme(): Int {
        return R.style.Theme_GrootanTechnologies_NoActionBar_FullScreenDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_attempts, container, false)

        return view
    }
}