package com.example.android.birthdayreminder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.android.birthdayreminder.database.PersonEntry;

import java.util.Calendar;

public class AddPersonActivity extends AppCompatActivity {
    public static final String EXTRA_PERSON_ID = "extraPersonId";
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    private static final int DEFAULT_TASK_ID = -1;
    private int mPersonId = DEFAULT_TASK_ID;

    public static final int RELATION_FRIEND = 1;
    public static final int RELATION_FAMILY = 2;
    public static final int RELATION_OTHER = 3;

    EditText mLastNameEditText, mFirstNameEditText;
    DatePicker mDatePicker;
    RadioGroup mRadioGroup;
    Button mSaveButton;

    public static final String NOTIF_ID1 = "notif_id1";
    public static final String NOTIF_ID2 = "notif_id2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        mLastNameEditText = (EditText) findViewById(R.id.last_name_edit_text);
        mFirstNameEditText = (EditText) findViewById(R.id.first_name_edit_text);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mSaveButton = (Button) findViewById(R.id.save_button);


        PersonViewModel viewModel = new ViewModelProvider(this).get(PersonViewModel.class);

        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String lastName = mLastNameEditText.getText().toString();
                String firstName = mFirstNameEditText.getText().toString();
                int year = mDatePicker.getYear();
                int month = (mDatePicker.getMonth() + 1);
                int day = mDatePicker.getDayOfMonth();
                int relation = getRelationFromViews();
                final PersonEntry person = new PersonEntry(lastName, firstName, year, month, day, relation);

                if (mPersonId == DEFAULT_TASK_ID) {
                    viewModel.insert(person);
                } else {
                    person.setId(mPersonId);
                    viewModel.update(person);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    registerBirthdayNotification(person.getId(), day, month, lastName);
                }
                finish();
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mPersonId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_PERSON_ID)) {
            mSaveButton.setText(getString(R.string.update));

            if (mPersonId == DEFAULT_TASK_ID) {
                mPersonId = intent.getIntExtra(EXTRA_PERSON_ID, DEFAULT_TASK_ID);

                viewModel.getPersonById(mPersonId).observe(this, new Observer<PersonEntry>() {
                    @Override
                    public void onChanged(PersonEntry personEntry) {
                        viewModel.getPersonById(mPersonId).removeObserver(this);
                        populateUI(personEntry);
                    }
                });
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void registerBirthdayNotification(int id, int day, int month, String lastName) {
        Calendar c = Calendar.getInstance();
        //In the Calendar object January = 0; February = 1 ...
        //The month we get from the database has January = 1; February = 2 ...
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 18);
        c.set(Calendar.MINUTE, 38);
        c.set(Calendar.SECOND, 30);

//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.YEAR, 1);
//        }

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AddPersonActivity.this, AlertReceiver.class);
        intent.putExtra(AlertReceiver.LAST_NAME_KEY, lastName);
        String notifId2Str = String.valueOf(id);
        intent.putExtra(AlertReceiver.NOT_ID2_KEY, notifId2Str);

        //FLAG_UPDATE_CURRENT is needed in order to change the intent Extra every time, instead of having it remain the same
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddPersonActivity.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //on the birthday date
        mAlarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        Calendar c7 = Calendar.getInstance();
        c7.set(Calendar.MONTH, month-1);
        c7.set(Calendar.DAY_OF_MONTH, day);
        c7.set(Calendar.HOUR_OF_DAY, 22);
        c7.set(Calendar.MINUTE, 8);
        c7.set(Calendar.SECOND, 20);
        c7.add(Calendar.DAY_OF_YEAR, -7);

//        //        if (c.before(Calendar.getInstance())) {
////            c.add(Calendar.YEAR, 1);
////        }
//
        Intent intent7 = new Intent(AddPersonActivity.this, AlertReceiver.class);
        intent7.putExtra(AlertReceiver.LAST_NAME_KEY, lastName);
        String notifId1Str = String.valueOf(id + 100000);
        intent7.putExtra(AlertReceiver.NOT_ID1_KEY, notifId1Str);

        PendingIntent pendingIntent7 = PendingIntent.getBroadcast(AddPersonActivity.this, 2, intent7, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //on the birthday date
        mAlarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c7.getTimeInMillis(), pendingIntent7);

        Log.v("AddPersonActivity", "Alarm in AddPersonActivity is set");
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putInt(INSTANCE_TASK_ID, mPersonId);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void populateUI(PersonEntry person) {
        if (person == null) {
            return;
        }
        mLastNameEditText.setText(person.getLastName());
        mFirstNameEditText.setText(person.getFirstName());
        int year = person.getYear();
        //In the database the month is saved accordingly: January = 1; February = 2; ...
        //But in the datePicker: January = 0; February = 1; ... That's why we subtract 1
        int month = person.getMonth() - 1;
        int day = person.getDay();
        mDatePicker.updateDate(year, month, day);
        setRelationInViews(person.getRelation());
    }

    public int getRelationFromViews() {
        int relation = 1;
        int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.radButton1:
                relation = RELATION_FRIEND;
                break;
            case R.id.radButton2:
                relation = RELATION_FAMILY;
                break;
            case R.id.radButton3:
                relation = RELATION_OTHER;

        }
        return relation;
    }

    public void setRelationInViews(int relation) {
        switch (relation) {
            case RELATION_FRIEND:
                mRadioGroup.check(R.id.radButton1);
                break;
            case RELATION_FAMILY:
                mRadioGroup.check(R.id.radButton2);
                break;
            case RELATION_OTHER:
                mRadioGroup.check(R.id.radButton3);
        }
    }
}