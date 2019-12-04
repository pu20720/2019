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

import com.xml.MYLHandler;
import com.xml.MessageHandler;
import com.xml.MessageStruct;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class Dynamic extends Activity {
	private ListView listaction;
	String[] action_name = new String[] { "日式","中式","台式","義式","美式","其他" };
	Intent intent;
	
	int index = 0;
	String IPAddress="";
	private Button btn_addtalk;
	
	private ArrayList<MessageStruct> query_data= new ArrayList<MessageStruct>();
	
	private ArrayList<MessageStruct> query_data2= new ArrayList<MessageStruct>();
	
	int type;
	String msg;
	
	EditText et;

	int flag = 0
	
	
;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic);
		
		IPAddress = (String) this.getResources().getText(R.string.url);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		listaction =(ListView)findViewById(R.id.list_action);
		
		et =(EditText)findViewById(R.id.editText1);
		
		index = 0;
		
		btn_addtalk = (Button) findViewById(R.id.button1);
	     btn_addtalk.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) 
				{
					if (flag == 1)
					{
						flag = 0;
						btn_addtalk.setText("SEARCH");
					}
					else
					{
						flag = 1;
						btn_addtalk.setText("CLEAR");
					}
					query_data2.clear();
					try {
						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				        StrictMode.setThreadPolicy(policy); 
				        String uriAPI ="";
				        try
				        {
				        	//String s = URLEncoder.encode(k, "utf-8");
				        	uriAPI = IPAddress + "getkeyword.php?keyword=" + et.getText().toString();
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
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					
					StringTokenizer stoken = null;		
					ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();
					
					
					for (int i=0; i<query_data.size(); i++)
			        {
				          HashMap<String, Object> map = new HashMap<String, Object>();
				          
					  	  map.put("ItemTitle", query_data.get(i).title);
				          
				          map.put("ItemText", query_data.get(i).rdate);
				          
				          query_data2.add(query_data.get(i));
				
				          listitem.add(map);           
			        }          		 
					 
					SimpleAdapter listitemAdapter=new SimpleAdapter(Dynamic.this,
							 	listitem,
					            R.layout.no_listview_style,
					            new String[]{"ItemTitle","ItemText"},
					            new int[]{R.id.topTextView, R.id.bottomTextView});

				    listaction.setAdapter(listitemAdapter);
				}
			});
		
		buildViews();
	}

	private void buildViews() 
	{
		ArrayAdapter<String> adapterAction = new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_list_item_1, action_name) 
			{
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view.findViewById(android.R.id.text1);
				text.setTextColor(Color.BLACK);
				return view;
			}
		};
		listaction.setAdapter(adapterAction);
		listaction.setOnItemClickListener(lstPreferListener);
	}
	
    private ListView.OnItemClickListener lstPreferListener=
        	new ListView.OnItemClickListener(){

    		public void onItemClick(AdapterView<?> parent, View v,
    					int position, long id) 
    		{
    			if (flag == 1)
    			{
    					Intent app = new Intent(Dynamic.this, DynamicList.class);
    					Bundle rdata = new Bundle();
    					rdata.putString("title", query_data2.get(position).title);
    					rdata.putString("content", query_data2.get(position).content);
    					rdata.putString("img", query_data2.get(position).pic);
    					rdata.putString("pnum", query_data2.get(position).pnum);
    					rdata.putString("article_id", query_data2.get(position).id);
    					rdata.putString("phone", query_data2.get(position).rdate);
    					rdata.putString("address", query_data2.get(position).address);
    					app.putExtras(rdata);
    					startActivity(app);
    			}
    			else if (index == 0)
    			{
    				msg =parent.getItemAtPosition(position).toString();
    				type = position;
    				
    				index = 1;
    				reload();
    			}
    			else
    			{
    				if (position == query_data.size())
    				{
    					index = 0;
    					buildViews();
    				}
    				else
    				{
    					Intent app = new Intent(Dynamic.this, DynamicList.class);
    					Bundle rdata = new Bundle();
    					rdata.putString("title", query_data.get(position).title);
    					rdata.putString("content", query_data.get(position).content);
    					rdata.putString("img", query_data.get(position).pic);
    					rdata.putString("pnum", query_data.get(position).pnum);
    					rdata.putString("article_id", query_data.get(position).id);
    					rdata.putString("phone", query_data.get(position).rdate);
    					rdata.putString("address", query_data.get(position).address);
    					app.putExtras(rdata);
    					startActivity(app);
    				}
    			}
    		}
   };
   
   
	void reload()
	{
		gettype();
		
	    StringTokenizer stoken = null;		
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();
		
		 Log.i("TAG", query_data.size()+"");
		for (int i=0; i<query_data.size(); i++)
        {
	          HashMap<String, Object> map = new HashMap<String, Object>();
	          
	          Log.i("TAG", query_data.get(i).title);
	          
		  	  map.put("ItemTitle", query_data.get(i).title);
	          
	          map.put("ItemText", query_data.get(i).rdate);
	
	          listitem.add(map);           
        }
		 
		 HashMap<String, Object> map = new HashMap<String, Object>();
         
      	 map.put("ItemTitle", "回上一頁");
         map.put("ItemText", "");

         listitem.add(map);           		 
		 
		SimpleAdapter listitemAdapter=new SimpleAdapter(this,
				 	listitem,
		            R.layout.no_listview_style,
		            new String[]{"ItemTitle","ItemText"},
		            new int[]{R.id.topTextView, R.id.bottomTextView});

	    listaction.setAdapter(listitemAdapter);
	}
	
	 void gettype()
	 {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        String uriAPI ="";
	        try
	        {
	        	//String s = URLEncoder.encode(k, "utf-8");
	        	uriAPI = IPAddress + "getgrouptype.php?type=" + type;
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
}