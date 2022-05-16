package com.example.assignment3.activities.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.assignment3.R
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var mAuth : FirebaseAuth

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get all preferences and store the name inside of namePref
        val preferences = this.context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val namePref = preferences?.getString("name", "")
        val imageTakenPref = preferences?.getBoolean("image_taken", true)

        // Find components on the screen
        val nameText: TextView = view.findViewById(R.id.homeGreeting) as TextView
        val profileImage: CircleImageView = view.findViewById(R.id.profileImage)

        val currentUser = mAuth.currentUser
        if(currentUser != null) {
            Toast.makeText(this.context, "Welcome, ${currentUser.email}!", Toast.LENGTH_SHORT).show()
        }

        nameText.text = "Welcome to our open day, ${namePref.toString()}"

        if (imageTakenPref!!)
        {
            profileImage.rotation = 90F
            setImageFromPath("storage/emulated/0/Android/media/com.example.assignment3/ProfileImage.jpg",
                profileImage)
        }
    }

    private fun setImageFromPath(imagePath: String, imageView: CircleImageView){
        val imgFile = File(imagePath)
        if (imgFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}