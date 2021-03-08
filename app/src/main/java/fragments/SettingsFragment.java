package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.quote.R;

import databases.RoomDB;

public class SettingsFragment extends PreferenceFragmentCompat {

    SharedPreferences preferences;

    String username;
    String language;
    String http;
    String db;

    EditTextPreference etp_username;
    ListPreference lp_languages;
    ListPreference lp_http;
    ListPreference lp_db;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);

        preferences = getPreferenceManager().getSharedPreferences();
        Log.d("preferences",preferences.getAll().toString());

        etp_username = findPreference("username");
        lp_languages = findPreference("languages");
        lp_http = findPreference("http");
        lp_db = findPreference("db");

        setSummaries();

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(prefs.getString("db","0") == "1") RoomDB.getInstance(getContext()).destroyInstance();
                Log.d("lang",prefs.getString("languages","0"));
                setSummaries();
            }};

        preferences.registerOnSharedPreferenceChangeListener(listener);

        setHasOptionsMenu(true);
    }

    private void setSummaries() {
        username = preferences.getString("username",getString(R.string.settings_name_info));
        int languageIndex = Integer.parseInt(preferences.getString("languages","0"));
        language = getResources().getStringArray(R.array.languages)[languageIndex];
        int httpIndex = Integer.parseInt(preferences.getString("http","0"));
        http = getResources().getStringArray(R.array.http)[httpIndex];
        int dbIndex = Integer.parseInt(preferences.getString("db","0"));
        db = getResources().getStringArray(R.array.db)[dbIndex];

        if(username != "")
            etp_username.setSummary(username);
        else etp_username.setSummary(R.string.settings_name_info);
        lp_languages.setSummary(language);
        lp_http.setSummary(http);
        lp_db.setSummary(db);
    }
}
