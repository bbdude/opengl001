package com.iigniteus.opengl001;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.MotionEvent;
import com.GuiControl;

/**
 * @author Jacob "Darth" Miller
 *
 */
public class GLRenderer implements Renderer {

	private Square 		square;		// the square
	private Square 		joystick;		// the joystick for movement
	private Square 		button;		// the button for jumping
	private Square      back;
	private Context 	context;
	private Square[]	platforms;
    private vector2[]   platPoint;
	private int 		plats = 12;
    private Text        text;
    private GuiControl  guiControl;

	
	private PointF size         = new PointF();
    private PointF touch        = new PointF();
    private vector2 speed       = new vector2(0,-0.4f);
    private PointF jumpHeight   = new PointF();
	
	private int jump            = 0;
	private boolean grounded    = false;
    private boolean hity        = false,
                    hitx        = false;
	
	/** Constructor to set the handed over context */
	public GLRenderer(Context context) {
		this.context = context;
		
		// initialise the square
		this.back = new Square();
		this.square = new Square();
		this.joystick = new Square();
		this.button = new Square();
		
		platforms = new Square[plats];
        platPoint = new vector2[plats];
		for (int j = 0; j < plats; j++)
		{ platforms[j] = new Square();}

        this.text = new Text();
        this.guiControl = new GuiControl(100,20,0);
		//Log.i("",platforms.toString());
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// clear Screen and Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Reset the Modelview Matrix
		gl.glLoadIdentity();

		// Drawing
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -5.0f);
		gl.glScalef(1.2f, 2.1f, 1);
		gl.glRotatef(-90, 0, 0, 1);
		back.draw(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();

		gl.glTranslatef(square.size.left, square.size.top, -5.0f);		// move 5 units INTO the screen
        //gl.glRotatef(90,0,0,1);												// is the same as moving the camera 5 units away
		gl.glScalef(0.2f, 0.2f, 0.2f);			// scale the square to 50%
		square.draw(gl,square.choice);						// Draw the triangle\
		gl.glPopMatrix();



		gl.glPushMatrix();
		//x max on screen is 1 and min is -1
		//y max on screen is 2 and min is -2
		gl.glTranslatef((float)-(size.x/840.00), (float)(size.y/750.00), -5.0f);
		gl.glScalef(0.45f, 0.45f, 0.45f);			// scale the square to 45% 
		joystick.draw(gl);						// Draw the triangle
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef((float)-(size.x/840.00), (float)-(size.y/750.00), -5.0f);
		gl.glScalef(0.45f, 0.45f, 0.45f);			// scale the square to 45% 
		gl.glRotatef(-90, 0, 0, 1);              //rotate in reverse 90degrees on z
		button.draw(gl);						// Draw the triangle
		gl.glPopMatrix();



		for (int j = 0; j < plats; j++)
		{
			gl.glPushMatrix();
			gl.glRotatef(-90, 0, 0, 1);              //rotate in reverse 90degrees on z
			//gl.glTranslatef(0, -0.4f, -5.0f);
            gl.glTranslatef(platPoint[j].x,platPoint[j].y, -5.0f);
			gl.glScalef(0.45f, 0.45f, 0.45f);			// scale the square to 45% 
			platforms[j].draw(gl);						// Draw the triangle
			gl.glPopMatrix();
		}

        gl.glPushMatrix();
        //gl.glTranslatef(square.size.left, square.size.top, -5.0f);
        gl.glTranslatef(0.5f, 1.25f, -5.0f);		// move 5 units INTO the screen
        gl.glRotatef(-90,0,0,1);												// is the same as moving the camera 5 units away
        gl.glScalef(0.5f, 0.5f, 0.5f);
        //gl.glScalef(0.1f, 0.1f, 0.1f);
        //text.draw(gl);
        guiControl.draw(gl);
        gl.glPopMatrix();

		//Log.i("Size:", square.size.toString());
		update(gl);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
	@SuppressLint("NewApi")
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Load the texture for the square
		Point thing = new Point();
		((Activity) context).getWindowManager().getDefaultDisplay().getSize(thing);
		size.x = thing.x;
		size.y = thing.y;
		int[] textureSqaure = new int[] {R.drawable.quad,R.drawable.leftquad, R.drawable.rightquad,R.drawable.jumpquad};
        square.loadGLTexture(gl,this.context,textureSqaure);
		square.loadGLTexture(gl, this.context,R.drawable.quad);
		joystick.loadGLTexture(gl, this.context,R.drawable.joystickblack);
		button.loadGLTexture(gl, this.context,R.drawable.jumpbuttonblack);
		back.loadGLTexture(gl, this.context,R.drawable.androidback);

		for (int j = 0; j < plats; j++)
		{ platforms[j].loadGLTexture(gl, this.context,R.drawable.platform);platPoint[j] = new vector2(0,-0.4f);}
        //platPoint[1].x = -1.25f;
        platPoint[0] = new vector2(-2.15f,-1f);
        platPoint[1] = new vector2(-1.7f,-1f);
        platPoint[2] = new vector2(-1.25f,-1f);
        platPoint[3] = new vector2(-0.8f,-0.8f);
        platPoint[4] = new vector2(-0.75f,-0.6f);
        platPoint[5] = new vector2(-0.3f,-0.6f);
        platPoint[6] = new vector2(-0f,-0.4f);
        platPoint[7] = new vector2(-0f,-0.2f);
        platPoint[8] = new vector2(-0f,-0f);
        platPoint[9] = new vector2(0.45f,-0.2f);
        platPoint[10] = new vector2(0.9f,-0.2f);
        platPoint[11] = new vector2(1.95f,-1f);

		joystick.size.left = (float) ((size.x/840.00));
		joystick.size.top = (float) (size.y -(size.y/750.00));
		joystick.size.right = joystick.size.left + 200;
		joystick.size.bottom = joystick.size.top - 200;
		
		button.size.left = (float) ((size.x/840.00));
		button.size.top = (float) ((size.y/750.00));
		button.size.right = joystick.size.left + 100;
		button.size.bottom = joystick.size.top - 100;
		
		square.size.set(1, 1.8f, 0.1f, -50);

        //text.loadGl(gl,context);
        //text.loadGLTexture(gl,context);
        guiControl.load(gl,context);
        //text.size.set(1, 1.8f, 0.1f, -50);
		//square.size.s
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 

	}
	public boolean onTouch(MotionEvent e) {

	    //int mActivePointerId = e.getPointerId(0);
	    //int pointerIndex = e.findPointerIndex(mActivePointerId);
        //int action = e.getActionMasked();
        for (int s = e.getPointerCount(), i = 0; i < s; i++) {

        if (e.getX(i) >= 0 && e.getX(i) <= (size.x/2) && e.getY(i) >= 0 && e.getY(i) <= (size.y/2))
        {
            if (e.getY(i) > touch.y)
            {
                speed.x -= 0.05f;
                speed.x -= 0.05f;
            }
            else if (e.getY(i) < touch.y)
            {
                speed.x += 0.05f;
                speed.x += 0.05f;
            }
        }
		/*if (e.getX(i) >= 0 && e.getX(i) <= (size.x/3.6) && e.getY(i) >= 0 && e.getY(i) <= (size.y/6.4))
		{
			if (e.getY(i) > touch.y)
			{
				speed.x -= 0.05f;
				speed.x -= 0.05f;
			}
			else if (e.getY(i) < touch.y)
			{
				speed.x += 0.05f;
				speed.x += 0.05f;
			}
		}*/
		if (e.getX(i) >= 0 && e.getX(i) <= (size.x/3.6) && e.getY(i) <= size.y && e.getY(i) >= size.y - (size.y/6.4) && (jump == 2 || grounded))
		{
			Log.i("Info", "JUMP");
			jump = 1;
			grounded = false;
			jumpHeight = new PointF(square.size.left + 1f,0);
		}
        }
		

		touch.set(e.getX(), e.getY());
		//Log.i("x", e.toString());
		
		return true;
	}
	
