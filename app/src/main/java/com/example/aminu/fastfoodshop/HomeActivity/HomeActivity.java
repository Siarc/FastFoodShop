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
import com.example.aminu.fastfoodshop.Methods.Constants;
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
    private Button button_clear;
    private Button button_deleteAll;
    private Button button_confirmPurchase;
    public static TextView last_price;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;

    //Price List View
    public static List<String> item_listName = new ArrayList<String>();
    public static List<String> item_listPrice = new ArrayList<String>();
    public static List<String> item_listPrice_copy = new ArrayList<String>();
    public static List<String> item_listid = new ArrayList<String>();

    public static List<String> item_id = new ArrayList<String>();
    public static List<String> item_name = new ArrayList<String>();
    public static List<String> item_image_url = new ArrayList<String>();
    public static List<String> item_price = new ArrayList<String>();
    public static List<String> item_price_copy = new ArrayList<String>();

    int[] itemImage={R.drawable.burger,R.drawable.burger,R.drawable.burger};

    //Menu ListView
    public static List<String> mOptions = new ArrayList<String>();
    public static List<String> mCategoryImgUrl = new ArrayList<String>();
    public static List<String> mCategoryId = new ArrayList<String>();

    private void oldUsed(){

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

        /*Object objectsName[]={itemName0,itemName1,itemName2,itemName3,itemName4,itemName5};
        Object objectsPrice[]={itemPrice0,itemPrice1,itemPrice2,itemPrice3,itemPrice4,itemPrice5};
        Object objectsPrice_copy[]={itemPrice00,itemPrice11,itemPrice22,itemPrice33,itemPrice44,itemPrice55};*/

        //String [] options = {"Chefs Special","Drinks","Regulars","Shakes","Special","Specials"};
        //private int[] images ={R.drawable.ic_chefs_special,R.drawable.ic_drinks,R.drawable.ic_regulars};
        //,R.drawable.ic_shakes,R.drawable.ic_special_drinks,R.drawable.ic_specials
    }

    /**
     * CODE STARTS HERE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: App Starting.");

        database = FirebaseDatabase.getInstance();

        iniWidgets();
        setupFirebaseAuth();
        iniDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fastfoodshop_homepage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sign_Out:

                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);

                return true;
            case R.id.custom_page:

                Intent intent1 = new Intent(HomeActivity.this, CustomizeActivity.class);
                startActivity(intent1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void iniWidgets(){
        button_clear = (Button) findViewById(R.id.netClear);
        button_deleteAll = (Button)findViewById(R.id.deleteAll);
        button_confirmPurchase = (Button) findViewById(R.id.confirmPurchase);
    }

    /**
     * GRIDVIEW ADAPTER SETUP
     */
    private void setupCustomGridAdapter(){

        try{

            GridView gridView = (GridView) findViewById(R.id.gridList);
            CustomGridAdapter adapter = new CustomGridAdapter(this, item_name,item_image_url,item_price);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    try {
                        if (item_listid.contains(item_name.get(i))) {
                            update_info(item_listid.indexOf(item_name.get(i)));

                        } else {

                            item_listid.add(item_name.get(i));
                            add(item_name.get(i), item_price.get(i), item_price_copy.get(i));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });

            }catch (Exception e){
                e.printStackTrace();
        }

    }

    /**
     * ADDING ITEMS TO LISTVIEW
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
        CustomAdapter adapter= new CustomAdapter(this,mOptions,mCategoryImgUrl);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String test = mCategoryId.get(i);

                DatabaseReference myRef_items = database.getReference().child(Constants.DATABASE_PATH_ITEM).child(test);

                // Read from the database
                myRef_items.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        item_id.clear();
                        item_name.clear();
                        item_image_url.clear();
                        item_price.clear();
                        item_price_copy.clear();

                        FoodItems fItems = new FoodItems();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){

                            fItems.setItemId(ds.getValue(FoodItems.class).getItemId());
                            fItems.setName(ds.getValue(FoodItems.class).getName());
                            fItems.setItemImgUrl(ds.getValue(FoodItems.class).getItemImgUrl());
                            fItems.setPrice(ds.getValue(FoodItems.class).getPrice());

                            item_id.add(fItems.getItemId());
                            item_name.add(fItems.getName());
                            item_image_url.add(fItems.getItemImgUrl());
                            item_price.add(fItems.getPrice().toString());
                            item_price_copy.add(fItems.getPrice().toString());
                        }
                        setupCustomGridAdapter();
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
        DatabaseReference myRef = database.getReference().child(Constants.DATABASE_PATH_CATEGORY);


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mOptions.clear();
                mCategoryImgUrl.clear();
                mCategoryId.clear();
                FoodOptions fOptions = new FoodOptions();
                for(DataSnapshot ds: dataSnapshot.getChildren()){


                    fOptions.setName(ds.getValue(FoodOptions.class).getName());
                    fOptions.setFoodOptionsId(ds.getValue(FoodOptions.class).getFoodOptionsId());
                    fOptions.setCategoryImgUrl(ds.getValue(FoodOptions.class).getCategoryImgUrl());
                    mOptions.add(fOptions.getName());
                    mCategoryId.add(fOptions.getFoodOptionsId());
                    mCategoryImgUrl.add(fOptions.getCategoryImgUrl());
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

