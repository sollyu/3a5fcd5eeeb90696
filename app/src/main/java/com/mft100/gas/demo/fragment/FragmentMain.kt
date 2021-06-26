package com.mft100.gas.demo.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mft100.gas.demo.R
import com.mft100.gas.demo.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
@AndroidEntryPoint
internal class FragmentMain : FragmentBase(), View.OnClickListener {
    private lateinit var mViewBinding: FragmentMainBinding

    override fun onCreateView(): View {
        mViewBinding = FragmentMainBinding.inflate(LayoutInflater.from(requireContext()), baseFragmentActivity.fragmentContainerView, false)
        return mViewBinding.root
    }

    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)

        mViewBinding.qmuiTopBarLayout.setTitle(R.string.app_name)
        mViewBinding.btn001.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            mViewBinding.btn001 -> onClickBtn001(view)
        }
    }

    private fun onClickBtn001(view: View) {
        startFragment(FragmentGas())
    }
}