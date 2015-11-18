using UnityEngine;
using System.Collections;

public class Joystick : MonoBehaviour {

	Vector3 touch;
	// Update is called once per frame
	void Update () {

		if (Input.touchCount > 0) {
			//get the position when user controll the joystick
			touch = Input.mousePosition;

			//float X for if()
			float posX = touch.x;
			int tempX = (int)((posX*0.625-143.7)); //in unity x is from 70 to 390, in Android x is from -100 to 100.
			//string X for sending to Android
			string joyX = tempX.ToString ();

			//float Y for if()
			float posY = touch.y;
			int tempY = (int)((posY*0.625-143.7)); //in unity y is from 70 to 390, in Android y is from -100 to 100.
			//string Y for sending to Android
			string joyY = tempY.ToString (); 

	
			if((tempX > -100 && tempX < 100) && (tempY > -100 && tempY < 100))
		    {

				print ("touch: mousePosition" + touch);

				// -10 <joyX < 0
				if(tempX > -10 && tempX < 0)
				{
					joyX =  "-0" + (tempX*-1).ToString ();
				}
				// 0 < joyX < 10
				else if(tempX < 10 && tempX >= 0)
				{
					joyX = "0" + joyX;
				}

				// -10 <joyY < 0
				if(tempY > -10 && tempY < 0)
				{
					joyY =  "-0" + (tempY*-1).ToString ();
				}
				// 0 < joyY < 10
				else if(tempY < 10 && tempY >= 0)
				{
					joyY = "0" + joyY;
				}


				if(tempX >= 0)
					joyX = "+"+joyX;

				if(tempY >= 0) 
					joyY = "+"+joyY;

				
				print ("joystick x: " + joyX);  
				print ("joystick y: " + joyY); 

				string strPosition = "#" + joyX + joyY + "@";
				print ("strPosition: " + strPosition);

				AndroidManager.GetInstance().CallJavaFunc( "JoyStickControl", strPosition);
			}

		}
	

	}

}