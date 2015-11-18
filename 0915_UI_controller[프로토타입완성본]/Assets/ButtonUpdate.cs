using UnityEngine;
using System.Collections;
using System.Collections.Generic;


public class ButtonUpdate : MonoBehaviour {

	//public AndroidJavaClass jclass;


	void Update ()
	{
		if (Application.platform == RuntimePlatform.Android) {
			
			if (Input.GetKey (KeyCode.Escape)) {
				Application.Quit ();
				return;
			}
		}
	}


	/*
	void OnGUI()
	{

		if (GUI.Button (new Rect(700, 100, 100, 100), "TestButton") == true)
		{
			AndroidManager.GetInstance().CallJavaFunc( "javaTestFunc", "UnityJavaJarTest" );
		}


		if (GUI.Button (new Rect (200, 100, 100, 100), "Test0930") == true) {

			var androidJC = new AndroidJavaClass ("com.unity3d.player.UnityPlayer");
			var jo = androidJC.GetStatic<AndroidJavaObject> ("currentActivity");
			// Accessing the class to call a static method on it
			var jc = new AndroidJavaClass ("com.example.javaplugin_m08_t19_c23.StartActivity");
			// Calling a Call method to which the current activity is passed
			jc.CallStatic ("Call", jo);
		}



	}
*/

}
