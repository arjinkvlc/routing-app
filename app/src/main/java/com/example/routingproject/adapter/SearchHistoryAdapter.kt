import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.R
import com.example.routingproject.data.model.SearchHistoryData

class SearchHistoryAdapter(
    private val searchHistoryList: List<SearchHistoryData>,
    private val onItemClick: (SearchHistoryData) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    inner class SearchHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchText: TextView = itemView.findViewById(R.id.searchText)

        fun bind(searchHistory: SearchHistoryData) {
            searchText.text = searchHistory.searchString
            itemView.setOnClickListener {
                onItemClick(searchHistory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return SearchHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(searchHistoryList[position])
    }

    override fun getItemCount(): Int = searchHistoryList.size
}
