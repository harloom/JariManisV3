package com.app.jarimanis.ui.thread

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_thread_more.*

class ThreadInfo (private val item  :Doc , private val callback  : InteractionEditClick): BottomSheetDialogFragment() {

    private lateinit var btnHapus: LinearLayout
    private lateinit var btnEdit: LinearLayout
    private lateinit var btnLapor: LinearLayout


    private val TAG = "DialogProfile"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_thread_more, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setCancelable(true)
        return dialog
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnHapus =txtHapus
        btnLapor = txtReport
        btnEdit = txtEdit
        btnHapus.setOnClickListener {
            val dialog =
                MaterialAlertDialogBuilder(activity).setMessage("Apakah Anda Ingin Menghapus!")
                    .setPositiveButton("OK") { dialogInterface, i ->
                        callback.onDeleteListener(item)
                        dismiss()
                    }.setCancelable(false).setNegativeButton("Batal") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }


            dialog.show()
        }

        val mDiskusi  = view.findViewById<TextView>(R.id.tv_count_diskusi)
        val mLike = view.findViewById<TextView>(R.id.tv_count_like)

        mDiskusi.setText(item.diskusiCount.toString())
        if(item.likes!!.isNotEmpty()){
            mLike.setText(item.likes.size.toString())
        }

        if (item.user?.id != TokenUser.idUser) {
            btnEdit.visibility = View.GONE
            btnHapus.visibility = View.GONE
        }

        action_dismiss.setOnClickListener {
            dismiss()
        }

        btnEdit.setOnClickListener {
            val newFragment = DialogEditThread(item,callback)
            if (fragmentManager != null) {
                newFragment.show(fragmentManager!!, "morePost")
            }

        }
        btnLapor.setOnClickListener {
            val dialog =
                MaterialAlertDialogBuilder(activity).setMessage("Apakah Anda Ingin Melapor!")
                    .setPositiveButton("OK") { dialogInterface, i ->
                    }.setCancelable(true).setNegativeButton("Batal") { dialogInterface, i ->
                        callback.onLaporListener(item)
                        dialogInterface.dismiss()
                    }


            dialog.show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun log(s: String) {
        Log.d(TAG, "Log : $s")
    }

    private fun deletePost(itemId: String) {



    }
}

