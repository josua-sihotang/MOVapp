package com.josua.movapp.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josua.movapp.R
import com.josua.movapp.model.Checkout
import kotlinx.android.synthetic.main.row_item_now_playing.view.*
import java.text.NumberFormat
import java.util.*

class CheckoutAdapter(private var data: List<Checkout>,
                      private val listener:(Checkout) -> Unit)
    : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    lateinit var contextAdapter : Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflantedView = layoutInflater.inflate(R.layout.row_item_checkout, parent, false)
        return ViewHolder(inflantedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CheckoutAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position], listener, contextAdapter)
    }

    class ViewHolder(view : View): RecyclerView.ViewHolder(view){
        private val tvTitle:TextView = view.findViewById(R.id.tv_kursi)
        private val tvHarga:TextView = view.findViewById(R.id.tv_harga)

        fun bindItem(data:Checkout, listener: (Checkout) -> Unit, context: Context){

            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            tvHarga.setText(formatRupiah.format(data.harga!!.toDouble()))

            if (data.kursi!!.startsWith("total")) {
                tvTitle.setText(data.kursi)
                tvTitle.setCompoundDrawables(null, null, null, null)
            } else {
                tvTitle.setText("Bangku No."+data.kursi)
            }

            itemView.setOnClickListener {
                listener(data)
            }

        }
    }

}
