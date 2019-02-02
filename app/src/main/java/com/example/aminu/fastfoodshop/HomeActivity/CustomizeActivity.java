package com.example.aminu.fastfoodshop.HomeActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aminu.fastfoodshop.Methods.Constants;
import com.example.aminu.fastfoodshop.Models.FoodItems;
import com.example.aminu.fastfoodshop.Models.FoodOptions;
import com.example.aminu.fastfoodshop.R;
import com.example.aminu.fastfoodshop.Utils.CustomCategoryViewAdapter;
import com.example.aminu.fastfoodshop.Utils.CustomItemViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.item_id;
import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.item_image_url;
import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.item_name;
import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.item_price;
import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.item_price_copy;
import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.mCategoryId;
import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.mCategoryImgUrl;
import static com.example.aminu.fastfoodshop.HomeActivity.HomeActivity.mOptions;

/**
 * Created by aminu on 10/5/2017.
 */

public class CustomizeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CustomizeActivity";

    //constant to track image chooser intent
    private static final int CATEGORY_IMAGE_REQUEST = 123;
    private static final int CATEGORY_IMAGE_UPDATE = 111;
    private static final int ITEM_IMAGE_REQUEST = 234;
    private static final int ITEM_IMAGE_UPDATE = 222;
    private static final int REQUEST_CODE = 12;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    //Widgets
    private Button btn_categoryImage;
    private Button btn_itemImage;
    private Button btn_confirmCategory;
    private Button btn_confirmItem;
    private Button btn_update_category;
    private Button btn_delete_category;
    private Button btn_select_categoryImage;
    private Button btn_update_item;
    private Button btn_delete_item;
    private Button btn_select_itemImage;
    private ImageView categoryImage;
    private ImageView itemImage;
    private ImageView update_category_image;
    private ImageView update_item_image;
    private EditText categoryName;
    private EditText itemName;
    private EditText itemPrice;
    private EditText update_category_name;
    private EditText update_item_name;
    private EditText update_item_price;
    private TextView showItemName;
    private TextView showCategoryName;
    private TextView closeCategoryDialog;
    private TextView closeItemDialog;

    //uri to store file
    private Uri filePathCategory;
    private Uri filePathItem;
    private Uri fileUpdatedPathCategory;
    private Uri fileUpdatedPathItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        verifyPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fastfoodshop_customizepage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.customize_page:

                Intent intent = new Intent(CustomizeActivity.this, HomeActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Initializing some of the widgets
    public void iniWidgets() {

        btn_categoryImage = (Button) findViewById(R.id.btn_select_category_image);
        btn_itemImage = (Button) findViewById(R.id.btn_select_item_image);
        btn_confirmCategory = (Button) findViewById(R.id.btn_add_catagory);
        btn_confirmItem = (Button) findViewById(R.id.btn_add_item);
        categoryImage = (ImageView) findViewById(R.id.categoryImage);
        itemImage = (ImageView) findViewById(R.id.itemImage);
        categoryName = (EditText) findViewById(R.id.categoryName);
        itemName = (EditText) findViewById(R.id.itemName);
        itemPrice = (EditText) findViewById(R.id.itemPrice);

        btn_categoryImage.setOnClickListener(this);
        btn_itemImage.setOnClickListener(this);
        btn_confirmCategory.setOnClickListener(this);
        btn_confirmItem.setOnClickListener(this);

    }

    //verify permissions
    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions");

        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){

            iniWidgets();
            iniDatabase();
        }else {
            ActivityCompat.requestPermissions(CustomizeActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }

    //Image Chooser methods
    private void categoryImgChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CATEGORY_IMAGE_REQUEST);
    }

    private void categoryImgChooserUpdate() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CATEGORY_IMAGE_UPDATE);
    }

    private void itemImgChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ITEM_IMAGE_REQUEST);
    }

    private void itemImgChooserUpdate() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ITEM_IMAGE_UPDATE);
    }

    /**
     * Calls for device storage images
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathCategory = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathCategory);
                categoryImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == ITEM_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathItem = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathItem);
                itemImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CATEGORY_IMAGE_UPDATE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUpdatedPathCategory = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUpdatedPathCategory);
                update_category_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == ITEM_IMAGE_UPDATE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUpdatedPathItem = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUpdatedPathItem);
                update_item_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Resize Image
    /*Bitmap ShrinkBitmap(String file, int width, int height){

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
        int widthRatio =(int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

        if(heightRatio > 1 || widthRatio > 1){
            if(heightRatio > widthRatio){
                bmpFactoryOptions.inSampleSize = heightRatio;
            }else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file,bmpFactoryOptions);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);


        return bitmap;
    }*/

    //Unique names
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Upload the Category name and image
    private void uploadFileCategory() {
        //checking if file is available
        if (filePathCategory != null && categoryName != null) {

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_CATEGORY + System.currentTimeMillis() + "." + getFileExtension(filePathCategory));

            //adding the file to database
            sRef.putFile(filePathCategory)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests") String categoryImgUrl = taskSnapshot.toString();

                            //adding an upload to Firebase database
                            String foodOptionsId = mDatabase.push().getKey();

                            //creating the upload object to store uploaded image details
                            FoodOptions foodOptions = new FoodOptions(categoryName.getText().toString().trim(), categoryImgUrl, foodOptionsId);


                            mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(foodOptionsId).setValue(foodOptions);
                            categoryName.getText().clear();
                            categoryImage.setImageResource(0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            //display an error if no file is selected
            Toast.makeText(getApplicationContext(), "Fill all the blanks ", Toast.LENGTH_LONG).show();
        }

        //reload the view
        iniDatabase();
    }

    //Update the category name and image
    private void updateFileCategory(final String categoryId){

        //checking if file is available
        if (fileUpdatedPathCategory != null && update_category_image != null) {

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_CATEGORY + System.currentTimeMillis() + "." + getFileExtension(fileUpdatedPathCategory));

            //adding the file to reference
            sRef.putFile(fileUpdatedPathCategory)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Updated ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests") String update_category_image = taskSnapshot.toString();

                            //creating the upload object to store uploaded image details
                            FoodOptions foodOptions = new FoodOptions(update_category_name.getText().toString().trim(), update_category_image, categoryId);


                            mDatabase.child(Constants.DATABASE_PATH_CATEGORY).child(categoryId).setValue(foodOptions);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            //display an error if no file is selected
            Toast.makeText(getApplicationContext(), "Fill all the blanks ", Toast.LENGTH_LONG).show();
        }

        //reload the view
        iniDatabase();
    }

    //Upload the item name, price and image
    private void uploadFileItem(final String categoryId) {
        //checking if file is available
        if (filePathItem != null && itemName != null && itemPrice != null) {

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_ITEM + System.currentTimeMillis() + "." + getFileExtension(filePathItem));

            //adding the file to reference
            sRef.putFile(filePathItem)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests") String itemImgUrl = taskSnapshot.toString();

                            //adding an upload to firebase database
                            String itemId = mDatabase.push().getKey();
                            Long price = Long.valueOf(itemPrice.getText().toString().trim());

                            //creating the upload object to store uploaded image details
                            FoodItems foodItems = new FoodItems(itemImgUrl, itemName.getText().toString().trim(), price, itemId);

                            mDatabase.child(Constants.DATABASE_PATH_ITEM).child(categoryId).child(itemId).setValue(foodItems);
                            itemName.getText().clear();
                            itemPrice.getText().clear();
                            itemImage.setImageResource(0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            //display an error if no file is selected
            Toast.makeText(getApplicationContext(), "Fill all the blanks ", Toast.LENGTH_LONG).show();
        }

        //reload the view
        setupCustomCategoryView();
    }

    //update the item name, price and image
    private void updateFileItem(final String itemId, final String categoryId){

        //checking if file is available
        if (fileUpdatedPathItem != null && update_item_image != null) {

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_ITEM + System.currentTimeMillis() + "." + getFileExtension(fileUpdatedPathItem));

            //adding the file to reference
            sRef.putFile(fileUpdatedPathItem)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Updated ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests") String update_item_image = taskSnapshot.toString();
                            Long price = Long.valueOf(update_item_price.getText().toString().trim());

                            //creating the upload object to store uploaded image details
                            FoodItems foodItems = new FoodItems(update_item_image,update_item_name.getText().toString().trim(), price,  itemId);


                            mDatabase.child(Constants.DATABASE_PATH_ITEM).child(categoryId).child(itemId).setValue(foodItems);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            //display an error if no file is selected
            Toast.makeText(getApplicationContext(), "Fill all the blanks ", Toast.LENGTH_LONG).show();
        }

        //reload the view
        setupCustomCategoryView();
    }

    //Dialog box for Category update and delete
    private void categoryDialogBox(final String categoryId, final  String categoryName){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_popup_category,null);
        dialogBuilder.setView(dialogView);

        showCategoryName =(TextView) dialogView.findViewById(R.id.showCategoryName);
        closeCategoryDialog = (TextView) dialogView.findViewById(R.id.closeCategoryDialog);
        update_category_image = (ImageView) dialogView.findViewById(R.id.update_category_image);
        update_category_name = (EditText) dialogView.findViewById(R.id.update_category_name);
        btn_select_categoryImage = (Button) dialogView.findViewById(R.id.btn_select_categoryImage);
        btn_update_category = (Button) dialogView.findViewById(R.id.btn_update_category);
        btn_delete_category = (Button) dialogView.findViewById(R.id.btn_delete_category);

        showCategoryName.setText(categoryName);

        final AlertDialog popUp = dialogBuilder.create();
        popUp.show();

        closeCategoryDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popUp.dismiss();
            }
        });

        btn_select_categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryImgChooserUpdate();
            }
        });

        btn_update_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateFileCategory(categoryId);

            }
        });

        btn_delete_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCategory(categoryId);
            }
        });
    }

    //Dialog box for Item update and delete
    private void itemDialogBox(final String itemId, final String categoryId, final String itemName) {

        Log.d(TAG, "itemDialogBox: debug step 2: item dialog box");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_popup_item,null);
        dialogBuilder.setView(dialogView);

        showItemName =(TextView) dialogView.findViewById(R.id.showItemName);
        closeItemDialog = (TextView) dialogView.findViewById(R.id.closeItemDialog);
        update_item_image = (ImageView) dialogView.findViewById(R.id.update_item_image);
        update_item_name = (EditText) dialogView.findViewById(R.id.update_item_name);
        update_item_price = (EditText) dialogView.findViewById(R.id.update_item_price);
        btn_update_item = (Button) dialogView.findViewById(R.id.btn_update_item);
        btn_delete_item = (Button) dialogView.findViewById(R.id.btn_delete_item);
        btn_select_itemImage = (Button) dialogView.findViewById(R.id.btn_select_itemImage);

        showItemName.setText(itemName);

        final AlertDialog popUp = dialogBuilder.create();
        popUp.show();

        closeItemDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popUp.dismiss();
            }
        });

        btn_select_itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemImgChooserUpdate();
            }
        });

        btn_update_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateFileItem(itemId,categoryId);
            }
        });

        btn_delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteItem(itemId,categoryId);
            }
        });

    }

    private void deleteItem(String itemId, String categoryId) {

        DatabaseReference dR_item = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_ITEM).child(categoryId).child(itemId);

        dR_item.removeValue();
    }

    private void deleteCategory(String categoryId) {

        DatabaseReference dR_category = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_CATEGORY).child(categoryId);

        dR_category.removeValue();

        DatabaseReference dR_item = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_ITEM).child(categoryId);

        dR_item.removeValue();
    }



    private void sendCategoryId(final String categoryId) {
        btn_confirmItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFileItem(categoryId);

            }
        });
    }


    private void setupCustomItemView(final String categoryId) {

        GridView gridView = (GridView) findViewById(R.id.czGrid);
        CustomItemViewAdapter adapter = new CustomItemViewAdapter(this, item_name,item_image_url,item_price);
        gridView.setAdapter(adapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d(TAG, "onItemLongClick: debug step 1: item view.");

                String itemId = item_id.get(i);
                String itemName = item_name.get(i);

                itemDialogBox(itemId,categoryId,itemName);

                return true;
            }
        });

    }

    private void setupCustomCategoryView() {

        Log.d(TAG, "setupCustomCategoryView: started");

        ListView listView = (ListView)findViewById(R.id.czList);
        CustomCategoryViewAdapter adapter= new CustomCategoryViewAdapter(this, mOptions,mCategoryImgUrl);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String categoryId = mCategoryId.get(i);

                DatabaseReference myRef_items = mDatabase.child(Constants.DATABASE_PATH_ITEM).child(categoryId);

                //Read from Database
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
                        setupCustomItemView(categoryId);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                sendCategoryId(categoryId);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                String categoryId = mCategoryId.get(i);
                String categoryName = mOptions.get(i);

                categoryDialogBox(categoryId,categoryName);

                return true;
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == btn_categoryImage) {
            categoryImgChooser();
        } else if (view == btn_itemImage) {
            itemImgChooser();

        } else if (view == btn_confirmCategory) {
            uploadFileCategory();
        }
    }

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
                FoodOptions foodOptions = new FoodOptions();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    foodOptions.setCategoryImgUrl(ds.getValue(FoodOptions.class).getCategoryImgUrl());
                    foodOptions.setName(ds.getValue(FoodOptions.class).getName());
                    foodOptions.setFoodOptionsId(ds.getValue(FoodOptions.class).getFoodOptionsId());
                    mOptions.add(foodOptions.getName());
                    mCategoryImgUrl.add(foodOptions.getCategoryImgUrl());
                    mCategoryId.add(foodOptions.getFoodOptionsId());
                }
                setupCustomCategoryView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
