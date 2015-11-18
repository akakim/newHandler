using UnityEngine;
using System.Collections;
using System.IO;
public class CameraWall : MonoBehaviour {

		
	WebCamTexture webcamTexture;
	//public Quaternion basesRotation;
	
	// Use this for initialization
	void Start () {
		
		webcamTexture = new WebCamTexture ();
		GetComponent<Renderer>().material.mainTexture = webcamTexture;
		webcamTexture.requestedWidth = 1280;
		webcamTexture.requestedHeight = 720;
		webcamTexture.Play ();
		
	}

	}