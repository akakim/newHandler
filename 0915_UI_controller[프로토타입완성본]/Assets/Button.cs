using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;

public class Button : MonoBehaviour {

	public GameObject IntroPanel;

	//PointerDown(When the object is pressed - While you're touching the button)
	//For the button "Forward"
	public void PointerDownForward(){
		gameObject.GetComponent<Renderer> ().enabled = false;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice","1");
	}

	//For the button "Backward"
	public void PointerDownBackward(){
		gameObject.GetComponent<Renderer> ().enabled = false;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "2");
	}

	//For the button "Left"
	public void PointerDownLeft(){
		gameObject.GetComponent<Renderer> ().enabled = false;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "3");
	}

	//For the button "Right"
	public void PointerDownRight(){
		//gameObject.GetComponent<Renderer> ().enabled = false;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "4");
	
	}

	//For the button5
	public void PointerDownButton5(){
		//gameObject.GetComponent<Renderer> ().enabled = false;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "5");
		//AndroidManager.GetInstance().CallJavaFunc2( "button5");	
	
	}
	
	//For the button6
	public void PointerDownButton6(){
		//gameObject.GetComponent<Renderer> ().enabled = false;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "6");
		//AndroidManager.GetInstance().CallJavaFunc2( "button6");

	}
	
	//For the button7
	public void PointerDownButton7(){
		gameObject.GetComponent<Renderer> ().enabled = false;
		//AndroidManager.GetInstance().CallJavaFunc( "javaTestFunc", "7");
		//AndroidManager.GetInstance().CallJavaFunc2( "moveActivity");
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "7");

	}

	//For the button8
	public void PointerDownButton8(){
		gameObject.GetComponent<Renderer> ().enabled = false;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "8");

	}

	//PointerUp(When the object isn't pressed)
	//For the buttons1~8
	public void PointerUpButton(){
		gameObject.GetComponent<Renderer> ().enabled = true;
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "Up");
	}
	



	//For the button "Button9"
	public void ClickButton9(){
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "Up");
	}

	//For the button "Button10"
	public void ClickButton10(){
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "10"); // +

	}
	//For the button "Button11"
	public void ClickButton11(){

		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "11"); // -
	}

	public void ClickButton12(){

		IntroPanel.SetActive(true); 
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "12");
		//여기서 블루투스 통신 목록 다시 새로 가져오도록 할 것

	}


	public void ClickButton13(){
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "13");
	}

	public void ClickButton14(){

	}

	public void ClickButton15(){
		
	}
	public void ClickButton16(){
		
	}
	public void ClickButton17(){
		
	}
	public void ClickButton18(){
		
	}
	public void ClickButton19(){
		
	}
	public void ClickButton20(){
		
	}


	public void ScanButton()
	{	
		AndroidManager.GetInstance().CallJavaFunction( "ViewPairingList");
	}

	public void disconnectButton()
	{
		AndroidManager.GetInstance().CallJavaFunc( "SendToDevice", "disconnect");
	}


	public void onClickSwithingButton(){
		//GameObject IntroPanel = GameObject.Find ("IntroPanel");
		IntroPanel.SetActive(false); 

		//CanvasGroup IntroPanel = GameObject.Find ("IntroPanel").gameObject.GetComponent<CanvasGroup> ();
		//IntroPanel.alpha = 0;
		//IntroPanel.interactable = false;
		//IntroPanel.GetComponent<Renderer> ().enabled = false;

	}







}
