package ru.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.databinding.FragmentSingInBinding
import ru.netology.util.AndroidUtils
import ru.netology.viewmodel.AuthViewModel

class SignInFragment : Fragment() {
//    companion object {
//        var Bundle.postPhoto: String? by StringArg
//        var Bundle.postData: Post? by PostArg
//    }

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
        val binding = FragmentSingInBinding.inflate(inflater, container, false)

        binding.enter.setOnClickListener {
            val login = binding.inputLogin.text?.trim().toString()
            val password = binding.inputPassword.text?.trim().toString()
            if (login != null && password != null) {
                viewModelAuth.authentication(login, password)
                AndroidUtils.hideKeyboard(it)
                findNavController().navigateUp()
            }
        }



        return binding.root
    }
}