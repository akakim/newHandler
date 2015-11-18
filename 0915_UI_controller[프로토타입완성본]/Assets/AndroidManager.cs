using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class AndroidManager : MonoBehaviour
{
	private AndroidJavaObject curActivity;
	private AndroidJavaClass jc;
	public int j = 0;
	public int k = 0;

	//the buttons in the paired list
	public GameObject [] PairedButton = new GameObject[20];
	//the buttons in the pairing list
	public GameObject [] PairingButton = new GameObject[20];

	static AndroidManager _instance;
	public static AndroidManager GetInstance()
	{
		if( _instance == null )
		{
			_instance = new GameObject("AndroidManager").AddComponent<AndroidManager>();   
		}
		return _instance;
	}
	void Awake()
	{
		///현재 활성화된 액티비티 얻어와서 저장
		jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		curActivity = jc.GetStatic<AndroidJavaObject>("currentActivity");

		print ("Unity Awake");

		//Set the pairedButtons invisible
		for (int i=0; i<20; i++)
			PairedButton [i].SetActive (false);

		//Set the pairingButtons invisible
		for (int i=0; i<20; i++)
			PairingButton [i].SetActive (false);

		AndroidManager.GetInstance ().CallJavaFunction ("BluetoothDialog");
		AndroidManager.GetInstance ().CallJavaFunction ("ViewParingList");
		Debug.Log ("BluetoothDialog called");
	}

	void Start(){

	}

	void Update(){
		AndroidManager.GetInstance ().CallJavaFunction ("sendPairingID");
	}


	//strFuncName: 자바의 함수 이름. strTemp: 자바에 전달할 문자열
	public void CallJavaFunc( string strFuncName, string strTemp )
	{
		if( curActivity == null )
			return;
		//액티비티안의 자바 메소드 호출
		curActivity.Call( strFuncName, strTemp);
	}


	//strFuncName: 자바의 함수 이름.
	public void CallJavaFunction( string strFuncName)
	{
		if( curActivity == null )
			return;
		//액티비티안의 자바 메소드 호출
		curActivity.Call(strFuncName);
	}

	public void call()

	{
		curActivity.Call ("Launch");
	}


	//From Android, SetPairedList(string) get the IDs which have paired before and set the PairedList with them.
	void SetPairedList(string pairedID)
	{
		print ("!!!!!!!Unity-SetPairedList(string)!!!!!!");
		print ("strJavaLog: " + pairedID);

		PairedButton [j-1].SetActive (true);
		Text pairedButtonText = PairedButton [j-1].transform.FindChild("Text").gameObject.GetComponent<Text> ();
		pairedButtonText.text = pairedID;
		j++;
	
		AndroidManager.GetInstance ().CallJavaFunction ("sendPairedID");
		
	}

	//From Android, SetPairingList(string) get the IDs which have paired before and set the PairingList with them.
	void SetPairingList(string paringID)
	{
		print ("!!!!!!!Unity-SetPairingList(string)!!!!!!");
		print ("strJavaLog: " + paringID);
		
		PairingButton [k].SetActive (true);
		Text pairingButtonText = PairingButton [k].transform.FindChild("Text").gameObject.GetComponent<Text> ();
		pairingButtonText.text = paringID;
		k++;

		AndroidManager.GetInstance ().CallJavaFunction ("sendPairingID");
	}
	
	//To finish application
	void finishApp(string strJava)
	{
		System.Diagnostics.Process.GetCurrentProcess ().Kill ();
	}








}