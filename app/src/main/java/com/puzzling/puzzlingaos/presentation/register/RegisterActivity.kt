package com.puzzling.puzzlingaos.presentation.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.puzzling.puzzlingaos.R
import com.puzzling.puzzlingaos.base.BaseActivity
import com.puzzling.puzzlingaos.databinding.ActivityRegisterBinding
import com.puzzling.puzzlingaos.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    private val viewModel: RegisterViewModel by viewModels { ViewModelFactory(this) }

    private lateinit var dayCycleAdapter: DayCycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dayCycleAdapter = DayCycleAdapter(this)
        binding.viewModel = viewModel

        clickDatePicker()
        clickDayCyclePicker()
        textBoxListener(viewModel.projectName)
        textBoxListener(viewModel.projectExplanation)
        textBoxListener(viewModel.role)
        textBoxListener(viewModel.nickName)
    }

    private fun clickDatePicker() {
        binding.btnDateDropDown.setOnClickListener {
            // binding.layoutRegisterDate.setBackgroundResource(R.drawable.rect_blue_line_16)
            pickedDate()
        }
    }

    private fun pickedDate() {
        val bottomSheetFragment = DatePickerFragment()
        bottomSheetFragment.setOnDateSelectedListener(object :
            DatePickerFragment.OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val formattedDateTextBox = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(selectedDate.time)
                val formattedDateRegister = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
                binding.tvDateDropDown.text = formattedDateTextBox
                viewModel.projectStartDate = formattedDateRegister
            }
        })

        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun clickDayCyclePicker() {
        binding.rvDayCycle.adapter = DayCycleAdapter(this)
        binding.rvDayCycle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        dayCycleAdapter.setOnDayClickListener { response ->
            viewModel.isDateCycleSelected = dayCycleAdapter.selectedDayArray
        }
    }

    private fun textBoxListener(textBox: MutableLiveData<String>) {
        // val textBoxString: String = textBox.value ?: "Null Value"
        textBox.observe(this) { textBoxString ->
            viewModel.let { viewModel ->
                if (!viewModel.validTextBox(textBoxString)) {
                    when (textBox) {
                        viewModel.projectName -> binding.textLayoutProjectName.error = "특수문자, 이모지를 사용할 수 없어요"
                        viewModel.projectExplanation -> binding.textLayoutIntroduction.error = "특수문자, 이모지를 사용할 수 없어요"
                        viewModel.role -> binding.textLayoutRole.error = "특수문자, 이모지를 사용할 수 없어요"
                        viewModel.nickName -> binding.textLayoutNickName.error = "특수문자, 이모지를 사용할 수 없어요"
                    }
                } else {
                    when (textBox) {
                        viewModel.projectName -> binding.textLayoutProjectName.error = null
                        viewModel.projectExplanation -> binding.textLayoutIntroduction.error = null
                        viewModel.role -> binding.textLayoutRole.error = null
                        viewModel.nickName -> binding.textLayoutNickName.error = null
                    }
                }
            }
        }
    }
}
