package ru.netology

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.auth.AppAuth
import ru.netology.databinding.FragmentExitBinding
import ru.netology.viewmodel.AuthViewModel

class ExitFragment : Fragment() {

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
        val binding = FragmentExitBinding.inflate(inflater, container, false)

        binding.yesButton.setOnClickListener {
            AppAuth.getInstance().removeAuth()
            findNavController().navigateUp()
        }

        binding.notButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}