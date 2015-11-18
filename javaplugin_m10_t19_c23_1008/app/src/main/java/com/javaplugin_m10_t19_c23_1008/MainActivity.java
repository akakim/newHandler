package com.javaplugin_m10_t19_c23_1008;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends UnityPlayerActivity {


    public ArrayList<String> pairedIDList = new ArrayList<String>();
    public ArrayList<String> pairingIDList = new ArrayList<String>();

    // 유니티를 위한 변수들 메세지를 한번에 하나씩 보내야만해서 만듬.
    public int pairedIDListSize = 0;
    public int pairingIDListSize = 0;
    public int index_ID = 0;
    public int index_pairingID = 0;
    public int index_Address= 0;
    public int index_pairingAddress= 0;
    public static final int delayTime = 2000;
    public static final int delayRate = 2000;

    // 테스팅을 위함. 테그랑 IS_TEST 변수만 바꾸면 Log에 메세지가 뜸
    private static final String  TAG = "Unity : ";
    private static final boolean IS_TEST = true; // for testing mode

    // 블루투스 통신 관련 변수.
    private              BluetoothAdapter      myBluetoothAdapter = null;
    private              Set<BluetoothDevice>  myBluetoothDevice = null;
    private              BluetoothSocket       btSocket = null;
    private              ConnectBT ForDevice;


    static          final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private               ArrayList pairedlist;
    private               ArrayList pairinglist;

    // UI 관련
    private               ProgressDialog        progress;
    private               Handler               handler;

    //
    private static final int FINISH = 0;
    private static final int BT_ON = 1;
    private static final int BT_CONNECT_TO_DEVICE = 2;
    private static final int BT_DISCOVERY_MODE = 3;
    private static final int BT_DISCONNECTED =4;
    private static final int DISMISS = 5;

    private               Handler               Free_Set_handler;
    // Free Set
    private static final int STOP  = 0;
    private static final int GO    = 1;
    private static final int BACK  = 2;
    private static final int LEFT  = 3;
    private static final int RIGHT = 4;
    private static final int FREESET_GONBACK = 5;

    // Status ??

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(IS_TEST)
            Log.d(TAG, "OnCreate()");

        //showing a message. that the device has no bluetooth adapter
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (myBluetoothAdapter == null) {
            Log.v(TAG, "Bluetooth Error Adapter is unable to use ");
            finish();
        }

        myBluetoothDevice = myBluetoothAdapter.getBondedDevices();

        pairedlist        = new ArrayList();
        pairinglist       = new ArrayList();
        handler           = new InnerHandler(this);
        Free_Set_handler  = new FreeSetHandler(this);
        progress          = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(true);
        if (myBluetoothDevice.size() <= 0) {
            Log.v(TAG,"No paired Device");
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(IS_TEST)
            Log.d(TAG,"onDestroy()");

        // Make sure we're not doing discovery anymore
        if (myBluetoothAdapter != null) {
            myBluetoothAdapter.cancelDiscovery();
        }

        myBluetoothAdapter.disable();
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
        try {
            btSocket.close();
            btSocket = null;
        }catch(Exception e){

        }

    }

    @Override
    public void onStop(){
        super.onStop();

    }

    public void onRestart(){
        super.onRestart();
        BluetoothDialog();

    }

    public void onPause(){
        super.onPause();
        myBluetoothAdapter.disable();
    }

    //sendPairedID() sends the pairedIDList[] to Unity's SetPairedList(str )
    public void sendPairedID(){

        if(IS_TEST){
            Log.v(TAG,"sendPairedID()");
        }

        if(index_ID==pairedIDList.size())
        {
            Log.d(TAG,"index_ID == pairedIDList.size()");
        }
        else
        {
            UnityPlayer.UnitySendMessage("AndroidManager","SetPairedList", pairedIDList.get(index_ID));
            index_ID++;
        }


    }

    //sendPairingID() sends the pairingIDList[] to Unity's SetPairingList(str )
    public void sendPairingID(){

        if(IS_TEST){
            Log.v(TAG,"sendPairingID()");
        }

        if(index_pairingID==pairingIDList.size())
        {

            Log.v(TAG,"index_pairingID == pairingIDList.size()");
            Log.v(TAG,"index_pairingID : " + index_pairingID);
            Log.v(TAG, "pairingIDLIst.size() : " + pairedIDList.size());
        }
        else
        {
            UnityPlayer.UnitySendMessage("AndroidManager","SetPairingList", pairingIDList.get(index_pairingID));
            index_pairingID++;
        }


    }


    public void ViewPairedList(){

        if(IS_TEST)
            Log.d(TAG, "ViewPairedList");

        String tmp = "No Device";

        for(BluetoothDevice bt : myBluetoothDevice){

            tmp = bt.getName();
            pairedIDList.add(tmp);
            pairedlist.add(bt.getAddress());
            if(IS_TEST) {
                Log.d(TAG, "tmp: " + tmp);
                Log.d(TAG, "pairedList[" + index_Address + "]:" + pairedlist.get(index_Address));
            }
            index_Address++;
        }

    }

    public void ViewPairingList(){
        if(IS_TEST){
            Log.v(TAG,"ViewParingList()");
        }


        pairinglist.clear();
        pairingIDListSize = 0;
        pairingIDList.clear();
        index_pairingID = 0;
        index_pairingAddress = 0;
        // If we're already discovering, stop it
        if (this.myBluetoothAdapter.isDiscovering()) {
            this.myBluetoothAdapter.cancelDiscovery();
        }


//        handler.sendEmptyMessage(BT_DISCOVERY_MODE);
        // Request discover from BluetoothAdapter

        this.myBluetoothAdapter.startDiscovery();
        if(IS_TEST){
            int n = myBluetoothAdapter.getState();

            Log.v(TAG,"bluetooth startDiscovering n = "+ n);
        }
    }



    public void ConnectPairedDevice(String indexFromUnity){
        int index = Integer.parseInt(indexFromUnity) - 1; //indexFrom Unity는 버튼 순서 1부터 해당, 인덱스는 0부터. 따라서 -1해줌.
        String address = pairedlist.get(index).toString();
        if(IS_TEST) {
            Log.d(TAG, "ConnectPairedDevice() address: " + address);
        }
        ForDevice = new ConnectBT();
        ForDevice.setMacAddress(address);
        handler.sendEmptyMessage(BT_CONNECT_TO_DEVICE);
        ForDevice.execute();
        //      handler.sendEmptyMessage(DISMISS);
        UnityPlayer.UnitySendMessage("AndroidManager", "switchingPanel", "");
    }

    public void ConnectPairingDevice(String indexFromUnity){

        int index = Integer.parseInt(indexFromUnity) -1 ; //indexFrom Unity는 버튼 순서 1부터 해당, 인덱스는 0부터. 따라서 -1해줌.
        String address = pairinglist.get(index).toString();
        if (IS_TEST) {
            Log.d(TAG, "ConnectPairingDevice() address: " + address);
        }

        ForDevice = new ConnectBT();
        handler.sendEmptyMessage(BT_CONNECT_TO_DEVICE);
        ForDevice.setMacAddress(address);
        ForDevice.execute();
//        handler.sendEmptyMessage(DISMISS);
    }

    public void sendToArduino(String str){
        if(IS_TEST) {
            Log.d(TAG, "sendToArduino():");
            Log.d(TAG, str);
        }
        char [] tmp = str.toCharArray();

        if (btSocket.isConnected()) {
            try {
                for (int i = 0; i < tmp.length ; i++)
                    btSocket.getOutputStream().write(tmp[i]);
            } catch (IOException e) {
                Log.e(TAG, "socket error");
                try {
                    btSocket.close();
                    btSocket = null;
                    Log.e(TAG,"Unexpected error socket is disconnected");
                    Log.d(TAG, "sendToArduino():");
                }
                catch (IOException socketError){
                    Log.e(TAG,socketError.toString());
                }

            }
        }
    }

    public void JoyStickControl(String strPosition)
    {
        // 너무 로그가 길면 에러남 20자 이하로 작성해야함.
        if(IS_TEST)
            Log.d(TAG,"JoyStick Pos :"+strPosition );

        Free_Set_handler.removeCallbacksAndMessages(null);

        char [] tmp = strPosition.toCharArray();
        if (btSocket.isConnected()) {
            try {
                for (int i = 0; i < tmp.length ; i++)
                    btSocket.getOutputStream().write(tmp[i]);
            } catch (IOException e) {
                Log.e(TAG, "socket error");
                try {
                    btSocket.close();
                    btSocket = null;
                    Log.e(TAG,"Unexpected error socket is disconnected");
                    Log.e("JoyStickControl()",e.toString());
                }
                catch (IOException socketError){
                    Log.e(TAG,socketError.toString());
                }

            }
        }
    }

    public void SendToDevice(String MsgFromUnity){
        int msg = Integer.parseInt(MsgFromUnity);
        String strMsg="No Message";
        switch (msg){
            case 0: //stop
                strMsg = "#55@";
                break;

            case 1: // gear up
                strMsg = "+";
                break;

            case 2: //gear down
                strMsg = "-";
                break;

            //   case 3: //preset 1
            //       strMsg = "#59@";

            default:
                strMsg = "default";
        }

        Log.v(TAG, "int :" + msg + "String : " + strMsg);
        try {
            btSocket.getOutputStream().write(strMsg.getBytes());
        }
        catch(IOException e){
            Log.v(TAG,"socket error");
            try{
                btSocket.close();
                btSocket = null;
            }catch(Exception e2){

            }

        }
    }



    public void SelectPreset(String MsgFromUnity){

        int msg = Integer.parseInt(MsgFromUnity);
        String strMsg = "No Message";
        switch (msg) {
            //Preset #1
            case FREESET_GONBACK:
                Free_Set_handler.sendEmptyMessage(FREESET_GONBACK);
                break;
            case 2: //왼쪽으로 돌며 회전
                strMsg = "#1530$";
                break;

            case 3:
                strMsg = "";
                break;

            case 4:
                strMsg = "";
                break;

            case 6:
                strMsg = "";
                break;

            case 7:
                strMsg = "$57155130$";
                break;

            default:
                strMsg = "default";
        }

        Log.v(TAG, "int :" + msg + "String : " + strMsg);
        try {
            btSocket.getOutputStream().write(strMsg.getBytes());
        }
        catch(IOException e){
            Log.v(TAG,"socket error");
            try{
                btSocket.close();
                btSocket = null;
            }catch(Exception e2){

            }

        }
    }



    //TestForDevice
    public void BluetoothDialog(){

        if(!myBluetoothAdapter.isEnabled()){
            Log.v(TAG,"BT_ON");
            handler.sendEmptyMessage(BT_ON);
            myBluetoothAdapter.enable();
        }
        else {
            Log.v(TAG,"Init..");
            myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            myBluetoothDevice = myBluetoothAdapter.getBondedDevices();

            ViewPairedList();
            sendPairedID();
        }

    }




    public void quitApplication(){
        handler.sendEmptyMessage(0);
        Log.d("UnityTestTest", "quitApplication() in JAVA");
    }


    public void finishMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("종료하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("종료",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                myBluetoothAdapter.disable();
                UnityPlayer.UnitySendMessage("AndroidManager","finishApp","finish");
                Log.d("UnityTestTest", "유니티 finishapp호출!-종료");
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });
        builder.show();
    }



    public void BluetoothDisconnect(){
        try {
            btSocket.close();
            btSocket = null;

        }
        catch(Exception e){

        }
    }

    public void checkConnect(String MsgUnity){
        if(myBluetoothAdapter.isEnabled() && btSocket.isConnected()){
            //UnityPlayerActivity("AndroidManager", "", "");
        }
    }
    public void setProgressDialogText(String msg){
        progress.setMessage(msg);
        progress.show();
    }

    public void dismissProgressdialog(){
        progress.dismiss();
    }

    public void setFreeSet_GoNBack(int delay){
        // something status setting .

        // K
        Free_Set_handler.sendEmptyMessage(GO);
        Free_Set_handler.sendEmptyMessageDelayed(BACK, delay);
        delay +=delayRate;
        Free_Set_handler.sendEmptyMessageDelayed(STOP,delay);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String tmpForPairing;
            Log.v(TAG,"Bluetooth Status" + action);
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) { //

                    tmpForPairing = device.getName();
                    pairingIDList.add(tmpForPairing);

                    pairinglist.add(device.getAddress());
                    if(IS_TEST) {
                        Log.d(TAG, "paringIDList: [" + index_pairingAddress + "]:" +  pairingIDList.get(index_pairingAddress));
                        Log.d(TAG, "pairingList[" + index_pairingAddress + "]:" + pairinglist.get(index_pairingAddress));
                    }
                    index_pairingAddress++;

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                if (pairedlist.isEmpty() && pairinglist.isEmpty()) {
                    Log.v(TAG,"No Device is searched");
                }

                // using test case .
                int maxOut = pairingIDList.size();
                int maxIn  = pairingIDList.size();
                for (int start = 0; start < maxOut; start++){
                    for(int Next = start +1; Next <maxIn; Next++){
                        if(pairinglist.get(start).equals(pairinglist.get(Next))){

                            index_pairingAddress--;
                            Log.v(TAG,"Equal error");
                            Log.v(TAG,String.valueOf(start)+ " " + pairingIDList.get(Next));
                            Log.v(TAG,String.valueOf(Next)+ " " + pairingIDList.get(Next));
                            pairinglist.remove(Next);
                            pairingIDList.remove(Next);
                            start--;
                            maxOut--;
                            maxIn--;
                            Next--;
                        }
                    }
                }

                sendPairingID();
            }
            else if(myBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON){
                if(IS_TEST){
                    Log.v(TAG,"Bluetooth on now initialize");
                }
                myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                myBluetoothDevice = myBluetoothAdapter.getBondedDevices();
                ViewPairedList();
                sendPairedID();
                handler.sendEmptyMessage(DISMISS);
            }

        }
    };

    private class ConnectBT extends AsyncTask <Void,Void,Void> // UI Thread
    {
        private boolean ConnectSuccess = true; // if it's here, it's almost connected
        private String MAC_address;
        private boolean For_paired_device = true;

        protected void onPreExcute() {
            //              progress = ProgressDialog.show(ledControl.this, "Connectting...", "Please wait!");
            // show a progress dialog
        }

        public void setPaired(boolean f){
            For_paired_device = f;
        }

        public void setMacAddress(String address){
            MAC_address = address;
        }

        protected Void doInBackground(Void... devices) { // while the progress dialog is shown, the connection is done in background
            try{

                if(IS_TEST){
                    if (For_paired_device){
                        Log.v(TAG,"do In Background() for paired");
                    }
                    else {
                        Log.v(TAG,"do In Background() for pairing");
                    }
                }

                if(btSocket == null){

                    BluetoothDevice dispositivo = myBluetoothAdapter.getRemoteDevice(MAC_address); //  98:D3:31:30:1A:53 here is my target MAC Address .
                    //connect to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);

                    //create RFCOMM(SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();

                    handler.sendEmptyMessage(DISMISS);
                }
            }
            catch (IOException e){
                ConnectSuccess = false;
            }
            return null;
        }

        protected void onPostExcute(Void result) // after the doInBackground,it checks if everything went fine
        {
            super.onPostExecute(result);
            if(!ConnectSuccess){
                Log.v(TAG,"Connection Faile");
                finish();
            }
            else{
                Log.v(TAG,"Connected");
            }
        }

    }

    // 프로그래스바 다루는 핸들러
    private static class InnerHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public InnerHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
