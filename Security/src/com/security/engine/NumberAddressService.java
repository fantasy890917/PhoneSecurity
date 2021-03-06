package com.security.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.security.R;

public class NumberAddressService {
	
	private static NumberAddressService numberAddressService;
	private Context context;
	
	private static final String PATH = Environment.getExternalStorageDirectory()+ "/Security/data.db";
	
	private static final String DIR_PATH = Environment.getExternalStorageDirectory()+ "/Security";
	private NumberAddressService(){

	}
	
	public static synchronized NumberAddressService getInstance(Context context){
		if(numberAddressService == null){
			numberAddressService = new NumberAddressService();
			numberAddressService.context = context;
		}
		
		return numberAddressService ;
	}
	
	public String getAddress(String number){
		 String pattern = "^1[3458]\\d{9}$";
		 String address = number;
		 
		 if(number.matches(pattern)){ //phone number
			 address = query("select city from info where mobileprefix = ? ",
					 new String[]{number.substring(0, 7)});
			 if(address.equals(""))  
	         {  
	             address = number;  
	         }
		 }else { //固定电话
			 int len = number.length();
			 switch(len){
			 	case 4:
			 		address = "Simulator";
			 		break;
			 	
			 	case 7:
			 		address="Local Number";
			 		break;
			 		
			 	case 8:
			 		address="Local Number";
			 		break;
			 	
			 	case 10:
			 		address = query("select city from info where area = ? limit 1", 
			 				new String[] {number.substring(0, 3)}); 
			 		if(address.equals(""))  
                    {  
                        address = number;  
                    }  
                    break; 
                    
			 	case 11 : //3位区号，8位号码  或4位区号，7位号码  
                    address = query("select city from info where area = ? limit 1", 
                    		new String[] {number.substring(0, 3)});  
                    if(address.equals(""))  
                    {  
                        address = query("select city from info where area = ? limit 1", 
                        		new String[] {number.substring(0, 4)});  
                        if(address.equals(""))  
                        {  
                            address = number;  
                        }  
                    }  
                    break;  
                      
                case 12 : //4位区号，8位号码  
                    address = query("select city from info where area = ? limit 1",
                    		new String[] {number.substring(0, 4)});  
                    if(address.equals(""))  
                    {  
                        address = number;  
                    }  
                    break;  
                      
                default :   
                    break;
			 }
		 }
		 
		 
		 return address;
	}
	
	private  String query(String sql, String[] selectionArgs){
		String result = "";
		
		SQLiteDatabase db = getAddressDB(PATH);
		
		if(db.isOpen()){
			Cursor cursor = db.rawQuery(sql, selectionArgs);
			if(cursor.moveToNext()){
				result = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}
		return result;
	}
	
	private  SQLiteDatabase getAddressDB(String path){
		/*
		Log.d("shiguibiao","path ="+path);
		
		File dir = new File( DIR_PATH);
		
		Log.d("shiguibiao","dirth ="+DIR_PATH);
		
		if(!dir.exists()){
			Log.d("shiguibiao","mkdir");
			dir.mkdir();
		}
		
		if(file.exists()){
			return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		}else{
			try{
				file.createNewFile();
				Log.d("shiguibiao","createNewFile");
				InputStream in = context.getApplicationContext()
						.getResources().openRawResource(R.raw.address);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[in.available()];
				while(in.read(buffer) != -1){
					in.read(buffer);
					fos.write(buffer);
					fos.flush();
					Log.d("shiguibiao","write");
				}
				in.close();
				fos.close();
			}catch(FileNotFoundException  e){
		        e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			*/
			return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//}
		
	}
	
	public void loadDBFile(){
		
		File dir = new File( DIR_PATH);
		if(!dir.exists()){

			dir.mkdir();
		}
		File file = new File(DIR_PATH,"data.db");
		if(!file.exists()){
			try{
				file.createNewFile();

				InputStream in = context.getApplicationContext()
						.getResources().openRawResource(R.raw.address);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[in.available()];
				while(in.read(buffer) != -1){
					//in.read(buffer); //which make half file copyed
					fos.write(buffer);
					fos.flush();

				}
				in.close();
				fos.close();
			}catch(FileNotFoundException  e){
		        e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
}
