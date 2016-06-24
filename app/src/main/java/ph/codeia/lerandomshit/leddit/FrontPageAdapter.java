package ph.codeia.lerandomshit.leddit;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindColor;
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

    @Inject @Named("top_posts")
    List<FrontPage.Post> posts;

    @Inject
    FrontPage.Interaction user;

    @BindColor(R.color.colorPrimary)
    @ColorInt int pendingColor;

    @Inject
    public FrontPageAdapter() {}

    @Inject
    void init(Activity a) {
        ButterKnife.bind(this, a);
    }

    @Override
    public StoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_leddit_story, parent, false);
        StoryView holder = new StoryView(view);
        ButterKnife.bind(holder, view);
        view.setOnClickListener(v -> user.didChoosePost(holder.getAdapterPosition()));
        return holder;
    }

    @Override
    public void onBindViewHolder(StoryView holder, int position) {
        FrontPage.Post post = posts.get(position);
        View v = holder.itemView;
        if (post != null) {
            v.setBackgroundColor(Color.TRANSPARENT);
            holder.title.setText(post.getTitle());
            holder.by.setText(post.getBy());
        } else {
            v.setBackgroundColor(pendingColor);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
