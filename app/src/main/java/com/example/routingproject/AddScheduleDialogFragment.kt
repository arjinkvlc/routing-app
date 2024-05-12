import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.routingproject.R

class AddScheduleDialogFragment : DialogFragment() {

    private lateinit var itemNameEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText

    private var listener: AddScheduleDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_add_schedule_dialog, null)
        builder.setView(dialogView)

        itemNameEditText = dialogView.findViewById(R.id.itemNameEditText)
        startTimeEditText = dialogView.findViewById(R.id.startTimeEditText)
        endTimeEditText = dialogView.findViewById(R.id.endTimeEditText)

        builder.setPositiveButton("Confirm") { dialog, _ ->
            // Kullanıcının girdiği değerler alınıyor
            val itemName = itemNameEditText.text.toString()
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()

            listener?.onDialogPositiveClick(itemName, startTime, endTime)

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        return builder.create()
    }

    fun setListener(listener: AddScheduleDialogListener) {
        this.listener = listener
    }

    interface AddScheduleDialogListener {
        fun onDialogPositiveClick(itemName: String, startTime: String, endTime: String)
    }
}
