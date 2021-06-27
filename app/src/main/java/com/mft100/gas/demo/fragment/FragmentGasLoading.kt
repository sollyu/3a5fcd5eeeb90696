package com.mft100.gas.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mft100.gas.demo.databinding.FragmentGasLoadingBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
@AndroidEntryPoint
internal class FragmentGasLoading : Fragment() {

    private lateinit var mViewBinding: FragmentGasLoadingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewBinding = FragmentGasLoadingBinding.inflate(inflater, container, false)
        return mViewBinding.root
    }
}