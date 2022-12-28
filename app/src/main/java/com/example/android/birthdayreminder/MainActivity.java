package com.example.android.birthdayreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.example.android.birthdayreminder.database.PersonEntry;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PersonAdapter.ItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    FloatingActionButton fabButton;

    private PersonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerViewPeople);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new PersonAdapter(this, this);

        mRecyclerView.setAdapter(mAdapter);

        fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddPersonActivityIntent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivity(startAddPersonActivityIntent);
            }
        });

        PersonViewModel viewModel = new ViewModelProvider(this).get(PersonViewModel.class);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull final RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                List<PersonEntry> people = mAdapter.getPeople();
//                //for loop in order to avoid id-notification difficulties
//                for (int i = 0; i<people.size(); i++) {
//                    people.get(i).setId(i);
//                }
                viewModel.delete(people.get(position));
                Log.v("MainActivity", "ItemDeleted");
            }
        }).attachToRecyclerView(mRecyclerView);

        viewModel.getPeople().observe(this, new Observer<List<PersonEntry>>() {
            @Override
            public void onChanged(List<PersonEntry> personEntries) {

                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");

                Collections.sort(personEntries, new Comparator<PersonEntry>() {
                    @Override
                    public int compare(PersonEntry personEntry1, PersonEntry personEntry2) {
                        return personEntry1.getLastName().compareTo(personEntry2.getLastName());
                    }
                });

                //notifyDataSetChanged is done in the setPeople() method
                mAdapter.setPeople(personEntries);

            }
        });
    }


    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
        intent.putExtra(AddPersonActivity.EXTRA_PERSON_ID, itemId);
        startActivity(intent);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemId = item.getItemId();
//        switch (itemId) {
//            case R.id.show_all: ;
//            break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}