package com.iigniteus.opengl001;

public class vector2 {
	public float x,y;
	public vector2()
	{
		x = 0;
		y = 0;
	}
    vector2(float changeX, float changeY)
    {
        x = changeX;
        y = changeY;
    }
    vector2(vector2 that)
    {
        x = that.x;
        y = that.y;
    }

	//Maths
	float magnitude()
	{
		return (float)Math.sqrt((Math.pow(x,2) + Math.pow(y,2)));
	}
	float dotProduct(vector2 change)
	{
		return ((x*change.x) + (y*change.y));
	}
	float crossProduct(vector2 change)
	{
		return x * change.y - y * change.x;
	}
	float eulerAngle(vector2 change)
	{
		//A.B = |A| *|B| * Cos(theta)
		// theta =ArcCos( A.B/|AB|)
		vector2 temp = new vector2(x,y);
		float theta = (float) Math.acos(dotProduct(change) / temp.magnitude());
		temp.x = x;
		temp.y = y;
		return (float) (temp.magnitude() * change.magnitude() * Math.cos(theta));
	}

	void normalise()
	{
		x = x/magnitude();
		y = y/magnitude();
	}

	vector2 getNormal()
	{
		vector2 temp = new vector2(x/magnitude(),y/magnitude());
		return temp;
	}
	vector2 linearInterp(float alpha,vector2 change)
	{
		//(1-alpha)Av + alphaBv
		alpha = 1- alpha;
		vector2 returnVector = new vector2((this.x * alpha) + (change.x * alpha),(this.y * alpha) + (change.y * alpha));
		return returnVector;
	}
}
