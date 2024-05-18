
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.welfarebenefits.R
import com.example.welfarebenefits.adapter.CityDoAdapter
import com.example.welfarebenefits.databinding.ResidencedialogBinding
import com.example.welfarebenefits.util.CitySelectionViewModel
import com.example.welfarebenefits.util.CitysFragment

interface ShowCustomDialogListener {
    fun onPositiveButtonClicked(selectedCity: String)
}

class ShowCustomDialog(
    private val listener: ShowCustomDialogListener
) : DialogFragment() {
    private var mBinding: ResidencedialogBinding? = null
    private val binding get() = mBinding!!
    private lateinit var fragmentManager: FragmentManager
    private val viewModel: CitySelectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = ResidencedialogBinding.inflate(inflater, container, false)
        val view = binding.root
        fragmentManager = childFragmentManager
        setupDialog()
        return view
    }

    private fun setupDialog() {
        val cityDoList = arrayOf(
            "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시",
            "대전광역시", "울산광역시", "세종특별자치시", "경기도", "충청북도",
            "충청남도", "전라남도", "경상북도", "경상남도",
            "제주특별자치도", "강원특별자치도", "전북특별자치도"
        )

        val cityDoAdapter = CityDoAdapter(requireContext(), cityDoList)
        binding.listView.adapter = cityDoAdapter

        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val fragment = CitysFragment(cityDoList[position])
            Log.e("ShowCustomDialog", cityDoList[position])
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment).commit()
            Log.e("ShowCustomDialog", "프래그먼트 변경완료")
        }

        binding.button.setOnClickListener {
            viewModel.getSelectedCity()?.let { it1 -> listener?.onPositiveButtonClicked(it1) }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}
