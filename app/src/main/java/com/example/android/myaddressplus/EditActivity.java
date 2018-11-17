package com.example.android.myaddressplus;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = EditActivity.class.getSimpleName();
    private RadioGroup mRadioGroup;
    private RadioButton mMr;
    private EditText mFirstname, mLastname, mAddress, mPostcode;
    private Spinner mProvince, mDegination;
    private Button mSubmit;
    private Uri addressUri;
    @Override
    protected void onCreate(Bundle bundle) {
        Log.i(TAG, "SangaeLee(147948186) onCreate");
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit);
        initializeUI();
          //getInttent
        Bundle extras = getIntent().getExtras();
        // Check from the saved Instance
        addressUri = (bundle == null) ? null : (Uri) bundle.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            addressUri = extras.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);
            fillData(addressUri);
        }
    }

    private void initializeUI() {
        Log.i(TAG, "SangaeLee(147948186) initializeUI");
        mFirstname = findViewById(R.id.firstname);
        mLastname = findViewById(R.id.lastname);
        mAddress = findViewById(R.id.address);
        mPostcode = findViewById(R.id.postcode);
        mProvince = findViewById(R.id.spinner_province);
        mDegination = findViewById(R.id.spinner_designation);
        mSubmit = findViewById(R.id.sumit_button);

        ArrayAdapter<CharSequence> mradapter = ArrayAdapter.createFromResource(EditActivity.this,
                R.array.designationarray, android.R.layout.simple_spinner_item);
        mradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDegination.setAdapter(mradapter);
        mDegination.setOnItemSelectedListener(EditActivity.this);
        //mDegination.setSelection(0, true);
        // View v = mDegination.getSelectedView();
        //((TextView)v).setTextSize();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditActivity.this,
                R.array.provincearray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mProvince.setAdapter(adapter);
        mProvince.setOnItemSelectedListener(EditActivity.this);
    }

    private void fillData(Uri uri) {
        Log.i(TAG, "SangaeLee(147948186) fillData");
        String[] projection = { AddressTableHandler.Degination,
                                AddressTableHandler.FirstName,
                                AddressTableHandler.LastName,
                                AddressTableHandler.Address,
                                AddressTableHandler.Province,
                                AddressTableHandler.Country,
                                AddressTableHandler.Postcode };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String degination = cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.Degination));

            for (int i = 0; i < mDegination.getCount(); i++) {

                String s = (String) mDegination.getItemAtPosition(i);
                if (s.equalsIgnoreCase(degination)) {
                    mDegination.setSelection(i);
                }
            }

            String province = cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.Province));

            for (int i = 0; i < mProvince.getCount(); i++) {

                String s = (String) mProvince.getItemAtPosition(i);
                if (s.equalsIgnoreCase(province)) {
                    mProvince.setSelection(i);
                }
            }

            mFirstname.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.FirstName)));
            mLastname.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.LastName)));
            mAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.Address)));
            mPostcode.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.Postcode)));

            // Always close the cursor
            cursor.close();
        }
    }

    public void OnClickSubmitButton(View view) {
        Log.i(TAG, "SangaeLee(147948186) OnClickSubmitButton");
        if (TextUtils.isEmpty(mFirstname.getText().toString())) {
            Toast.makeText(this, "Input Data, please!", Toast.LENGTH_LONG).show();
            //return;
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "SangaeLee(147948186) onItemSelected");
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "SangaeLee(147948186)onSaveInstanceState");
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE, addressUri);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "SangaeLee(147948186) onPause");
        super.onPause();
        saveState();
    }

    private void saveState() {
        Log.i(TAG, "SangaeLee(147948186) saveState");
        String firstName = mFirstname.getText().toString();
        String lastName = mLastname.getText().toString();
        String address = mAddress.getText().toString();
        String postcode = mPostcode.getText().toString();
        String designation = String.valueOf(mDegination.getSelectedItem());
        String province = String.valueOf(mProvince.getSelectedItem());

        if(firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || postcode.isEmpty()) {
            Toast.makeText(this, "Input Data, please!", Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(AddressTableHandler.Degination, designation);
        values.put(AddressTableHandler.FirstName, firstName);
        values.put(AddressTableHandler.LastName, lastName);
        values.put(AddressTableHandler.Address, address);
        values.put(AddressTableHandler.Province, province);
        values.put(AddressTableHandler.Country, "CANADA");
        values.put(AddressTableHandler.Postcode, postcode);

        if (addressUri == null) {
            // Insert
            addressUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        } else {
            // edit
            getContentResolver().update(addressUri, values, null, null);
        }
    }

}
