package com.example.routingproject.schedule

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.routingproject.R
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AddScheduleDialogFragment : DialogFragment() {

    private lateinit var tripTitleEditText: EditText
    private lateinit var tripDescriptionEditText: EditText
    private lateinit var tripStartTimeEditText: EditText
    private lateinit var scheduleViewModel: ScheduleViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    //val tripStartTime = "2024-05-18T12:58:11.681Z"
    private var listener: AddScheduleDialogListener? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_add_schedule_dialog, null)
        builder.setView(dialogView)

        // Initialize user repository
        val userRepository = context?.let { UserRepository(RetrofitClient.apiService, it) }

        if (userRepository != null) {
            // Initialize ViewModel with userRepository
            scheduleViewModel = ViewModelProvider(this, ScheduleDialogViewModelFactory(userRepository, requireContext()))[ScheduleViewModel::class.java]
        } else {
            throw IllegalStateException("UserRepository should not be null")
        }

        tripTitleEditText = dialogView.findViewById(R.id.tripTitleEditText)
        tripDescriptionEditText = dialogView.findViewById(R.id.tripDescriptionEditText)
        tripStartTimeEditText = dialogView.findViewById(R.id.tripStartTimeEditText)

        tripStartTimeEditText.setOnClickListener {
            showDateTimePickerDialog()
        }

        builder.setPositiveButton("Confirm") { dialog, _ ->
            // Kullanıcının girdiği değerler alınıyor
            val tripTitleInput = tripTitleEditText.text.toString()
            val tripDescriptionInput = tripDescriptionEditText.text.toString()
            val tripStartTimeInput = tripStartTimeEditText.text.toString()
            addTrip(tripTitleInput, tripDescriptionInput, tripStartTimeInput, false)
            Log.d("testtrip", addTrip(tripTitleInput, tripDescriptionInput, tripStartTimeInput, true).toString())
            listener?.onDialogPositiveClick(tripTitleInput, tripDescriptionInput, tripStartTimeInput)
            Log.d("testtrip", tripTitleInput + tripDescriptionInput)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        return builder.create()
    }

    private fun showDateTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(Calendar.YEAR, selectedYear)
            calendar.set(Calendar.MONTH, selectedMonth)
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay)

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                tripStartTimeEditText.setText(dateFormat.format(calendar.time))
            }, hour, minute, true)

            timePickerDialog.show()
        }, year, month, day)

        datePickerDialog.show()
    }

    fun addTrip(tripName: String, tripDescription: String?, startTime: String, isFavourite: Boolean?): Boolean {
        var addTripSucceed: Boolean = false
        scheduleViewModel.viewModelScope.launch {
            // addTripSucceed = scheduleViewModel.addTrip(tripName, tripDescription!!, startTime, isFavourite!!)
        }
        return addTripSucceed
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setListener(listener: AddScheduleDialogListener) {
        this.listener = listener
    }

    interface AddScheduleDialogListener {
        fun onDialogPositiveClick(itemName: String, startTime: String, endTime: String){

        }
    }

}

class ScheduleDialogViewModelFactory(private val userRepository: UserRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(userRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
