package jp.geoanno;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GeoService extends Service implements LocationListener{
	
	private LocationManager locationManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        // ロケーションマネージャのインスタンスを取得する
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void onDestroy() {
        Toast.makeText(this, "位置情報の更新 終了", Toast.LENGTH_SHORT).show();
        locationManager.removeUpdates(this);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "位置情報の更新 開始", Toast.LENGTH_SHORT).show();
        
     // 位置情報の更新を受け取るように設定
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // プロバイダ
        0, // 通知のための最小時間間隔
        0, // 通知のための最小距離間隔
        this); // 位置情報リスナー
        return START_STICKY;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	
}
