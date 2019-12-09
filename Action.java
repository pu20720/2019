package andbas.Ch11TabHost2;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.xml.LoginXMLHandler;
import com.xml.LoginXMLStruct;
import com.xml.MessageHandler;
import com.xml.MessageStruct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Action extends Activity 
{
	static public Action myaction;
	
	ListView listaction;
	Intent intent;
	
	int index = 0;
	String IPAddress="";
	
	private ArrayList<MessageStruct> query_data= new ArrayList<MessageStruct>();
	private ArrayList<MessageStruct> query_data2= new ArrayList<MessageStruct>();
	
	int type;
	String msg;
	
	String user;
	
	LoginXMLStruct data;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic);
        

        myaction = this;
        
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);	    	  
   	    user = telephonyManager.getDeviceId();        
        
		IPAddress = (String) this.getResources().getText(R.string.url);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		listaction =(ListView)findViewById(R.id.list_action);
		index = 0;
        
        buildViews();
    }

	private void buildViews() 
	{
		reload();
		listaction.setOnItemClickListener(lstPreferListener);

	}

    private ListView.OnItemClickListener lstPreferListener=
        	new ListView.OnItemClickListener(){

    		public void onItemClick(AdapterView<?> parent, View v,
    					int position, long id) 
    		{
   					Intent app = new Intent(Action.this, DiscussList.class);
   					Bundle rdata = new Bundle();
   					rdata.putString("title", query_data2.get(position).title);
   					rdata.putString("pnum", query_data2.get(position).pnum);
   					rdata.putString("article_id", query_data2.get(position).id);
   					app.putExtras(rdata);
   					startActivity(app);
    		}
   };
   
   
	void reload()
	{
		gettype();
		
		String address="";
	    StringTokenizer stoken = null;		
		ArrayList<HashMap<String, String>> listitem = new ArrayList<HashMap<String, String>>();
		
		query_data2.clear();
		for (int i=0; i<query_data.size(); i++)
        {
	          HashMap<String, String> map = new HashMap<String, String>();
			  
			  if (query_data.get(i).id == null) continue;
			  
		      map.put("id", query_data.get(i).id);
	          
			  map.put("name", query_data.get(i).title);	  

			  query_data2.add(query_data.get(i));
			  
	          listitem.add(map);           
        }
		 
	    
	    listaction.setAdapter(new MyAdapter(this, listitem));
	}
	
	 void gettype()
	 {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        String uriAPI ="";
	        try
	        {
	        	//String s = URLEncoder.encode(k, "utf-8");
	        	uriAPI = IPAddress + "getusergrouplist.php?user=" + user;
	        	Log.i("TAG", uriAPI);
	        }
	        catch (Exception e)
	        {
	        	e.printStackTrace();
	        }
	        
			Log.i("TAG", uriAPI);
			
			URL url = null;
			try{
				url = new URL(uriAPI);
				
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				//Using login handler for xml
				MessageHandler myHandler = new MessageHandler();
				xr.setContentHandler(myHandler);
				//open connection
				xr.parse(new InputSource(url.openStream()));
				//verify OK
				query_data = myHandler.getContainer().getListItems();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	 }   	
	 
		void DeleteData(String article_id)
		{
	    	  try
	    	  {
		    	  String uriAPI = IPAddress + "deleteattendgroup.php?aid=" + article_id + "&user=" + user;
		    	  
		    	  Log.i("TAG", uriAPI);
		
					URL url = null;
					try{
						url = new URL(uriAPI);
						
						SAXParserFactory spf = SAXParserFactory.newInstance();
						SAXParser sp = spf.newSAXParser();
						XMLReader xr = sp.getXMLReader();
						//Using login handler for xml
						LoginXMLHandler myHandler = new LoginXMLHandler();
						xr.setContentHandler(myHandler);
						//open connection
						xr.parse(new InputSource(url.openStream()));
						//verify OK
						data = myHandler.getParsedData();
					}
					catch(Exception e){
						e.printStackTrace();
						return;
					}				
	    	  }
	    	  catch (Exception e)
	    	  {
	    		  e.printStackTrace();
	    	  }
	    	  
	    	  reload();
		}

}
