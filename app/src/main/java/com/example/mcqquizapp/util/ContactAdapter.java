package com.example.mcqquizapp.util;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcqquizapp.R;
import com.example.mcqquizapp.model.QuestionDataModel;

import java.util.List;

/**
 * A custom Adapter Class for the recyclerView.
 *
 * @Property questionData holding the fetched data model values.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.viewHolder> {

    // ArrayList of [QuestionDataModel] for holding the fetched Values.
    private final List<QuestionDataModel> questionData;

    //Constructor for initialising the List- questionData
    public ContactAdapter(List<QuestionDataModel> questionData) {
        this.questionData = questionData;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_layout, parent, false);
        Log.d("ContactAdapterClass", "Layout Inflated");
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("ContactAdapterClass", "Data Binding- item" + position);

        QuestionDataModel dataModel = questionData.get(position);
        holder.question.setText(dataModel.getQuestion());

        //below logic is for checking the status of Question is answered or not, and changing the Status respectively
        if (!dataModel.isQuestionAnswered()) {
            holder.status.setImageResource(R.drawable.ques_status_1);
        } else
            holder.status.setImageResource(R.drawable.ques_status_0);

        holder.view.setOnClickListener(view -> {
            // Calling the static function of Utils class in order to update the values and start the question details screen.
            Utils.viewModelClass.itemClicked(position);
            Utils.viewModelClass.setListDataUpdated(questionData);
            Utils.viewModelClass.startQuestionDetailsScreen();
        });

        // For setting the BookMarkStatus by fetching the updated values and changing the image for showing the different status.
        if (dataModel.isBookmarked) {
            holder.bookmark.setImageResource(R.drawable.bookmark_on);
        } else {
            holder.bookmark.setImageResource(R.drawable.bookmark_off);
        }
        Log.d("ContactAdapterClass", "Data Binding- item" + position + "Successful");
    }

    @Override
    public int getItemCount() {
        return questionData.size();
    }

    // View holder class for the contact adapter class.
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView question;
        ImageView status, bookmark;
        View view;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            question = itemView.findViewById(R.id.question_text_view);
            status = itemView.findViewById(R.id.status_image);
            bookmark = itemView.findViewById(R.id.bookmark_image);
        }
    }
}
