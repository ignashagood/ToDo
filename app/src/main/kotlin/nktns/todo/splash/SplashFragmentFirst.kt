package nktns.todo.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nktns.todo.databinding.SplashScreenFirstBinding
import nktns.todo.main.MainActivity

class SplashFragmentFirst : Fragment() {

    private var binding: SplashScreenFirstBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        SplashScreenFirstBinding.inflate(inflater, container, false).run {
            binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.buttonNext?.setOnClickListener {
            (activity as MainActivity).showSplashScreenSecond()
        }
    }
}
