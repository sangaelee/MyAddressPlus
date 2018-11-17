package com.example.android.myaddressplus;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ACTIVITY_INSERT = 0;
    private static final int ACTIVITY_EDIT = 1;

    ListView mylist;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "SangaeLee(147948186) onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_main);
        mylist = (ListView)findViewById(R.id.address_list);

        //show the Logo
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.mailbox1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        fillData();
        registerForContextMenu(mylist);
    }


    private void fillData(){
        Log.i(TAG, "SangaeLee(147948186) fillData");
        String[] from = new String[] {AddressTableHandler.FirstName };

        int[] to = new int[]{R.id.name};
        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_row_item, null, from, to, 0);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(i, ACTIVITY_EDIT);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "SangaeLee(147948186) onCreateLoader");
        String[] projection = {
                AddressTableHandler.Address_ID, AddressTableHandler.FirstName
        };
        CursorLoader cursorLoader = new CursorLoader(this, MyContentProvider.CONTENT_URI,
                projection, null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "SangaeLee(147948186) onLoadFinished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG, "SangaeLee(147948186) onLoaderReset");
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "SangaeLee(147481186) onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "SangaeLee(147481186) onOptionsItemSelected");
        switch(item.getItemId()) {
            case R.id.menu_insert:
                Intent addIntent = new Intent(this, EditActivity.class);
                startActivityForResult(addIntent, ACTIVITY_INSERT);
                return true;
            case R.id.menu_about:
                createDialog(R.string.about_content);
                break;
            case R.id.menu_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "SangaeLee(147948186) onActivityResult");
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        Log.i(TAG, "SangaeLee(147948186) onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Select The Action");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "SangaeLee(147948186) onContextItemSelected");
        if (item.getItemId() == R.id.menu_delete) {
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + info.id);
            getContentResolver().delete(uri, null, null);
            Toast.makeText(this, "Address is deleted.", Toast.LENGTH_SHORT).show();
            fillData();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void createDialog(int textId) {
        Log.i(TAG, "SangaeLee(147948186) createDialog");
        AboutFragment newDialog = AboutFragment.newInstant(textId);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        newDialog.show(transaction,"fragment");

    }
}
