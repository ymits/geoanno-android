package jp.geoanno;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			startService(new Intent(this, GeoService.class));
		} else {
			stopService(new Intent(this, GeoService.class));
		}
	}
    
}
