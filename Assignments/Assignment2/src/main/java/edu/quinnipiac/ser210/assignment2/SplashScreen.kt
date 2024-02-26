/*
Start Fragment when user runs app
@author cdavignon
@date 2/25/2024
 */
package edu.quinnipiac.ser210.assignment2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.activityViewModels


class SplashScreen : Fragment() {
    private val playboardFragmentModel: edu.quinnipiac.ser210.assignment2.View by activityViewModels()
    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val findName = view.findViewById<TextInputEditText>(R.id.userName)

        view.findViewById<Button>(R.id.START).setOnClickListener {
            val name = findName.text.toString()
            playboardFragmentModel.setText(name)
            navController.navigate(R.id.connect_splash_to_playboard)
        }
    }
}