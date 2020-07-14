package it.brunorenz.mybank.mybankconfiguration;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingFragment extends PreferenceFragmentCompat {


    private static final String TAG =
            SettingFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        setTypeClassNumber((EditTextPreference) findPreference(getString(R.string.PRE_CONN_TIMEOUT)));
        setTypeClassNumber((EditTextPreference) findPreference(getString(R.string.PRE_READ_TIMEOUT)));
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