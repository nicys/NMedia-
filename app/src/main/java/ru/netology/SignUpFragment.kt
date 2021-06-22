package ru.netology

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.databinding.FragmentSignUpBinding
import ru.netology.databinding.FragmentSingInBinding
import ru.netology.util.AndroidUtils
import ru.netology.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {

    @ExperimentalCoroutinesApi
    private val viewModelAuth: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.enter.setOnClickListener {
            val userName = binding.inputName.text?.trim().toString()
            val login = binding.inputLogin2.text?.trim().toString()
            val password = binding.inputPassword2.text?.trim().toString()
            if (userName != null || login != null || password != null) {
                viewModelAuth.registration(userName, login, password)
                AndroidUtils.hideKeyboard(it)
                findNavController().navigateUp()
            } else {
                Snackbar.make(requireView(), getString(R.string.dontFilled), Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
        }

        binding.toSingIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}