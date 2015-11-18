using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class Controller : MonoBehaviour {
	public Image Stick;
	private Vector3 originPos = Vector3.zero;

	float StickRadius = 0;


	void Start()
	{
		originPos = Stick.transform.position;
		StickRadius = Stick.rectTransform.sizeDelta.x*3;
	}

	public void OnDrag()
	{
		print ("터치 중!");
		if (Stick == null)
			return;

		Touch touch = Input.GetTouch (0);


		Vector3 dir = (new Vector3 (touch.position.x, touch.position.y, originPos.z)-originPos).normalized;
		//print ("x: " + touch.position.x + " y: " + touch.position.y + " z: " + originPos.z);
		float touchAreaRadius = Vector3.Distance (originPos, new Vector3 (touch.position.x, touch.position.y, originPos.z));
		//print ("touchAreaRadius: " + touchAreaRadius);

		if (touchAreaRadius > StickRadius)
			Stick.rectTransform.position = originPos + (dir * StickRadius);
		else
			Stick.rectTransform.position = touch.position;


		//AndroidManager.GetInstance().CallJavaFunc( "JoyStickControl", strPosition);

	}

	public void OnEndDrag()
	{
		print ("손 뗐음!");
		AndroidManager.GetInstance().CallJavaFunc("JoyStickControl","#+00+00@");
		if (Stick != null) {
			Stick.rectTransform.position = originPos;
		
		}

	}



}
