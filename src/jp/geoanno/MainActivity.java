package jp.geoanno;

import java.util.UUID;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends Activity implements OnCheckedChangeListener {
	
	EditText nameText;
	
	Switch noticeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        nameText = (EditText) findViewById(R.id.name_txt);
        noticeSwitch = (Switch) findViewById(R.id.notice_switch);
        noticeSwitch.setOnCheckedChangeListener(this);
        
        SharedPreferences pref = getSharedPreferences("pref",Application.MODE_PRIVATE);
		String name = pref.getString("name", null);
		boolean isChecked = pref.getBoolean("isChecked", false);
		
		this.nameText.setText(name);
		this.noticeSwitch.setChecked(isChecked);
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Intent i = new Intent(this, GeoService.class);
		i.putExtra("accountId", getAccountId());
		i.putExtra("name", this.nameText.getText().toString());
		if(isChecked){
			startService(i);
		} else {
			stopService(i);
		}
		saveState(this.nameText.getText().toString(), isChecked);
	}
	
	private String getAccountId(){
		SharedPreferences pref = getSharedPreferences("pref",Application.MODE_PRIVATE);
		String accountId = pref.getString("accountId", null);
		if(accountId == null){
			accountId = UUID.randomUUID().toString();
			Editor editor = pref.edit();
			editor.putString( "accountId", accountId );
			editor.commit();
		}
		return accountId;
	}
	
	private void saveState(String name, boolean isChecked){
		SharedPreferences pref = getSharedPreferences("pref",Application.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString( "name", name );
		editor.putBoolean("isChecked", isChecked);
		editor.commit();
	}
    
}
