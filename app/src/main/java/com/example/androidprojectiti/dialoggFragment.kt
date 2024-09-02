
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.androidprojectiti.R


class DialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.fragment_dialogg, null)
        // val animationView = dialogView.findViewById<LottieAnimationView>(R.id.animation_view)

        val redirectButton = dialogView.findViewById<Button>(R.id.continue_b)


        redirectButton.setOnClickListener {

            findNavController().navigate(R.id.action_dialogFragment_to_loginFragment)

        }

        builder.setView(dialogView)

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialogg, container, false)

        val layout: LinearLayout = view.findViewById(R.id.layout)
        layout.setBackgroundResource(R.drawable.dialog_background)

        return view
    }
}
