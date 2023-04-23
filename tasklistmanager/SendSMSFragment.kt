package com.example.tasklistmanager

import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tasklistmanager.databinding.FragmentSendSMSBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SendSMSFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSendSMSBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.SendMessageButton.setOnClickListener {
            sendSMS()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendSMSBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun sendSMS() {
        val phoneNumber = "4356199863"
        val message = binding.messageEdittext.toString()
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber,null,message,null,null)
        Toast.makeText(context,"Message Sent!", Toast.LENGTH_SHORT).show()
    }


}