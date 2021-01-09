package com.josua.movapp.home.tiket

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*

import com.josua.movapp.R
import com.josua.movapp.home.dashboard.ComingSoonAdapter
import com.josua.movapp.model.Film
import com.josua.movapp.utils.Preferences
import kotlinx.android.synthetic.main.fragment_tiket.*

/**
 * A simple [Fragment] subclass.
 */
class TiketFragment : Fragment() {

    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference
    private var dataList = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tiket, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(context!!)
        mDatabase = FirebaseDatabase.getInstance("https://movapp-5f54f-default-rtdb.firebaseio.com/").getReference("Film")

        rc_tiket.layoutManager = LinearLayoutManager(context)
        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, ""+p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                dataList.clear()
                for (getdataSnapshot in p0.children){
                    val film = getdataSnapshot.getValue(Film::class.java)
                    dataList.add(film!!)
                }

                rc_tiket.adapter = ComingSoonAdapter(dataList){
                    var intent = Intent(context, TiketActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }

                tv_total.setText("${dataList.size}Movies")
            }

        })
    }

}
