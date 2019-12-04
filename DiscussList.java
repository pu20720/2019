package andbas.Ch11TabHost2;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.xml.DiscussHandler;
import com.xml.DiscussStruct;
import com.xml.LoginXMLHandler;
import com.xml.LoginXMLStruct;
import com.xml.MessageHandler;
import com.xml.MessageStruct;
import com.xml.UserGroupHandler;
import com.xml.UserGroupStruct;

import andbas.Ch11TabHost2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class DiscussList extends Activity 
{
	TextView textView1;
	ListView lv_discuss;
	ListView lv_user;
	
	EditText s1;
	
	ImageView iv;
	
	String title, context= "", img, pnum, article_id;
	int type;
	String IPAddress="";
	
	Button sender_msg, chatroom;
	
	private ArrayList<UserGroupStruct> query_data= new ArrayList<UserGroupStruct>();
	
	private ArrayList<DiscussStruct> discuss_data= new ArrayList<DiscussStruct>();
	
	LoginXMLStruct data;
	
	String user;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activitylist);

		
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);	    	  
   	    user = telephonyManager.getDeviceId();
		
	    Bundle bData = this.getIntent().getExtras();
	    
	    article_id = bData.getString( "article_id" );
	    pnum = bData.getString( "pnum" );
	    title = bData.getString( "title" );
	    
		IPAddress = (String) this.getResources().getText(R.string.url);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		    
        buildViews();  //user define
    }
    
    
	private void buildViews() 
	{
		textView1 = (TextView)findViewById(R.id.textView1);
		
		lv_discuss = (ListView)findViewById(R.id.ListView01);
		lv_user = (ListView)findViewById(R.id.listView1);	
		
		
		sender_msg = (Button) findViewById(R.id.button2);
		chatroom = (Button) findViewById(R.id.button1);
		
		textView1.setText(title);
		
		 reload();
			
		 sender_msg.setOnClickListener(listener);
		 chatroom.setOnClickListener(listener);
	}
	
	private Button.OnClickListener listener  = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button2:
				addf();
				break;
			case R.id.button1:
				break;
			
        	  	
			}
			
		}
	};
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void reload()
	{
		int attend_num;

		getdiscussgroup();
		
		getusergroup();
		
		attend_num = query_data.size();
		
		if (attend_num == 0) attend_num = 1;
		 
		chatroom.setText("" + attend_num + "/" + pnum);
		chatroom.setEnabled(false);

		//ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) 
			{
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view.findViewById(android.R.id.text1);
				text.setTextColor(Color.BLACK);
				return view;
			}
		};

		for (int i=0; i<discuss_data.size(); i++)
        {
	          adapter.add(discuss_data.get(i).user + ":" + discuss_data.get(i).content);
        }
	    
		lv_discuss.setAdapter(adapter);

		
		ArrayList<HashMap<String, String>> listitem2 = new ArrayList<HashMap<String, String>>();
		for (int i=0; i<query_data.size(); i++)
        {
	          HashMap<String, String> map = new HashMap<String, String>();
	          
		      map.put("id", Integer.toString(i));
        	  map.put("name", query_data.get(i).user);
	          listitem2.add(map);           
        }
	    
		lv_user.setAdapter(new UserAdapter(this, listitem2));		

	}
	
	 void getusergroup()
	 {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        String uriAPI ="";
	        try
	        {
	        	//String s = URLEncoder.encode(k, "utf-8");
	        	uriAPI = IPAddress + "getgroupuser.php?aid=" + article_id;
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
				UserGroupHandler myHandler = new UserGroupHandler();
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
	 
	 void getdiscussgroup()
	 {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        String uriAPI ="";
	        try
	        {
	        	//String s = URLEncoder.encode(k, "utf-8");
	        	uriAPI = IPAddress + "gettalklist.php?aid=" + article_id;
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
				DiscussHandler myHandler = new DiscussHandler();
				xr.setContentHandler(myHandler);
				//open connection
				xr.parse(new InputSource(url.openStream()));
				//verify OK
				discuss_data = myHandler.getContainer().getListItems();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
	 
	
	 
	void addf()
	{
	      AlertDialog.Builder alert = new AlertDialog.Builder(this);
	      alert.setCancelable(false);
	      alert.setTitle("");     
	      
	      ScrollView sv = new ScrollView(this);
	      LinearLayout ll = new LinearLayout(this);
	      ll.setOrientation(LinearLayout.VERTICAL);
	      sv.addView(ll);
	      
	      TextView tlogin = new TextView(this);
	      tlogin.setTextColor(Color.WHITE);
	      tlogin.setText(": ");
	      s1 = new EditText(this);
	      s1.setText("");
	      ll.addView(tlogin);
	      ll.addView(s1);

	      // Set an EditText view to get user input
	      alert.setView(sv);
	      
	      alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton)
	        {
	      	  try
	      	  {
	  	    	  String content = URLEncoder.encode((s1.getText().toString()), "UTF-8");
	  	    	  
	  	    	  String uriAPI = IPAddress + "sendtalk.php?aid=" + article_id  + "&content=" + content 
	  						+ "&user=" + user;
	  	    	  
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
	  				finally
	  				{
	  					reload();
	  				}
	      	  }
	      	  catch (Exception e)
	      	  {
	      		  e.printStackTrace();
	      	  }

	        		
	        }
	      });
	      
	      alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int whichButton)
	          {
	            

	          }
	        });
	        
	      
	      alert.show();    
	}
	
	void showlocation(int id)
	{
		String uriAPI = IPAddress + "getsd.php?user=" + query_data.get(id).user;;
		  
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
		
		if (data.h_chilid.equals("0"))
		{
			
		}
		else
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
	}
}
