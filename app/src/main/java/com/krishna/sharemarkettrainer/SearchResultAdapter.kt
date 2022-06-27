package com.krishna.sharemarkettrainer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchResultAdapter(context: Context, stocksList: List<Symbol>) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder?>() {

    private val context: Context
    private val stocksList: List<Symbol>

    init{
        this.context = context
        this.stocksList = stocksList
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var stockSymbol: TextView
        var stockCompanyName: TextView
        var category: TextView
        var searchResultLayout: RelativeLayout

        init {
            stockSymbol = itemView.findViewById(R.id.stock_symbol_text)
            stockCompanyName = itemView.findViewById(R.id.stock_company_name)
            category = itemView.findViewById(R.id.category)
            searchResultLayout = itemView.findViewById(R.id.search_rel_layout)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.search_item_layout,parent, false)
        return SearchResultAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stockData: Symbol = stocksList[position]
        holder.category.text = stockData.result_sub_type
        holder.stockSymbol.text = stockData.symbol
        var stockSymbolText = stockData.symbol_info

        if (stockSymbolText.length > 25){
            stockSymbolText = stockSymbolText.subSequence(0,24).toString().plus("...")
        }

        holder.stockCompanyName.text = stockSymbolText
        holder.searchResultLayout.setOnClickListener({
            val intent = Intent(context,StockTradeActivity::class.java)
            intent.putExtra("stockSymbol",stockData.symbol)
            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return stocksList.size
    }

}