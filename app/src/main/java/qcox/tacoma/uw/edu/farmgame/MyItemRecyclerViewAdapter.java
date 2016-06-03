package qcox.tacoma.uw.edu.farmgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import qcox.tacoma.uw.edu.farmgame.ItemListFragment.OnListFragmentInteractionListener;
import qcox.tacoma.uw.edu.farmgame.data.GameValues;
import qcox.tacoma.uw.edu.farmgame.data.PlayerValues;
import qcox.tacoma.uw.edu.farmgame.items.PlantItems;

import java.util.List;

/**
 * This Recycler View shows a list of all the items in the game and shows how many the player has of
 * each.
 *
 * {@link RecyclerView.Adapter} that can display a {@link PlantItems} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 *
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlantItems> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<PlantItems> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        if (mValues.get(position).imageResourceIndex != -1) {
            holder.mItemImageView.setImageResource(mValues.get(position).imageResourceIndex);
        }

        holder.mNameView.setText(mValues.get(position).name);
        holder.mAmountView.setText("You Have: " + GameValues.getCurrentPlayerValues().getItemAmount(mValues.get(position).name));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * used to recycle views to speed up
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mItemImageView;
        public final TextView mNameView;
        public final TextView mAmountView;
        public PlantItems mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mItemImageView = (ImageView) view.findViewById(R.id.image);
            mNameView = (TextView) view.findViewById(R.id.name);
            mAmountView = (TextView) view.findViewById(R.id.amount);
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
