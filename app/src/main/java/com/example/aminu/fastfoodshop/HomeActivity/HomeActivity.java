package com.example.aminu.fastfoodshop.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aminu.fastfoodshop.Login.LoginActivity;
import com.example.aminu.fastfoodshop.Methods.TotalPrice;
import com.example.aminu.fastfoodshop.Models.FoodItems;
import com.example.aminu.fastfoodshop.Models.FoodOptions;
import com.example.aminu.fastfoodshop.R;
import com.example.aminu.fastfoodshop.Utils.CustomAdapter;
import com.example.aminu.fastfoodshop.Utils.CustomGridAdapter;
import com.example.aminu.fastfoodshop.Utils.CustomListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Context mContext= HomeActivity.this;
    private static Button button_clear;
    private static Button button_deleteAll;
    private static Button button_confirmPurchase;
    public static TextView last_price;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Price List View
    public static List<String> item_listName = new ArrayList<String>();
    public static List<String> item_listPrice = new ArrayList<String>();
    public static List<String> item_listPrice_copy = new ArrayList<String>();
    public static List<String> item_listid = new ArrayList<String>();

    private List<String> item_name = new ArrayList<String>();
    private List<String> item_price = new ArrayList<String>();
    private List<String> item_price_copy = new ArrayList<String>();

    //Item GridView
    /*String[] itemName0={"Beef Burger","Chicken Burger","Pizza","Sandwich","Sub-Sandwich","Tacos"};
    String[] itemName1={"Spicy Beef Burger","Chicken Burger","Pizza","Sandwich","Sub-Sandwich","Tacos"};
    String[] itemName2={"Hot Beef Burger","Chicken Burger","Pizza","Sandwich","Sub-Sandwich","Tacos"};
    String[] itemName3={"Not a Beef Burger","Chicken Burger","Pizza","Sandwich","Sub-Sandwich","Tacos"};
    String[] itemName4={"What aBeef Burger","Chicken Burger","Pizza","Sandwich","Sub-Sandwich","Tacos"};
    String[] itemName5={"Got a Beef Burger","Chicken Burger","Pizza","Sandwich","Sub-Sandwich","Tacos"};
    String[] itemPrice0={"300","330","600","60","280","260"};
    String[] itemPrice1={"100","330","600","60","280","260"};
    String[] itemPrice2={"200","330","600","60","280","260"};
    String[] itemPrice3={"400","330","600","60","280","260"};
    String[] itemPrice4={"500","330","600","60","280","260"};
    String[] itemPrice5={"700","330","600","60","280","260"};

    String[] itemPrice00={"300","330","600","60","280","260"};
    String[] itemPrice11={"100","330","600","60","280","260"};
    String[] itemPrice22={"200","330","600","60","280","260"};
    String[] itemPrice33={"400","330","600","60","280","260"};
    String[] itemPrice44={"500","330","600","60","280","260"};
    String[] itemPrice55={"700","330","600","60","280","260"};*/

    int[] itemImage={R.drawable.burger,R.drawable.burger,R.drawable.burger};
    //,R.drawable.burger,R.drawable.burger,R.drawable.burger

    /*Object objectsName[]={itemName0,itemName1,itemName2,itemName3,itemName4,itemName5};
    Object objectsPrice[]={itemPrice0,itemPrice1,itemPrice2,itemPrice3,itemPrice4,itemPrice5};
    Object objectsPrice_copy[]={itemPrice00,itemPrice11,itemPrice22,itemPrice33,itemPrice44,itemPrice55};*/


    //Menu ListView

    public static List<String> mOptions = new ArrayList<String>();
    public static List<String> mId = new ArrayList<String>();

    //String [] options = {"Chefs Special","Drinks","Regulars","Shakes","Special","Specials"};
    int[] images ={R.drawable.ic_chefs_special,R.drawable.ic_drinks,R.drawable.ic_regulars};
    //,R.drawable.ic_shakes,R.drawable.ic_special_drinks,R.drawable.ic_specials

    /**
     * CODE STARTS HERE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: App Starting.");




        iniWidgets();
        setupFirebaseAuth();
        iniDatabase();
        //setupCustomGridAdapter();
        //setupCustomListAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fastfoodshop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sign_Out:

                mAuth.signOut();
                Intent intent2 = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent2);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void iniWidgets(){
        button_clear = (Button) findViewById(R.id.netClear);
        button_deleteAll = (Button)findViewById(R.id.deleteAll);
        button_confirmPurchase = (Button) findViewById(R.id.confirmPurchase);
    }

    /**
     * GRIDVIEW ADAPTER SETUP
     * @param sentName
     * @param sentPrice
     * @param sentPrice_copy
     */
    private void setupCustomGridAdapter(List sentName, List sentPrice, List sentPrice_copy){

        final List<String> itemName = sentName;
        final List<String> itemPrice = sentPrice;
        final List<String> itemPrice_copy = sentPrice_copy;

        GridView gridView = (GridView) findViewById(R.id.gridList);
        CustomGridAdapter adapter = new CustomGridAdapter(this, itemName,itemImage,itemPrice);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //setupCustomListAdapter(itemName[i],itemPrice[i]);
                //Toast.makeText(mContext, itemName[i], Toast.LENGTH_SHORT).show();
                try {
                    if (item_listid.contains(itemName.get(i))) {
                        update_info(item_listid.indexOf(itemName.get(i)));

                        Log.d(TAG, "ID Index: " + item_listid.contains(itemName.get(i)) + "Item ID: " + item_listid.get(item_listid.indexOf(i)));
                    } else {

                        item_listid.add(itemName.get(i));
                        add(itemName.get(i), itemPrice.get(i), itemPrice_copy.get(i));
                    }
                }catch (Exception e){
                    Log.d(TAG, "onItemClick: grid item checking error: "+e.getMessage());
                }
            }

        });

    }

    /**
     * ADDING ITEMS TO STATIC LISTVIEW
     * @param sentName
     * @param sentPrice
     * @param sentPrice_copy
     */
    void add(String sentName, String sentPrice, String sentPrice_copy) {


        item_listName.add(sentName);
        item_listPrice.add(sentPrice);
        item_listPrice_copy.add(sentPrice_copy);
        setupCustomListAdapter( item_listName,item_listPrice,item_listPrice_copy);
    }

    /**
     * @param item_index
     */
    void update_info(int  item_index){
        setupCustomListAdapter( item_listName,item_listPrice,item_listPrice_copy , item_index );
    }


    /**
     * ListView Adapter Setup for Menu
     */
    private  void setupCustomAdapter(){

        ListView listView = (ListView)findViewById(R.id.fragList);
        CustomAdapter adapter= new CustomAdapter(this, mOptions,images);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //setupCustomGridAdapter(objectsName[i],objectsPrice[i],objectsPrice_copy[i]);
                //Log.d(TAG, "onItemClick: Changing menu item.");

                final String test = mId.get(i);
                Log.d(TAG, "onItemClick: "+test);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef_items = database.getReference().child("item_lists").child(test);
                Log.d(TAG, "onItemClick: "+myRef_items);

                // Read from the database
                myRef_items.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d(TAG, "onDataChange: whats happening?"+dataSnapshot);

                        item_name.clear();
                        item_price.clear();
                        item_price_copy.clear();

                        FoodItems fItems = new FoodItems();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){

                            Log.d(TAG, "onDataChange: its inside?");

                            fItems.setName(ds.getValue(FoodItems.class).getName());
                            fItems.setPrice(ds.getValue(FoodItems.class).getPrice());


                            item_name.add(fItems.getName());
                            item_price.add(fItems.getPrice().toString());
                            item_price_copy.add(fItems.getPrice().toString());
                            Log.d(TAG, "onDataChange: "+item_name);
                            Log.d(TAG, "onDataChange: "+item_price);
                            Log.d(TAG, "onDataChange: "+item_price_copy);
                        }
                        setupCustomGridAdapter(item_name,item_price,item_price_copy);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });

    }

    /**
     * ListView Adapter for Price List
     * @param objectsName
     * @param objectsPrice
     * @param objectsPrice_copy
     */
    private void setupCustomListAdapter(List<String> objectsName, List<String> objectsPrice, List<String> objectsPrice_copy){

        //Toast.makeText(mContext, objectsName.get(0), Toast.LENGTH_SHORT).show();

        ListView listView = (ListView) findViewById(R.id.priceList);

        final CustomListAdapter adapter= new CustomListAdapter(this,objectsName,objectsPrice,objectsPrice_copy);
        listView.setAdapter(adapter);
        Log.d(TAG, "setupCustomListAdapter: Pushing item, price & price_copy array to the ListView.");

        /**
         * TextView for total price
         */
        try {
            String lastPrice = TotalPrice.netPrice(item_listPrice_copy);
            last_price = (TextView) findViewById(R.id.rvText4);
            last_price.setText(lastPrice);
        }catch (Exception e){
            Log.d(TAG, "setupCustomListAdapter: Price in total Error: "+e.getMessage());
        }

        /**
         * Clear Bottom ListView
         */
        try{

            button_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clearListView();//call clear method
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onClick: Clearing");
                }
            });
        }catch (Exception e){
            Log.d(TAG, "Button on Double Click:  "+e.getMessage());
        }
        try{

            button_deleteAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clearListView();//call clear method
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onClick: Clearing");
                }
            });
        }catch (Exception e){
            Log.d(TAG, "Button on Double Click:  "+e.getMessage());
        }
        try{

            button_confirmPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clearListView();//call clear method
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onClick: Clearing");
                }
            });
        }catch (Exception e){
            Log.d(TAG, "Button on Double Click:  "+e.getMessage());
        }

    }

    /**
     * ListView Adapter for Price List when Index match found
     * @param objectsName
     * @param objectsPrice
     * @param objectsPrice_copy
     * @param index_id
     */
    private void setupCustomListAdapter(List<String> objectsName, List<String> objectsPrice, List<String> objectsPrice_copy , int index_id ){

        ListView listView = (ListView) findViewById(R.id.priceList);
        final CustomListAdapter adapter= new CustomListAdapter(this,objectsName,objectsPrice,objectsPrice_copy);

        /**
         * On double click quantity change
         */
        try {
            //On double click Quantity increase

            int intQuantity = Integer.parseInt(CustomListAdapter.item_counter.get(index_id));
            intQuantity++;
            String stringQuantity = String.valueOf(intQuantity);
            CustomListAdapter.item_counter.set(index_id, stringQuantity);

            //On double click Price increase

            String countPrice2 = item_listPrice.get(index_id);
            intQuantity *= Integer.parseInt(countPrice2);//multiply count number with individual price of item
            item_listPrice_copy.set(index_id, String.valueOf(intQuantity));
            CustomListAdapter.item_priceBundle.set(index_id, String.valueOf(intQuantity));


            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.d(TAG, "setupCustomListAdapter: changing the quantity and price only.");
            Log.d(TAG, "Quantity: "+intQuantity+" Price for quantity: "+intQuantity);
        }catch (Exception e){
            Log.d(TAG, "setupCustomListAdapter: index changer adapter error: "+e.getMessage());
        }


        /**
         * TextView for total price
         */
        try {
            String lastPrice = TotalPrice.netPrice(item_listPrice_copy);
            last_price = (TextView) findViewById(R.id.rvText4);
            last_price.setText(lastPrice);
        }catch (Exception e){
            Log.d(TAG, "setupCustomListAdapter: Price in total Error: "+e.getMessage());
        }

        /**
         * Clear Bottom ListView
         */
        try{

            button_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clearListView();//call clear method
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onClick: Clearing");
                }
            });
        }catch (Exception e){
            Log.d(TAG, "Button on Double Click:  "+e.getMessage());
        }
        try{

            button_deleteAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clearListView();//call clear method
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onClick: Clearing");
                }
            });
        }catch (Exception e){
            Log.d(TAG, "Button on Double Click:  "+e.getMessage());
        }
        try{

            button_confirmPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clearListView();//call clear method
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onClick: Clearing");
                }
            });
        }catch (Exception e){
            Log.d(TAG, "Button on Double Click:  "+e.getMessage());
        }

    }

    /**
     * Method responsible for clearing the ListView
     */
    private void clearListView() {
        item_listName.clear();
        item_listPrice.clear();
        item_listPrice_copy.clear();
        item_listid.clear();
        CustomListAdapter.item_priceBundle.clear();
        CustomListAdapter.item_counter.clear();
        last_price.setText("Price");
    }


    /*
    -------------------------------------------------------FireBase-------------------------------------------
     */

    private void iniDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("catagory_options");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mOptions.clear();
                FoodOptions fOptions = new FoodOptions();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    //String name =ds.getValue(String.class);
                    fOptions.setName(ds.getValue(FoodOptions.class).getName());
                    fOptions.setId(ds.getValue(FoodOptions.class).getId());
                    mOptions.add(fOptions.getName());
                    mId.add(fOptions.getId());
                    Log.d(TAG, "onDataChange: "+mOptions);
                }
                setupCustomAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Checks to see if the @param user is logged in
     * @param //user
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user== null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            Log.d(TAG, "checkCurrentUser: moving to login screen.");
        }else{
            Log.d(TAG, "checkCurrentUser: user logged in already.");
        }
    }


    /**
     * setup the Firebase auth object
     */
    private void setupFirebaseAuth(){

        Log.d(TAG, "setupFirebaseAuth: Setting up Firebase Auth.");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //Check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}