	public void update(GL10 gl)
	{
		/********************Collision*********************/
        for (int j = 0; j < plats; j++) {
            detectPlatformCol(new vector2(platPoint[j]));
        }
        /*****************EndCollision*********************/
        /********************JumpCode**********************/
        if (square.size.left <= jumpHeight.x && jump == 1)
            speed.y += 0.05f;
        else if (jump == 1)
            jump = 0;
        else if (square.size.left > -0.9f && !grounded)
        {
            speed.y -= 0.05f;
            grounded = false;
        }
        else
            jump = 2;
        /********************EndJumpCode*******************/

        /********************TestLifeCode*******************/
        if (square.size.left < -0.85)
            guiControl.setHealth(guiControl.getHealth() - 1);
        /******************EndTestLifeCode*******************/

        /*****************ResetVar*************************/
        if (!hity)
        {
            square.size.left += speed.y;
            square.size.right += speed.y;
            //grounded = false;
        }
        else
        {
            jumpHeight = new PointF(square.size.left - 1,0);
            square.size.left -= speed.y/8;
            square.size.right -= speed.y/8;
        }
        if (!hitx)
        {
            square.size.top += speed.x;
            square.size.bottom += speed.x;
        }

        hity = false;
        hitx = false;
        grounded = false;

        if (square.size.left <= jumpHeight.x && jump == 1)
            square.choice = 0;
        //else if (speed.x > 0)
        //    square.choice = 2;
        //else if (speed.x < 0)
        //    square.choice = 3;
        else
        square.choice = 0;

		speed = new vector2(0,0);
        /*****************EndResetVar**********************/
        guiControl.update(gl,context);
	}

    void detectPlatformCol(vector2 point)
    {
        point.x *= -1;
        point.y -= -0.4f;
        RectF colS = new  RectF(square.size.left + speed.y,square.size.top - 0.25f,square.size.left + speed.y + 0.01f,square.size.top + 0.01f);
        /******************************bottom********************right********************top********************left**/
        RectF platl = new RectF((float)-0.685 + point.y,(float) -0.3 + point.x,(float) -0.65 + point.y,(float) 0.25 + point.x);
        RectF platr = new RectF((float)-0.685 + point.y,(float) -0.3 + point.x,(float) -0.15 + point.y,(float) 0.25 + point.x);
        if (colS.intersect(platl))
        {
            jumpHeight = new PointF(square.size.left,0);
            square.size.left -= speed.y/8;
            square.size.right -= speed.y/8;
            hity = true;
        }
        else if (colS.intersect(platr))
        {
            grounded = true;
        }

        colS = new  RectF(square.size.left,square.size.top - 0.25f + speed.x,square.size.left + 0.01f,square.size.top + 0.01f + speed.x);
        /******************************bottom********************right********************top********************left**/
        platl = new RectF((float)-0.68 + point.y,(float) 0.24 + point.x,(float) -0.2 + point.y,(float) 0.25 + point.x);
        platr = new RectF((float)-0.68 + point.y,(float) -0.3 + point.x,(float) -0.2 + point.y,(float) -0.29 + point.x);

        if (colS.intersect(platl) || colS.intersect(platr))
        {
            square.size.top -= speed.x/8;
            square.size.bottom -= speed.x/8;
            hitx = true;
        }

    }

}
