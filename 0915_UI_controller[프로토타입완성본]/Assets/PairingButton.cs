using UnityEngine;
using System.Collections;

public class PairingButton : MonoBehaviour {

	public void ClickPairingButton()
	{
		print ("ClickpairingButton() in Unity");
		string objectName = gameObject.name;
		print ("name: " + name);
		print ("objectName: " + objectName);
		string index = objectName.Remove (0, 13);//PairingButton : 알파벳 13개
		print ("index: " + index);
		AndroidManager.GetInstance ().CallJavaFunc("ConnectPairingDevice",index);
	}

}
