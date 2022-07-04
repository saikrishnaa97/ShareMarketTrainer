package com.krishna.sharemarkettrainer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChangersAdapter(context: Context, changersList: List<Table>) : RecyclerView.Adapter<ChangersAdapter.ViewHolder?>() {

    private val context: Context
    private val changersList: List<Table>

    init{
        this.context = context
        this.changersList = changersList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangersAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.changers_layout_item,parent, false)
        return ChangersAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChangersAdapter.ViewHolder, position: Int) {
        var changeItem = changersList[position]
        holder.price_change.text = "Rs. "+changeItem.change_val.toString()
        holder.percent_change.text = changeItem.change_percent.toString()+"%"
        holder.stock_name_changers.text = changeItem.ScripName.toString()
        holder.cur_price_changers.text = "Rs. "+changeItem.Ltradert.toString()
        if (changeItem.change_val > 0){
            holder.price_change.setTextColor(Color.GREEN)
            holder.percent_change.setTextColor(Color.GREEN)
            holder.price_change.text = "+Rs."+changeItem.change_val.toString()
            holder.percent_change.text = "+"+changeItem.change_percent.toString()+"%"
        }
        else if (changeItem.change_val < 0) {
            holder.price_change.setTextColor(Color.RED)
            holder.percent_change.setTextColor(Color.RED)
        }
        else {
            holder.price_change.setTextColor(Color.WHITE)
            holder.percent_change.setTextColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener({
            val intent = Intent(context,StockTradeActivity::class.java)
            intent.putExtra("stockSymbol",changeItem.ScripName)
            context.startActivity(intent)
        })

    }

    override fun getItemCount(): Int {
        return changersList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var stock_name_changers: TextView
        var cur_price_changers: TextView
        var percent_change: TextView
        var price_change: TextView

        init {
            stock_name_changers = itemView.findViewById(R.id.stock_name_changers)
            cur_price_changers = itemView.findViewById(R.id.cur_price_changers)
            percent_change = itemView.findViewById(R.id.percent_change)
            price_change = itemView.findViewById(R.id.price_change)
        }
    }
}