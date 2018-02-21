package com.example.gustavo.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

class AdaptadorMensaje(items: ArrayList<Mensaje>?, var cCodUsu: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM1 = 1
    private val ITEM2 = 2

    private var items = ArrayList<Mensaje>()

    init {
        this.items = items!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            ITEM1 -> viewHolder = Item1Holder(inflater.inflate(R.layout.mensaje_item_rec, parent, false))
            ITEM2 -> viewHolder = Item2Holder(inflater.inflate(R.layout.mensaje_item_env, parent, false))
            else -> viewHolder = Item1Holder(inflater.inflate(R.layout.mensaje_item_rec, parent))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM1 -> {
                val item1 = items[position]
                val item1Holder = holder as Item1Holder
                item1Holder.tvTexto1.text = item1.texto
                item1Holder.tvNombre1.text = item1.getcCodUsu()
            }
            ITEM2 -> {
                val item2 = items[position]
                val item2Holder = holder as Item2Holder
                item2Holder.tvTexto2.text = item2.texto
                item2Holder.tvNombre2.text = item2.getcCodUsu()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position].getcCodUsu() == cCodUsu) {
            return ITEM2
        } else {
            return ITEM1
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal inner class Item1Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTexto1: TextView
        var tvNombre1: TextView

        init {
            tvNombre1 = itemView.findViewById<View>(R.id.tvNombreRec) as TextView
            tvTexto1 = itemView.findViewById<View>(R.id.tvTextoRec) as TextView
        }
    }

    internal inner class Item2Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTexto2: TextView
        var tvNombre2: TextView

        init {
            tvNombre2 = itemView.findViewById<View>(R.id.tvNombreEnv) as TextView
            tvTexto2 = itemView.findViewById<View>(R.id.tvTextoEnv) as TextView
        }
    }
}