package yaksok.dodream.com.yaksok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import yaksok.dodream.com.yaksok.item.FamilyItem;
import yaksok.dodream.com.yaksok.R;

public class FamilyFindAdapter extends BaseAdapter{
    private ArrayList<FamilyItem> familyItems = new ArrayList<>();
    @Override
    public int getCount() {
        return familyItems.size();
    }

    @Override
    public Object getItem(int position) {
        return familyItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final int pos = position;
       final Context context = parent.getContext();

       if(convertView == null){
           LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = layoutInflater.inflate(R.layout.family_list_item,parent,false);
       }

        TextView name = (TextView)convertView.findViewById(R.id.family_list_item_name);

       FamilyItem familyItem = familyItems.get(position);
       name.setText(familyItem.getName());

        return convertView;
    }

    public void addItem(String name){
        FamilyItem familyItem = new FamilyItem();

        familyItem.setName(name);

        familyItems.add(familyItem);
    }
}
