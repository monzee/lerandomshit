package ph.codeia.lerandomshit.leddit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.codeia.lerandomshit.R;

/**
 * This file is a part of the Le Random Shit project.
 */
public class FrontPageAdapter extends RecyclerView.Adapter<FrontPageAdapter.StoryView> {
    public static class StoryView extends RecyclerView.ViewHolder {
        @BindView(R.id.the_title)
        TextView title;

        @BindView(R.id.the_op)
        TextView by;

        public StoryView(View itemView) {
            super(itemView);
        }
    }

    @Inject
    LayoutInflater inflater;

    @Inject
    List<Hn.Story> items;

    @Inject
    FrontPageContract.Interaction user;

    @Inject
    public FrontPageAdapter() {}

    @Override
    public StoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_leddit_story, parent, false);
        StoryView holder = new StoryView(view);
        ButterKnife.bind(holder, view);
        view.setOnClickListener(v -> user.didChooseStory(holder.getAdapterPosition()));
        return holder;
    }

    @Override
    public void onBindViewHolder(StoryView holder, int position) {
        Hn.Story story = items.get(position);
        holder.title.setText(story.title);
        holder.by.setText(story.by);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
