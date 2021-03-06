package it.brunorenz.mybank.mybankconfiguration.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceViewHolder;

import it.brunorenz.mybank.mybankconfiguration.MyBankIntents;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.httpservice.MyBankServerManager;

public class ButtonLogonPasswordPreference extends EditTextPreference {

    private static final String TAG =
            ButtonLogonPasswordPreference.class.getSimpleName();
    public ButtonLogonPasswordPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ButtonLogonPasswordPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonLogonPasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonLogonPasswordPreference(Context context) {
        super(context);
    }
    public void onClickPressed()
    {
        MyBankServerManager server = MyBankServerManager.createMyBankServerManager(getContext());
        /*
        LogonRequest req = new LogonRequest();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        req.setEmail(pref.getString(getContext().getString(R.string.PRE_LOGON_EMAIL),""));
        req.setPassword(pref.getString(getContext().getString(R.string.PRE_LOGON_PWD),""));

         */
        server.logon(null, MyBankIntents.DATA_LOGON_RESPONSE);
    }
/*
    @Override
    public void setOnBindEditTextListener(@Nullable OnBindEditTextListener onBindEditTextListener) {
        new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            }
        };
    }
*/
    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Button btn = (Button) holder.findViewById(R.id.resetButton);
        if (btn != null)
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPressed();
                }
            });
        }
    }

}
