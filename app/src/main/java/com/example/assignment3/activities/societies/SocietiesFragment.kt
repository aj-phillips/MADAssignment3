package com.example.assignment3.activities.societies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.assignment3.R
import com.example.assignment3.adapters.SocietyAdapter
import com.example.assignment3.model.society.Data
import com.example.assignment3.model.society.Society

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SocietiesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SocietiesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val dataList: MutableList<Data> = mutableListOf()
    private lateinit var societyAdapter: SocietyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        societyAdapter = SocietyAdapter(dataList)

        AndroidNetworking.initialize(context)

        AndroidNetworking.get("https://leg1tt.github.io/JsonData/societies.json").build()
            .getAsObject(Society::class.java, object : ParsedRequestListener<Society> {
                override fun onResponse(response: Society?) {
                    response?.let { dataList.addAll(it.data) }
                    societyAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_societies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewComponent = view.findViewById<RecyclerView>(R.id.societiesList)
        recyclerViewComponent.layoutManager = LinearLayoutManager(this.context)
        recyclerViewComponent.addItemDecoration(DividerItemDecoration(this.context, OrientationHelper.VERTICAL))
        recyclerViewComponent.adapter = societyAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SocietiesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SocietiesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}