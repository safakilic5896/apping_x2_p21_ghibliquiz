package com.example.apping_x2_p21_ghibliquiz

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PeopleAdapter (val data: List<PeopleItem>, val context: Activity, val onItemClickListener: View.OnClickListener) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    class ViewHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {
        val age: TextView = rowView.findViewById(R.id.age_character)
        val image: ImageView = rowView.findViewById(R.id.gender_character)
        val name : TextView = rowView.findViewById(R.id.name_character)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.item_character, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val people: PeopleItem = data[position]
        holder.age.text = people.age
        holder.name.text = people.name

        if (people.gender.equals("Male")) {
            holder.image.setImageResource(R.drawable.male)
        } else {
            holder.image.setImageResource(R.drawable.femele)
        }
        holder.itemView.tag = position
    }

}