//                Log.v(TAG,"branch what is message");

                if(msg.what == FINISH){

                    activity.finishMessage();
                }
                else if (msg.what == BT_ON) {
                    activity.setProgressDialogText("Awake Bluetooth");
                    Log.v(TAG, "BT_ON");
                } else if (msg.what == BT_CONNECT_TO_DEVICE) {
                    activity.setProgressDialogText("Connect to Device");
                    Log.v(TAG, "BT_CONNECT_TO_DEVICE");
                } else if (msg.what == BT_DISCOVERY_MODE) {
                    activity.setProgressDialogText("Bluetooth is discovering..");
                    Log.v(TAG, "BT_DISCOVERY_MOD");
                }
                else if (msg.what == BT_DISCONNECTED){
                    activity.setProgressDialogText("connect end");
                }
                else if (msg.what == DISMISS){
                    activity.dismissProgressdialog();
                    Log.v(TAG, "DISMISS");
                }
            }
        }
    }

    private static class FreeSetHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public  FreeSetHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what){
                    case FREESET_GONBACK:
                        activity.setFreeSet_GoNBack(activity.delayTime);
                        break;
                    case GO:
                        activity.sendToArduino("#59@");
                        break;
                    case LEFT:
                        activity.sendToArduino("#05@");
                        break;
                    case RIGHT:
                        activity.sendToArduino("@95#");
                        break;
                    case BACK:
                        activity.sendToArduino("@50#");
                        break;
                    case STOP:
                        activity.sendToArduino("@55#");
                        break;
                }
            }
        }
    }

}
