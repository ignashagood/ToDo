package nktns.todo.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.databinding.SplashScreenSecondBinding
import nktns.todo.main.MainActivity

class SplashFragmentSecond : Fragment() {

    private var binding: SplashScreenSecondBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        SplashScreenSecondBinding.inflate(inflater, container, false).run {
            binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.buttonNext?.setOnClickListener {
            (activity as MainActivity).showMainScreen()
        }
    }
}
