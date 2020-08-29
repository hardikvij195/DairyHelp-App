package com.example.dairyhelp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AppListAdapter extends BaseAdapter {


    private Context mcontext ;
    private List<OrderClass> mList ;

    public AppListAdapter(Context mcontext, List<OrderClass> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = View.inflate(mcontext , R.layout.order_list_layout , null);

        TextView OrderId = (TextView)v.findViewById(R.id.textViewOrderId);
        TextView DName = (TextView)v.findViewById(R.id.DName);
        TextView Order = (TextView)v.findViewById(R.id.textView18);

        Order.setText("Order : \n" + mList.get(position).getOrder());
        DName.setText(mList.get(position).getName());
        OrderId.setText("Order Id : \n" + mList.get(position).getId() );

        return v;

    }
}

