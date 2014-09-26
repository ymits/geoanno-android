package jp.geoanno;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class GeoService extends Service implements LocationListener{
	
	private LocationManager locationManager;
	
	private DefaultHttpClient httpClient;
	
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private static final int INTERVAL = 30 * 1000;
	
	private String accountId = null;
	private String name = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.httpClient = new DefaultHttpClient();
	}

	@Override
	public void onDestroy() {
        Toast.makeText(this, "位置情報の更新 終了", Toast.LENGTH_SHORT).show();
        locationManager.removeUpdates(this);
        locationManager = null;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.accountId = intent.getStringExtra("accountId");
		this.name = intent.getStringExtra("name");
		
		if(locationManager == null){
	        Toast.makeText(this, "位置情報の更新 開始 :"+this.name, Toast.LENGTH_SHORT).show();
	        // ロケーションマネージャのインスタンスを取得する
	        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        
	        // 位置情報の更新を受け取るように設定
	        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	        	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        	noticeLocation(location);
	        	
	            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // プロバイダ
	            INTERVAL, // 通知のための最小時間間隔
	            10, // 通知のための最小距離間隔
	            this); // 位置情報リスナー
	        } else {
	        	Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	        	noticeLocation(location);
	        	
	        	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // プロバイダ
	        	INTERVAL, // 通知のための最小時間間隔
	            10, // 通知のための最小距離間隔
	            this); // 位置情報リスナー
	        }
		}
        
        return START_REDELIVER_INTENT;
	}

	@Override
	public void onLocationChanged(Location location) {
		noticeLocation(location);
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

	private void noticeLocation(final Location location){
		if(location == null){
			return;
		}
		this.executor.execute(new Runnable() {
			
			@Override
			public void run() {
				//Log.d("location", "accountId:"+GeoService.this.accountId+" name:"+GeoService.this.name+" lat:"+location.getLatitude()+" lon:"+location.getLongitude());
				JSONObject jsonObj = new JSONObject();
				try {
					jsonObj.put("accountId", GeoService.this.accountId);
					jsonObj.put("name", GeoService.this.name);
					jsonObj.put("updateTime", location.getTime());
					
					JSONObject position = new JSONObject();
					position.put("latitude", location.getLatitude());
					position.put("longitude", location.getLongitude());
					
					jsonObj.put("position", position);
					
					HttpPost request = new HttpPost("http://geoanno.herokuapp.com/currentPosition");
			        StringEntity body = new StringEntity(jsonObj.toString(), "UTF-8");
			        request.addHeader("Content-type", "application/json;charset=UTF-8");
			        request.setEntity(body);
			        //Log.d("http", body.toString());
			         
			        httpClient.execute(request, new ResponseHandler<String>(){
			            public String handleResponse(HttpResponse response) throws IOException{
			                switch(response.getStatusLine().getStatusCode()){
			                case HttpStatus.SC_OK:
			                    //Log.i("http","OK");
			                    return null;
			                case HttpStatus.SC_NOT_FOUND:
			                	//Log.i("http","404");
			                    return null;
			                default:
			                	//Log.i("http","NG");
			                    return null;
			                }
			            }
			        });
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
}
