package com.iigniteus.opengl001;

import java.util.List;

import junit.framework.Test;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	/** The OpenGL view */
	private GLSurfaceView glSurfaceView;
    private GLRenderer glrend = new GLRenderer(this);
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initiate the Open GL view and
        // create an instance with this activity
        glSurfaceView = new GLSurfaceView(this);
        
        // set our renderer to be the main renderer with
        // the current activity context
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {               
            public boolean onTouch(View v, MotionEvent e) {             
                glrend.onTouch(e);
                return true;
            }
        });
        glSurfaceView.setRenderer(glrend);
        setContentView(glSurfaceView);
        
        
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 100 / 100.0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
    }

	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    switch(item.getItemId())
         {
         case  R.id.item1:
         Log.d("ChangeScheme", "Selected : ChangeScheme Option");

         startActivity(new Intent(MainActivity.this, LoginActivity.class));

          return true;
         case  R.id.action_settings:
         Log.d("ChangeScheme", "Selected : ChangeScheme Option");

         startActivity(new Intent(MainActivity.this, SettingsActivity.class));

          return true;
         }
	return false;

    }
    
    public boolean isForeground(String PackageName)
    {
    	ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    	
    	List<ActivityManager.RunningTaskInfo> task  = manager.getRunningTasks(1);
    	
    	ComponentName componentInfo = task.get(0).topActivity;
    	
    	if (componentInfo.getPackageName().equals(PackageName)) return true;
    	return false;
    }
}
/*
public class example extends Activity {
	static boolean isActive = false;

	@Override
	protected void onStart() {
		super.onStart();
		isActive = true;
	}
	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
    }
}

*/