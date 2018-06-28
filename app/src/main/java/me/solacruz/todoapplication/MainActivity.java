package me.solacruz.todoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //declaring "stateful" objects (idk what stateful means)
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter; //wires the items to listView?
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gets a reference to the ListView
        lvItems = (ListView) findViewById(R.id.lvItems);
        //initializes the items list
        readItems();
        //initializes the adapter through the item list (?)
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //connects the adapter to the view on screen
        lvItems.setAdapter(itemsAdapter);

        //setup the lsitener on creation
        setupListViewListener();
    }

    private void setupListViewListener(){
        //set the ListView's "itemLongClickListener" (?)
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                //removes item at the given index from the list
                items.remove(position);
                //tells adapter that the dataset changed'
                itemsAdapter.notifyDataSetChanged();
                //stores the updated list
                writeItems();

                //Log.i("MainActivity", "Removed item"+ position);
                //return true to tell the framework that the long click was consumed
                return true;
            }

        });
    }

    public void onAddItem(View v){
        //gets a ref to the "EditText" that's made with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //grabs the content in EditText as a String
        String itemText= etNewItem.getText().toString();
        //adds the item to the list through the adapter'
        itemsAdapter.add(itemText);
        //clears the EditTExt by setting it to empty
        etNewItem.setText("");
        //Toast notification for user
        Toast.makeText(getApplicationContext(), "Item added to list!", Toast.LENGTH_SHORT).show();
        //store the updated list
        writeItems();
    }

    //returns the file in which the data is stores
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    //read the items from the file system
    private void readItems(){
        try{
            //make an array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            //print the error to the console
            e.printStackTrace();
            //load an empty list
            items = new ArrayList<>();
        }
    }

    //write the items to the filesystem
    private void writeItems(){
        try {
            //save the item list as a line-delimited (?) text file
            FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
        }
    }
}
