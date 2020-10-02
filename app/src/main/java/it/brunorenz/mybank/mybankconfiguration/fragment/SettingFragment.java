package it.brunorenz.mybank.mybankconfiguration.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import it.brunorenz.mybank.mybankconfiguration.R;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private static final String TAG =
            SettingFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        setTypeClassNumber((EditTextPreference) findPreference(getString(R.string.PRE_CONN_TIMEOUT)));
        setTypeClassNumber((EditTextPreference) findPreference(getString(R.string.PRE_READ_TIMEOUT)));
        setTypeClassEmail((EditTextPreference) findPreference(getString(R.string.PRE_LOGON_EMAIL)));


        setTypeClassPassword((ButtonLogonPasswordPreference) findPreference(getString(R.string.PRE_LOGON_PWD)));
    }

    private void setTypeClassNumber(EditTextPreference pref) {
        if (pref != null) {
            pref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }
    }

    private void setTypeClassPassword(EditTextPreference pref) {
        if (pref != null) {
            pref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            });
            pref.setSummaryProvider(new Preference.SummaryProvider() {
                @Override
                public CharSequence provideSummary(Preference preference) {
                    String t = ((EditTextPreference) preference).getText();
                    if (t != null)
                    {
                        StringBuilder sb = new StringBuilder();
                        for (int i =0; i<  t.length(); i++) sb.append("*");
                        return sb.toString();
                    }
                    return "";
                }
            });
        }
    }
    private void setTypeClassEmail(EditTextPreference pref) {
        if (pref != null) {
            pref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
            });
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Object c = savedInstanceState.get("connTimeout");
            Log.d(TAG, "connTimeout = " + c);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        Log.d(TAG, "onPreferenceTreeClick " + preference.toString());
        return super.onPreferenceTreeClick(preference);
    }
}