import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.R
import com.example.routingproject.adapter.ScheduleAdapter

class ScheduleFragment : Fragment(), AddScheduleDialogFragment.AddScheduleDialogListener {

    private lateinit var scheduleAdapter: ScheduleAdapter
    private var deleteButton: ImageButton? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerviewList)
        scheduleAdapter = ScheduleAdapter(mutableListOf())
        recyclerView.adapter = scheduleAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val addButton = view.findViewById<Button>(R.id.addButton)
        addButton.setOnClickListener {
            val dialog = AddScheduleDialogFragment()
            dialog.setListener(this)
            dialog.show(childFragmentManager, "AddScheduleDialogFragment")
        }

        deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)
        Log.d("test",deleteButton.toString())
        deleteButton?.setOnClickListener {
            Log.d("test","basarili1")
            val lastItemPosition = scheduleAdapter.itemCount - 1
            if (lastItemPosition >= 0) {
                Log.d("test","basarili2")
                scheduleAdapter.removeItem(lastItemPosition)
                Log.d("test","basarili3")
            }
        }

        return view
    }

    override fun onDialogPositiveClick(itemName: String, startTime: String, endTime: String) {
        val newItem = "$itemName    ~     $startTime - $endTime"
        scheduleAdapter.addItem(newItem)
    }
}
