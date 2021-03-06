package org.pathfinderfr.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.pathfinderfr.R;
import org.pathfinderfr.app.database.entity.DBEntity;
import org.pathfinderfr.app.database.entity.FavoriteFactory;
import org.pathfinderfr.app.database.entity.Feat;
import org.pathfinderfr.app.database.entity.Skill;
import org.pathfinderfr.app.database.entity.Spell;

import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final MainActivity mParentActivity;
    private final List<DBEntity> mValues;
    private final boolean mTwoPane;
    private String factoryId;
    private boolean showNameLong;

    SimpleItemRecyclerViewAdapter(MainActivity parent,
                                  List<DBEntity> items,
                                  boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
        factoryId = null;
        showNameLong = false;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }
    public void setShowNameLong(boolean nameLong) { this.showNameLong = nameLong; }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DBEntity item = (DBEntity) view.getTag();

            Context context = view.getContext();
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getId());
            intent.putExtra(ItemDetailFragment.ARG_ITEM_FACTORY_ID, item.getFactory().getFactoryId());

            context.startActivity(intent);
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));

        DBEntity entity = mValues.get(position);
        String name = showNameLong ? entity.getNameLong() : entity.getName();

        // TODO: find a way to better handle this special case.
        // Favorites stores long names as names because details are not available
        if(FavoriteFactory.FACTORY_ID.equalsIgnoreCase(factoryId) && !showNameLong && !(entity instanceof Skill)) {
            int idx = name.indexOf('(');
            if(idx > 0) {
                name = name.substring(0, idx);
            }
        }

        holder.mContentView.setText(name);
        holder.itemView.setTag(entity);
        holder.itemView.setOnClickListener(mOnClickListener);

        ImageView icon = (ImageView) holder.itemView.findViewById(R.id.itemIcon);
        if(entity instanceof Feat) {
            icon.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_item_icon_feat, holder.itemView.getContext().getTheme()));
        } else if(entity instanceof Skill) {
            icon.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_item_icon_skill, holder.itemView.getContext().getTheme()));
        } else if(entity instanceof Spell) {
            icon.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_item_icon_spell, holder.itemView.getContext().getTheme()));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            //mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}