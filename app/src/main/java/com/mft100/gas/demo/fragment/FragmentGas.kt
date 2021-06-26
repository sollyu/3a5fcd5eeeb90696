package com.mft100.gas.demo.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import com.mft100.gas.demo.R
import com.mft100.gas.demo.databinding.FragmentGasBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import org.jetbrains.annotations.NotNull

@FragmentScoped
@AndroidEntryPoint
internal class FragmentGas : FragmentBase(), View.OnClickListener {

    private lateinit var mViewBinding: FragmentGasBinding
    private lateinit var mTopBarButtonBack: AppCompatImageButton

    override fun onCreateView(): View {
        mViewBinding = FragmentGasBinding.inflate(LayoutInflater.from(requireContext()), baseFragmentActivity.fragmentContainerView, false)
        return mViewBinding.root
    }

    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        val context: Context = rootView.context
        mViewBinding.qmuiTopBarLayout.setTitle(R.string.app_name)
        mTopBarButtonBack = mViewBinding.qmuiTopBarLayout.addLeftBackImageButton()
        mTopBarButtonBack.setOnClickListener(this)

        mViewBinding.btn001.setOnClickListener(this)
    }

    override fun onClick(@NotNull view: View) {
        when (view) {
            mTopBarButtonBack   -> onClickTopBarBack(view)
            mViewBinding.btn001 -> onClickLeftButton001(view)
        }
    }

    private fun onClickLeftButton001(view: View) {
        val content = FragmentGasContent()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_content, content)
        fragmentTransaction.commit()
    }

    private fun onClickTopBarBack(view: View) {
        popBackStack()
    }

}
