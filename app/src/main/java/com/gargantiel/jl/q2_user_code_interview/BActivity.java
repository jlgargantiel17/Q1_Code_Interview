package com.gargantiel.jl.q2_user_code_interview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BActivity extends AppCompatActivity {

    //Instance Variables
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, String>> appendedContacts;
    LinearLayout avatarLayout;
    HorizontalScrollView scrollView;
    ListView contacts_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        contactList = new ArrayList<HashMap<String, String>>();
        appendedContacts = new ArrayList<HashMap<String, String>>();
        getContactList();
        onInitialize();

    }

    //Action creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, "Done");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    //UI Initialized
    private void onInitialize() {

        contacts_list = (ListView) findViewById(R.id.contacts_list);
        EditText search = (EditText) findViewById(R.id.search);
        avatarLayout = (LinearLayout) findViewById(R.id.avatarLayout);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollview);

        contacts_list.setAdapter(new ArrayAdapter<HashMap<String, String>>(this, R.layout.contact_list_item, R.id.name, contactList) {

            //Filter Variables
//            public ArrayList<HashMap<String, String>> filtered;
//            ArrayList<HashMap<String, String>> items;

            private Filter filter;

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = (LayoutInflater) BActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(R.layout.contact_list_item, parent, false);

                TextView name = (TextView) row.findViewById(R.id.name);
                TextView number = (TextView) row.findViewById(R.id.number);
                CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);
                final ImageView contact_photo = (ImageView) row.findViewById(R.id.contact_photo);

                if (this.getItem(position).get("isSelected").equals("1")){
                    checkBox.setChecked(true);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {

                        if ( isChecked ) {
                            appendList(getItem(position));
                            contactList.get(position).put("isSelected", "1");
                            ((ArrayAdapter<HashMap<String, String>>)contacts_list.getAdapter()).notifyDataSetChanged();
                        }else {
                            appendedContacts.remove(getItem(position));
                            contactList.get(position).put("isSelected", "0");
                            ((ArrayAdapter<HashMap<String, String>>)contacts_list.getAdapter()).notifyDataSetChanged();
                        }

                    }
                });

                setImage(this.getItem(position), contact_photo, this.getContext());
                name.setText(this.getItem(position).get("name"));
                number.setText(this.getItem(position).get("phone"));

                return row;
            }
            //This is the filter for search implementation not functional yet

//            @Override
//            public Filter getFilter() {
//                if (filter == null) {
//                    items = new ArrayList<Structure_Delivery>();
//                    for (int index = 0; index < super.getCount(); index++)
//                        items.add(super.getItem(index));
//                    filter = new ManageFilter();
//                }
//                return filter;
//            }
//
//            class ManageFilter extends Filter {
//
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//                    // NOTE: this function is *always* called from a background thread, and
//                    // not the UI thread.
//                    constraint = constraint.toString().toLowerCase();
//                    FilterResults result = new FilterResults();
//                    if (constraint != null && constraint.toString().length() > 0) {
//                        ArrayList<Structure_Delivery> filtered = new ArrayList<Structure_Delivery>();
//                        ArrayList<Structure_Delivery> lItems = new ArrayList<Structure_Delivery>();
//                        synchronized (this) {
//                            lItems.addAll(items);
//                        }
//                        for (int i = 0, l = lItems.size(); i < l; i++) {
//                            Structure_Delivery m = lItems.get(i);
//                            if (m.getOrderFrom().toLowerCase().contains(constraint) || m.getDeliveryAddressDisplay().toLowerCase().contains(constraint) || m.getDeliveryStatusDisplay().toLowerCase().contains(constraint))
//                                filtered.add(m);
//                        }
//                        result.count = filtered.size();
//                        result.values = filtered;
//                    } else {
//                        synchronized (this) {
//                            result.values = items;
//                            result.count = items.size();
//                        }
//                    }
//                    return result;
//                }
//
//                @SuppressWarnings("unchecked")
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    // NOTE: this function is *always* called from the UI thread.
//                    filtered = (ArrayList<Structure_Delivery>) results.values;
//                    notifyDataSetChanged();
//                    clear();
//                    for (int i = 0, l = filtered.size(); i < l; i++)
//                        add(filtered.get(i));
//                    notifyDataSetInvalidated();
//                }
//
//            }

        });


    }

    // This is where the action bar actions are handled
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                appendedContacts.clear();
                intent.putExtra("contactlist",appendedContacts);
                setResult(RESULT_OK, intent);
                finish();

                return false;

            case 1000:

                intent.putExtra("contactlist",appendedContacts);
                setResult(RESULT_OK, intent);
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Appends the selected contacts to be passed to AActivity
    private void appendList(HashMap<String, String> contact){

        appendedContacts.add(contact);
        LinearLayout.LayoutParams scrollParams;

        if (appendedContacts.size() == 0){
            scrollParams = new LinearLayout.LayoutParams(0,50,0.0f);
        }else if (appendedContacts.size()>5 && appendedContacts.size() < 10){
            scrollParams = new LinearLayout.LayoutParams(0,50,0.2f);
        }else if (appendedContacts.size()>=10){
            scrollParams = new LinearLayout.LayoutParams(0,50,0.3f);
        }else {
            scrollParams = new LinearLayout.LayoutParams(0,50,0.1f);
        }

        scrollView.setLayoutParams(scrollParams);

        FrameLayout.LayoutParams imageViewParams = new FrameLayout.LayoutParams(50, 50);

        ImageView myImage = new ImageView(this);
        myImage.setLayoutParams(imageViewParams);
        setImage(contact, myImage, this);
        avatarLayout.addView(myImage);

    }

    //Appends image to user avatar in the contact list
    private void setImage(HashMap<String, String> item, final ImageView imageView, Context context){
        try{
            Log.d("JL DEBUG", "Data: "+item.get("img").equals("null")); //Check for null Images
            String imgUri = item.get("img");
            Picasso.with(context).load(imgUri).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                    imageDrawable.setCircular(true);
                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                    imageView.setImageDrawable(imageDrawable);
                }
                @Override
                public void onError() {
                    imageView.setImageResource(R.drawable.ic_mood_black_24dp);
                }
            });
        }catch (Exception e){
            imageView.setImageResource(R.drawable.ic_mood_black_24dp);
        }
    }

    //Get contacts from Phone.
    private void getContactList() {

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        while (phones.moveToNext()) {
            final String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            final String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            final String image_uri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            contactList.add(new HashMap<String, String>() {{
                put("name", name);
                put("phone", phoneNumber);
                put("img", image_uri);
                put("isSelected","0");
            }});
        }
        phones.close();
    }
}
