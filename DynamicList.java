package andbas.Ch11TabHost2;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.xml.LoginXMLHandler;
import com.xml.LoginXMLStruct;
import com.xml.MessageHandler;
import com.xml.MessageHandler2;
import com.xml.MessageStruct;
import com.xml.MessageStruct2;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class DynamicList extends Activity 
{
	TextView textView1;
	TextView textView2;
	ListView lv;
	
	
	
	ImageView iv;
	
	
	String title, context= "", img, pnum, article_id, phone;
	int type;
	String IPAddress="";
	
	Button account, chatroom, attend, saleinfo;
	
	private ArrayList<UserGroupStruct> query_data= new ArrayList<UserGroupStruct>();
	
	LoginXMLStruct data;
	
	String user;
	
    int private_recieve_type;
    
    int cop = 0;
    
    private ArrayList<MessageStruct2> mydata;
	
    String address;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dynamiclist);

		IPAddress = (String) this.getResources().getText(R.string.url);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);	    	  
   	    user = telephonyManager.getDeviceId();
   	    
		
	    Bundle bData = this.getIntent().getExtras();
	    
	    title = bData.getString( "title" );
	    context = bData.getString( "content" );
	    img = bData.getString( "img" );
	    pnum = bData.getString( "pnum" );
	    article_id = bData.getString( "article_id" );
		phone = bData.getString( "phone" );
		address = bData.getString( "address" );
        buildViews();  //user define
    }
    
    
	private void buildViews() 
	{
		int idx1, idx2, idx3;
	
	
		textView1 = (TextView)findViewById(R.id.textView1);
		textView2 = (TextView)findViewById(R.id.textView2);
		
		
		iv = (ImageView)findViewById(R.id.imageView1);
		
		//account = (Button) findViewById(R.id.button2);
		chatroom = (Button) findViewById(R.id.button1);
		attend = (Button) findViewById(R.id.button3);
		saleinfo = (Button) findViewById(R.id.button2);
		
		textView1.setText(title);
		
		//Log.i("TAG", context);
		
		idx1 = 0;
		
		textView2.setText(context);
		//textView3.setText(context.substring((context.length()/3) + 1,  2 * (context.length()/3)));
		//textView4.setText(context.substring((2 * (context.length()/3) + 1), context.length()));
		//String imgs[] = img.split(",");
		
		//for (int i=0; i<imgs.length; i++)
		{
			String str = IPAddress + "upload/" + img;
			Log.i("TAG", str);
			 try {
			        URL url = new URL(str);
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        connection.setDoInput(true);
			        connection.connect();
			        InputStream input = connection.getInputStream();
			        BitmapFactory.Options opts = new BitmapFactory.Options();
			        
			        opts.inSampleSize = 2;
	
			        Bitmap myBitmap = BitmapFactory.decodeStream(input, null, opts);
			        iv.setImageBitmap(myBitmap);
					
			  } catch (Exception e) {
			        e.printStackTrace();		        
			  }
		}

		 
		 chatroom.setEnabled(false);
		 
		 
		 reload();
		 
			

		 chatroom.setOnClickListener(listener);
		 attend.setOnClickListener(listener);
		 saleinfo.setOnClickListener(listener);
	}
	
	private Button.OnClickListener listener  = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.button2:
				//phone22
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				startActivity(mapIntent);
				break;
			case R.id.button1:
				Intent app = new Intent(DynamicList.this, DiscussList.class);
				Bundle rdata = new Bundle();
				rdata.putString("title", title);
				rdata.putString("pnum", pnum);
				rdata.putString("article_id", article_id);
				app.putExtras(rdata);
				startActivity(app);			
				break;
			case R.id.button3:
				senddata();
        	  	break;
        	  	
			
        	  	
			}
			
		}
	};
	
	
	void reload()
	{
		int attend_num;
		
		getusergroup();
		
		attend_num = query_data.size();
		
		if (attend_num == 0) attend_num = 1;
		 
		 //textView3.setText("" + attend_num + "/" + pnum);
		 

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
			
			for (int i = 0; i<query_data.size(); i++)
			{
				if (query_data.get(i).user.equals(user))
				{
					attend.setEnabled(false);
					chatroom.setEnabled(true);
					break;
				}
			}
	 }

	 
	void senddata()
	{
    	  try
    	  {
	    	  String uriAPI = IPAddress + "sendattendgroup.php?aid=" + article_id + "&user=" + user;
	    	  
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
	
	
	
	public void openOptionsDialog(String title, String info)
	{
	    new AlertDialog.Builder(this)
	    .setTitle(title)
	    .setMessage(info)
	    .setPositiveButton("OK",
	        new DialogInterface.OnClickListener()
	        {
	         public void onClick(DialogInterface dialoginterface, int i)
	         {
	        	
	         }
	         }
	        )
	    .show();
	}
	
	public int toweb(String uriAPI) {
		int error = 0;
		HttpGet httpRequest = new HttpGet(uriAPI);

		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} else {
				// mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString());
			}
		} catch (ClientProtocolException e) {
			// mTextView1.setText(e.getMessage().toString());
			e.printStackTrace();
			error = 1;
		} catch (IOException e) {
			// mTextView1.setText(e.getMessage().toString());
			e.printStackTrace();
			error = 1;
		} catch (Exception e) {
			// mTextView1.setText(e.getMessage().toString());
			e.printStackTrace();
			error = 1;
		}

		return error;
	}
}
