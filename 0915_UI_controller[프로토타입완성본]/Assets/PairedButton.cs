using UnityEngine;
using System.Collections;

public class PairedButton : MonoBehaviour {

	
	public void ClickPairedButton()
	{
		string objectName = gameObject.name;
		print ("name: " + name);
		print ("objectName: " + objectName);
		string index = objectName.Remove (0, 12); //PairedButton :알파벳 12개
		print ("index: " + index);
		AndroidManager.GetInstance ().CallJavaFunc("ConnectPairedDevice",index);
	}
	

}
