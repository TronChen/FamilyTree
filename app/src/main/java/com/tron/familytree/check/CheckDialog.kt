package com.tron.familytree.check

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.Lottie
import com.tron.familytree.FamilyTreeApplication
import com.tron.familytree.R
import com.tron.familytree.databinding.DialogCheckBinding
import java.io.InputStream

class CheckDialog : AppCompatDialogFragment() {

    var iconRes: InputStream? = null
    var message: String? = null
    private val messageType by lazy {
        CheckDialogArgs.fromBundle(requireArguments()).messageTypeKey
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MessageDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        init()
        val binding = DialogCheckBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dialog = this

        when(messageType){
            MessageType.ADDED_SUCCESS ->{
                binding.addSuccess.visibility = View.VISIBLE
                message = getString(R.string.addto_success)
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({ this.dismiss() }, 3000)
    }

    private fun init() {
        when (messageType) {
            MessageType.LOGIN_SUCCESS -> {
//                iconRes = StylishApplication.instance.getDrawable(R.drawable.ic_success)
//                message = getString(R.string.login_success)
            }
            MessageType.ADDED_SUCCESS -> {
                iconRes = FamilyTreeApplication.INSTANCE.assets.open("confirm.json")
                message = getString(R.string.addto_success)
            }
            MessageType.MESSAGE -> {
//                iconRes = StylishApplication.instance.getDrawable(R.drawable.ic_launcher_foreground)
//                message = messageType.value.message
            }
            else -> {

            }
        }
    }

    enum class MessageType(val value: Message) {
        LOGIN_SUCCESS(Message()),
        LOGIN_FAIL(Message()),
        ADDED_SUCCESS(Message()),
        MESSAGE(Message())
    }

    interface IMessage {
        var message: String
    }

    class Message : IMessage {
        private var _message = ""
        override var message: String
            get() = _message
            set(value) { _message = value }
    }
}