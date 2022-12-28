package com.example.android.birthdayreminder;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.birthdayreminder.database.PersonEntry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    final private ItemClickListener mItemClickListener;
    private Context mContext;
    private List<PersonEntry> mPersonEntries = new ArrayList<>();
    //private boolean todayBirthday;

    public PersonAdapter(Context context, ItemClickListener itemClickListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PersonViewHolder holder, int position) {
        PersonEntry personEntry = mPersonEntries.get(position);
        String lastName = personEntry.getLastName();
        String firstName = personEntry.getFirstName();

        int year = personEntry.getYear();
        int month = personEntry.getMonth();
        int day = personEntry.getDay();
        String birthdayDate = day + "/" + month + "/" + year;
        holder.lastNameTextView.setText(lastName);
        holder.firstNameTextView.setText(firstName);
        holder.birthdayTextView.setText(birthdayDate);

        int relation = personEntry.getRelation();
        GradientDrawable relationCircle = (GradientDrawable) holder.relationImgView.getBackground();
        int relationColor = getRelationColor(relation);
        relationCircle.setColor(relationColor);
    }

    private int getRelationColor(int relation) {
        int relationColor = 0;

        switch (relation) {
            case 1:
                relationColor = ContextCompat.getColor(mContext, R.color.kelly_green);
                break;
            case 2:
                relationColor = ContextCompat.getColor(mContext, R.color.corn_yellow);
                break;
            case 3:
                relationColor = ContextCompat.getColor(mContext, R.color.blue_gray);
                break;
            default:
                break;
        }
        return relationColor;
    }

    //Returns the number of items to display
    @Override
    public int getItemCount() {
        if (mPersonEntries == null) {
            return 0;
        }
        //we return how many items we want ot be shown in our recycler view, here is basically every item
        return mPersonEntries.size();

    }

    public void setPeople(List<PersonEntry> personEntries) {
        mPersonEntries = personEntries;
        notifyDataSetChanged();
    }

    public List<PersonEntry> getPeople() {
        return mPersonEntries;
    }

    //public boolean isTodayBirthday() {return todayBirthday;}

    //TODO 2 Maybe getItemCount  Can  Hel  pWith  This
//    public void showOnlyPeople(List<PersonEntry> personEntries, int relation) {
//
//        for (int i = 0; i < personEntries.size(); i++) {
//            List<PersonEntry> showPersonEntries = new ArrayList<>();
//            if (personEntries.get(i).getRelation() == 1) {
//
//            }
//        }
//    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lastNameTextView, firstNameTextView, birthdayTextView;
        ImageView relationImgView;

        public PersonViewHolder(View itemView) {
            super(itemView);

            lastNameTextView = itemView.findViewById(R.id.last_name_text_view);
            firstNameTextView = itemView.findViewById(R.id.first_name_text_view);
            birthdayTextView = itemView.findViewById(R.id.birthday_text_view);
            relationImgView = itemView.findViewById(R.id.relation_img_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mPersonEntries.get(getAbsoluteAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
