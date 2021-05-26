package team16.easytracker

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import team16.easytracker.database.Contracts
import team16.easytracker.database.DbHelper
import team16.easytracker.utils.Validator
import team16.easytracker.model.Address
import team16.easytracker.model.Worker

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    // TODO: Rename and change types of parameters
    lateinit var etTitleSetting : EditText
    lateinit var etFirstNameSetting : EditText
    lateinit var etLastNameSetting : EditText
    lateinit var etPhonePrefixSetting : EditText
    lateinit var etPhoneNumberSetting : EditText
    lateinit var etPostCodeSetting : EditText
    lateinit var etCitySetting : EditText
    lateinit var etStreetSetting : EditText
    lateinit var etUsernameSetting : EditText

    lateinit var tvErrorGenderSetting : TextView
    lateinit var tvErrorTitleSetting : TextView
    lateinit var tvErrorFirstNameSetting : TextView
    lateinit var tvErrorLastNameSetting : TextView
    lateinit var tvErrorPhonePrefixSetting : TextView
    lateinit var tvErrorPhoneNumberSetting : TextView
    lateinit var tvErrorPostCodeSetting : TextView
    lateinit var tvErrorCitySetting : TextView
    lateinit var tvErrorStreetSetting : TextView
    lateinit var tvErrorUsernameSetting : TextView

    lateinit var spGenderSetting : Spinner

    lateinit var btnSaveChangesSetting : Button
    lateinit var btnChangePassword : Button

    lateinit var currentWorker : Worker
    //lateinit val languageSpinner : Spinner
    lateinit var btnBluetoothSettings: Button
    lateinit var helper: DbHelper



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val languageSpinner = view.findViewById<Spinner>(R.id.spLanguageSetting)
        MyApplication.initLanguageSpinner(languageSpinner, requireActivity())
        etTitleSetting = view.findViewById(R.id.etTitleSetting)
        etFirstNameSetting = view.findViewById(R.id.etFirstNameSetting)
        etLastNameSetting = view.findViewById(R.id.etLastNameSetting)
        etPhonePrefixSetting = view.findViewById(R.id.etPhonePrefixSetting)
        etPhoneNumberSetting = view.findViewById(R.id.etPhoneNumberSetting)
        etPostCodeSetting = view.findViewById(R.id.etPostCodeSetting)
        etCitySetting = view.findViewById(R.id.etCitySetting)
        etStreetSetting = view.findViewById(R.id.etStreetSetting)

        tvErrorGenderSetting = view.findViewById(R.id.tvErrorGenderSetting)
        tvErrorTitleSetting = view.findViewById(R.id.tvErrorTitleSetting)
        tvErrorFirstNameSetting = view.findViewById(R.id.tvErrorFirstNameSetting)
        tvErrorLastNameSetting = view.findViewById(R.id.tvErrorLastNameSetting)
        tvErrorPhonePrefixSetting = view.findViewById(R.id.tvErrorPhonePrefixSetting)
        tvErrorPhoneNumberSetting = view.findViewById(R.id.tvErrorPhoneNumberSetting)
        tvErrorPostCodeSetting = view.findViewById(R.id.tvErrorPostCodeSetting)
        tvErrorCitySetting = view.findViewById(R.id.tvErrorCitySetting)
        tvErrorStreetSetting = view.findViewById(R.id.tvErrorStreetSetting)

        spGenderSetting = view.findViewById(R.id.spGenderSetting)

        btnSaveChangesSetting = view.findViewById(R.id.btnSaveChangesSetting)
        btnChangePassword = view.findViewById(R.id.btnChangePassword)


        btnSaveChangesSetting.setOnClickListener { updateProfile() }
        btnChangePassword.setOnClickListener { changePassword() }
        btnBluetoothSettings = view.findViewById(R.id.btnBluetoothSettings)

        btnBluetoothSettings.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, BluetoothFragment(), "BluetoothFragment")
                    .addToBackStack(null)
                    .commit()
        }


        helper = DbHelper.getInstance()
        val workerId = MyApplication.loggedInWorker!!.getId()
        currentWorker = helper.loadWorker(workerId)!!
        val address : Address? = currentWorker?.addressId?.let { helper.loadAddress(it) }

        etTitleSetting.text = Editable.Factory.getInstance().newEditable(currentWorker?.title)
        etFirstNameSetting.text = Editable.Factory.getInstance().newEditable(currentWorker?.firstName)
        etLastNameSetting.text = Editable.Factory.getInstance().newEditable(currentWorker?.lastName)
        etPhonePrefixSetting.text = Editable.Factory.getInstance().newEditable(currentWorker?.phoneNumber?.substring(0, 1))
        etPhoneNumberSetting.text = Editable.Factory.getInstance().newEditable(currentWorker?.phoneNumber?.substring(2))
        etPostCodeSetting.text = Editable.Factory.getInstance().newEditable(address?.zipCode)
        etCitySetting.text = Editable.Factory.getInstance().newEditable(address?.city)
        etStreetSetting.text = Editable.Factory.getInstance().newEditable(address?.street)
    }

    fun updateProfile() {
        val genderSetting = spGenderSetting.selectedItem.toString()
        val titleSetting = etTitleSetting.text.toString()
        val firstNameSetting = etFirstNameSetting.text.toString()
        val lastNameSetting = etLastNameSetting.text.toString()
        val phonePrefixSetting = etPhonePrefixSetting.text.toString()
        val phoneNumberSetting = etPhoneNumberSetting.text.toString()
        val postCodeSetting = etPostCodeSetting.text.toString()
        val citySetting = etCitySetting.text.toString()
        val streetSetting = etStreetSetting.text.toString()

        val validTitleSetting = validateTitleSetting(titleSetting)
        val validFirstNameSetting = validateFirstNameSetting(firstNameSetting)
        val validLastNameSetting = validateLastNameSetting(lastNameSetting)
        val validPhonePrefixSetting = validatePhonePrefixSetting(phonePrefixSetting)
        val validPhoneNumberSetting = validatePhoneNumberSetting(phoneNumberSetting)
        val validPostCodeSetting = validatePostCodeSetting(postCodeSetting)
        val validCitySetting = validateCitySetting(citySetting)
        val validStreetSetting = validateStreetSetting(streetSetting)

        if(validTitleSetting && validFirstNameSetting && validLastNameSetting && validPhonePrefixSetting && validPhoneNumberSetting
                && validPostCodeSetting && validCitySetting && validStreetSetting)
        {
            val workerId = MyApplication.loggedInWorker!!.getId()
            helper.updateWorker(workerId, firstNameSetting, lastNameSetting,
                    currentWorker.dateOfBirth, titleSetting, currentWorker.email,
                    phonePrefixSetting + phoneNumberSetting, currentWorker.createdAt,
                    helper.saveAddress(streetSetting, postCodeSetting, citySetting))
        }

    }


    fun changePassword()
    {
        val dialog = ChangePasswordDialogFragment()

        val fm = this@ProfileFragment.fragmentManager

        if(fm != null) {
            dialog.show(fm, "changePassword")
        }
    }

    fun validateTitleSetting(titleSetting: String) : Boolean {
        val errorTitleSetting = Validator.validateTitle(titleSetting, resources)
        if (errorTitleSetting != "") {
            tvErrorTitleSetting.text = errorTitleSetting
            tvErrorTitleSetting.visibility = View.VISIBLE
            tvErrorGenderSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateFirstNameSetting(firstNameSetting: String) : Boolean {
        val errorFirstNameSetting = Validator.validateFirstName(firstNameSetting, resources)
        if (errorFirstNameSetting != "") {
            tvErrorFirstNameSetting.text = errorFirstNameSetting
            tvErrorFirstNameSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateLastNameSetting(lastNameSetting: String) : Boolean {
        val errorLastNameSetting = Validator.validateLastName(lastNameSetting, resources)
        if (errorLastNameSetting != "") {
            tvErrorLastNameSetting.text = errorLastNameSetting
            tvErrorLastNameSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validatePhonePrefixSetting(phonePrefixSetting: String) : Boolean {
        val errorPhonePrefixSetting = Validator.validatePhonePrefix(phonePrefixSetting, resources)
        if (errorPhonePrefixSetting != "") {
            tvErrorPhonePrefixSetting.text = errorPhonePrefixSetting
            tvErrorPhonePrefixSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validatePhoneNumberSetting(phoneNumberSetting: String) : Boolean {
        val errorPhoneNumberSetting = Validator.validatePhoneNumber(phoneNumberSetting, resources)
        if (errorPhoneNumberSetting != "") {
            tvErrorPhoneNumberSetting.text = errorPhoneNumberSetting
            tvErrorPhoneNumberSetting.visibility = View.VISIBLE
            tvErrorPhonePrefixSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validatePostCodeSetting(postCodeSetting: String) : Boolean {
        val errorPostCodeSetting = Validator.validatePostCode(postCodeSetting, resources)
        if (errorPostCodeSetting != "") {
            tvErrorPostCodeSetting.text = errorPostCodeSetting
            tvErrorPostCodeSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateCitySetting(citySetting: String) : Boolean {
        val errorCitySetting = Validator.validateCity(citySetting, resources)
        if (errorCitySetting != "") {
            tvErrorCitySetting.text = errorCitySetting
            tvErrorCitySetting.visibility = View.VISIBLE
            tvErrorPostCodeSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

    fun validateStreetSetting(streetSetting: String) : Boolean {
        val errorStreetSetting = Validator.validateStreet(streetSetting, resources)
        if (errorStreetSetting != "") {
            tvErrorStreetSetting.text = errorStreetSetting
            tvErrorStreetSetting.visibility = View.VISIBLE
            return false
        }
        return true
    }

}

class ChangePasswordDialogFragment : DialogFragment() {

    lateinit var etOldPassword : EditText
    lateinit var etNewPassword : EditText
    lateinit var tvErrorOldPassword : TextView
    lateinit var helper: DbHelper

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        etOldPassword = view?.findViewById(R.id.etOldPassword)!!
        etNewPassword = view?.findViewById(R.id.etNewPassword)!!
        tvErrorOldPassword = view?.findViewById(R.id.tvErrorOldPassword)!!

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_change_password, null))
                    // Add action buttons
                    .setPositiveButton(R.string.confirm,
                            DialogInterface.OnClickListener { dialog, id ->
                                if(validatePasswordChange())
                                {
                                    getDialog()?.cancel()
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                getDialog()?.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun validatePasswordChange() : Boolean {
        if(helper.updateWorkerPassword(MyApplication.loggedInWorker!!.getId(), etOldPassword.text.toString(), etNewPassword.text.toString()))
        {
            return true;
        }
        else
        {
            tvErrorOldPassword.text = "You have to enter your Password correctly to change it!"
            tvErrorOldPassword.visibility = View.VISIBLE
            return false;
        }
    }
}