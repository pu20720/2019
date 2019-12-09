package andbas.Ch11TabHost2;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


public class UserAdapter extends BaseAdapter
{
	private DiscussList DiscussList;
	private LayoutInflater myInflater;
	private ArrayList<HashMap<String, String>> list = null;
	private ViewTag viewTag;
	 
	public UserAdapter(DiscussList context, ArrayList<HashMap<String, String>> list) 
	{
	    this.DiscussList = context;
	    this.myInflater = LayoutInflater.from(context);
	    this.list = list;
	}
	 
	public int getCount() {
	    return list.size();
	}
	 
	public Object getItem(int position) {
	    return list.get(position);
	}
	 
	public long getItemId(int position) {
	    return Long.valueOf(list.get(position).get("id"));
	}
	 
	public View getView(int position, View convertView, ViewGroup parent) {
	 
	    if (convertView == null) {
	        // 取得listItem容器 view
	        convertView = myInflater.inflate(R.layout.userlistview, null);
	 
	        // 建構listItem內容view
	        viewTag = new ViewTag(
	                (TextView) convertView.findViewById(R.id.textView1),
	                (Button) convertView.findViewById(R.id.button1));
	 
	        // 設置容器內容
	        convertView.setTag(viewTag);
	 
	    } else {
	        viewTag = (ViewTag) convertView.getTag();
	    }
	     
	    viewTag.text1.setText(list.get(position).get("name"));
	    //設定按鈕監聽事件及傳入 DiscussList 實體
	    viewTag.btn1.setOnClickListener(new ItemButton_Click(this.DiscussList, position));
	     
	    return convertView;
	}
	 
	public class ViewTag {
	    TextView text1;
	    TextView btn1;
	     
	    public ViewTag(TextView textview1, Button button1) {
	        this.text1 = textview1;
	        this.btn1 = button1;
	    }
	}
	 
	//自訂按鈕監聽事件
	class ItemButton_Click implements OnClickListener {
	    private int position;
	    private DiscussList DiscussList;
	     
	    ItemButton_Click(DiscussList context, int pos) {
	        this.DiscussList = context;
	        position = pos;
	    }
	 
	    public void onClick(View v) 
	    {
	    	int id = Integer.valueOf(list.get(position).get("id"));
	    	DiscussList.showlocation(id);
	    }
	}
}