package com.rohindh.babyneeds;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rohindh.babyneeds.data.DataBaseHandler;
import com.rohindh.babyneeds.model.Items;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button savebtn;
    private EditText item;
    private EditText size;
    private EditText color;
    private EditText quantity;
    private DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseHandler = new DataBaseHandler(this);
        List<Items> itemsList = dataBaseHandler.getAllItems();
        for (Items item : itemsList){
            Log.d("details","name"+item.getItemName());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createpopup();
            }
        });
    }

    private void createpopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        savebtn = view.findViewById(R.id.save_Btn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem(view);

            }
        });
        item = view.findViewById(R.id.baby_item);
        size =view.findViewById(R.id.item_size);
        color = view.findViewById(R.id.item_color);
        quantity = view.findViewById(R.id.item_quantity);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.getText().toString().isEmpty()
                        && !color.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()
                        && !size.getText().toString().isEmpty()) {
                    saveItem(view);
                }else {
                    Snackbar.make(view, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }}
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();


    }

    private void saveItem(View view) {
        //saves the data using database handler
        Items items = new Items();
        String newItem = item.getText().toString().trim();
        String newItemColor =color.getText().toString();
        int newItemQty = Integer.parseInt(quantity.getText().toString());
        int newItemSize = Integer.parseInt(size.getText().toString());

        items.setItemColor(newItemColor);
        items.setItemName(newItem);
        items.setItemQuantity(newItemQty);
        items.setItemSize(newItemSize);
        Log.d("works fine","upto additem call to dbh");
        dataBaseHandler.additem(items);
        Snackbar.make(view,"item added successfully",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,ListActivity.class));

            }
        },1200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
