package com.example.wifisignal;





import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;










import java.util.Timer;
import java.util.TimerTask;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	 private WifiManager mainWifi;
	 private WifiAdmin mWifiAdmin; 
	 private WifiAdmin mWifiAdmin1; 
	 private List<ScanResult> list; 
	 private List<ScanResult> list1; 
	 Map<String,String> store=new HashMap<String,String>();
	 Map<String,String> store1=new HashMap<String,String>();
	 Button start;	
	 Button end;
	 Button search;
	 Button stop;
	 EditText editText1;
	 EditText editText2;
	 EditText editText3;
	 String x;
     String y;
	 boolean isStop=false;
	 int i=0;
	 int m=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
		start = (Button)findViewById(R.id.button1);
		start.setOnClickListener(new StartClickListener());
		end = (Button)findViewById(R.id.button2);
        end.setOnClickListener(new EndClickListener());      
        search =(Button)findViewById(R.id.button3);
		search.setOnClickListener(new SendClickListener());
		stop =(Button)findViewById(R.id.button4);
		stop.setOnClickListener(new StopClickListener());
		editText1=(EditText)findViewById(R.id.editText1);
		editText2=(EditText)findViewById(R.id.editText2);
		editText3=(EditText)findViewById(R.id.editText3);
	}
	Handler handler = new Handler();
    //Ҫ��handler��������߳̿���ʹ��runnable�ӿڣ������ȶ���ýӿ�
    //�߳������иýӿڵ�run����
    Runnable update_thread = new Runnable()
    {
        public void run()
        {
        	mWifiAdmin = new WifiAdmin(MainActivity.this); 			
        	try {	 			 
                /*ɨ��wifi�ź��Լ�����Ԥ����     */  	        		
        	        mWifiAdmin.startScan();      
				    list=mWifiAdmin.getWifiList(); 
			   
				    String file_name="x"+editText1.getText().toString()+"y"+editText2.getText().toString()+editText3.getText().toString();
				    FileOutputStream fos1;
				    File file2=new File(Environment.getExternalStorageDirectory()+File.separator+"sampling"+File.separator+file_name);
				    if(!file2.exists()){
					  file2.mkdirs();
				    }
	      	       File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+"sampling"+File.separator+file_name+File.separator+i+".txt" );
	      	       if(!file1.exists()) {   
                    try {
					file1.createNewFile();
					fos1 = new FileOutputStream(file1);
					String  a="x   "+editText1.getText().toString()+"    y     "+editText2.getText().toString()+"    ";
					for(ScanResult value:list)
			      	  {		 
					 store.put(value.BSSID, String.valueOf(value.SSID+"  "+value.level));     			      	
			      	  }	
					Iterator<Entry<String, String>> iter = store.entrySet().iterator(); 
					while(iter.hasNext()){
						Map.Entry<String,String> entry = (Map.Entry<String, String>)iter.next();
						 a+="  "+entry.getKey()+"   "+entry.getValue();			
					}
					fos1.write(a.getBytes()); 
	                store.clear();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}                                     //�����ļ�    ����ͼƬ�Լ�ͼƬ��Ϣ
               
         
      	 }
	      	i++;	      		                           
        }catch(Exception e){
  			   
  		   }
        	if(i<300){
        	 handler.postDelayed(update_thread,200); 
        	}else{
        	 handler.removeCallbacks(update_thread);
   	          start.setEnabled(true);
   	          i=0;	//��ʱ1s���ֽ��̼߳��뵽�̶߳�����
        	}
        }
    };

	 private class StartClickListener implements OnClickListener
	    {
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            //���߳̽ӿ������͵��̶߳�����
	        //  handler.post(update_thread);
	          handler.postDelayed(update_thread, 15000);
	          start.setEnabled(false);
	    }
	     
	    }
	 private class EndClickListener implements OnClickListener
	    {

	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            //���ӿڴ��̶߳������Ƴ�
	          handler.removeCallbacks(update_thread);
	          start.setEnabled(true);
	          i=0;
	        }
	        
	    }
	 
	 private class SendClickListener implements OnClickListener
	    {

	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	 handler1.post(locate_thread1);
	        	 search.setEnabled(false);
	       
	        }
	        
	    }
	 
	 private class StopClickListener implements OnClickListener
	    {

	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        	handler1.removeCallbacks(locate_thread1);
	        	 search.setEnabled(false);
	        	m=0;
	            isStop=true;
	        }
	        
	    }
	 Handler handler1 = new Handler();
	 Runnable locate_thread1 = new Runnable()
	    {
	        public void run()
	        {
	        	mWifiAdmin = new WifiAdmin(MainActivity.this); 			
	        	try {	 			 
	                /*ɨ��wifi�ź��Լ�����Ԥ����     */  	        		
	        	        mWifiAdmin.startScan();      
					    list=mWifiAdmin.getWifiList(); 
				   
					    String file_name="x"+editText1.getText().toString()+"y"+editText2.getText().toString()+editText3.getText().toString();
					    FileOutputStream fos1;
					    File file2=new File(Environment.getExternalStorageDirectory()+File.separator+"locating"+File.separator+file_name);
					    if(!file2.exists()){
						  file2.mkdirs();
					    }
		      	       File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+"locating"+File.separator+file_name+File.separator+m+".txt" );
		      	       if(!file1.exists()) {   
	                    try {
						file1.createNewFile();
						fos1 = new FileOutputStream(file1);
						String  a="x   "+editText1.getText().toString()+"    y     "+editText2.getText().toString()+"    ";
						for(ScanResult value:list)
				      	  {		 
						 store.put(value.BSSID, String.valueOf(value.SSID+"  "+value.level));     			      	
				      	  }	
						Iterator<Entry<String, String>> iter = store.entrySet().iterator(); 
						while(iter.hasNext()){
							Map.Entry<String,String> entry = (Map.Entry<String, String>)iter.next();
							 a+="  "+entry.getKey()+"   "+entry.getValue();			
						}
						fos1.write(a.getBytes()); 
		                store.clear();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                                     //�����ļ�    ����ͼƬ�Լ�ͼƬ��Ϣ
	               
	         
	      	 }
		      	m++;	      		                           
	        }catch(Exception e){
	  			   
	  		   }
	        	if(m<300){
	        	 handler.postDelayed(locate_thread1,200);       //��ʱ1s���ֽ��̼߳��뵽�̶߳�����
	        	}else{
	        		handler1.removeCallbacks(locate_thread1);
		        	search.setEnabled(true);
		        	m=0;
		            isStop=true;
	        	}
	        }
	    };

	 Runnable locate_thread = new Runnable(){
		 public void run(){
			// while(!isStop)
			 try {
			    String file_name1="x"+editText1.getText().toString()+"y"+editText2.getText().toString()+editText3.getText().toString();
	        	File file2=new File(Environment.getExternalStorageDirectory()+File.separator+"locating"+file_name1);
			    if(!file2.exists()){
				  file2.mkdirs();
			    }		
				
					    mWifiAdmin = new WifiAdmin(MainActivity.this); 						        			       
				        long endTime = System.currentTimeMillis()+2000;
				        int flag=0;
				    //    while(System.currentTimeMillis()<endTime){
				        	 synchronized(this){
				        		  try{
				        			  //Thread.sleep(200);
					    			  mWifiAdmin.startScan();      
					    			  list=mWifiAdmin.getWifiList();
					    			  for(ScanResult temp:list){
					    				  if(store.containsKey(temp.BSSID))
					    					{
					    					
					    						store.put(temp.BSSID, store.get(temp.BSSID)+"  "+(temp.level+"_"+temp.SSID));		
					    					
					    					}else
					    					{
					    						store.put(temp.BSSID, ""+(temp.level+"_"+temp.SSID));
					    					}
					    			  }
					    			  }catch(Exception e){
						    			   
						    		   }
						    	   }
				        	//	  }
				         PreProcess process=new PreProcess();  
					     List<Double> list_in=new ArrayList();
 					     Map<String,String> result=new HashMap<String,String>();
						 Iterator<java.util.Map.Entry<String, String>> iter = store.entrySet().iterator();
						 FileOutputStream fos1;
						 while(iter.hasNext()){ 							 
					      	  Map.Entry<String,String> entry = (Map.Entry<String,String>)iter.next();
					      	  String [] data_str =entry.getValue().split("\\s+");			      	 
					      	for(String value:data_str)
					      	  {
					      		list_in.add(Double.parseDouble(value)); 
					      	
					      	  }			      	
					     	  double[] result_one = process.Roman(list_in);
					      	  double result_two = process.average(result_one);			      	 		   		      	
					      	  result.put(entry.getKey(),Double.toString(result_two));					
					      	  list_in.clear();
					      	  
				        	 }
						    File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+file_name1+File.separator+m+".txt" );
					      	if(!file1.exists()) {  
		       				     file1.createNewFile();
							     fos1 = new FileOutputStream(file1);
							 
					      	 for(Map.Entry<String, String>entry1: result.entrySet()) {                           		
		            			 double dd= Double.parseDouble(entry1.getValue().toString())  ;           			 	            			 
								    String input=entry1.getKey()+"    "+dd+"    ";
								    fos1.write(input.getBytes());  
								     
		            			 }
					      	fos1.close();
					      	store.clear();
		            		 }
				  	      	 m++;
					      	 
		 } catch (IOException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			         }
				 handler1.postDelayed(locate_thread, 200);       //��ʱ1s���ֽ��̼߳��뵽�̶߳�����
		 } 
	 };

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			exitBy2click();
		}
		return super.onKeyDown(keyCode, event);
	}
	private static Boolean isExit=false;
   private void exitBy2click(){
	   Timer tExit=null;
	   if(isExit==false){
		   isExit=true;
		   Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
		   tExit=new Timer();
		   tExit.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isExit=false;
			}
			   
		   }, 2000);
	   }else{
		   finish();
		   System.exit(0);
	   }
	   
   }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	 
}
