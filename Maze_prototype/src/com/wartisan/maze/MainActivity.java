package com.wartisan.maze;

import com.wartisan.maze.R;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MazeView myMazeView;
	private static final int TOGGLE_SOUND = 1;
	private boolean soundEnabled = true;
	public static final String PREFERENCES_NAME = "MyPreferences";
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					 WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        myMazeView = (MazeView) findViewById(R.id.mazeview);
        myMazeView.setKeepScreenOn(true);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int ht = displaymetrics.heightPixels;
        int wt = displaymetrics.widthPixels; 
        myMazeView.setCanvasSize(wt,ht);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		DatabaseAdapter db = new DatabaseAdapter(this);
		try {
        	db.open(); 
        } catch(SQLException sqle){ 
        	System.err.println("error open db");
        	throw sqle;
        }
        Cursor c = db.getRecord(1);        
        startManagingCursor(c);
        if (c.moveToFirst())
        {
            do {
            	soundEnabled = Boolean.parseBoolean((c.getString(1)));	            	
            } while (c.moveToNext());
        }
        db.close();
		//SharedPreferences settings = getSharedPreferences(PREFERENCES_NAME, 0);
		//soundEnabled = settings.getBoolean("soundSetting", true);
		/*
		try {
			readXML();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		myMazeView.soundOn = soundEnabled;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		MenuItem toggleSound = menu.add(0, TOGGLE_SOUND,
//			0, "Toggle Sound");
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
		if (id == R.id.action_settings) {
			String soundText = "Sound on";
			if (soundEnabled) {
				soundEnabled = false;
				soundText = "Sound off";
			} else {
				soundEnabled = true;
			}
			
			Toast.makeText(this,  soundText,  Toast.LENGTH_SHORT).show();
		}
		return false;
	}
}
