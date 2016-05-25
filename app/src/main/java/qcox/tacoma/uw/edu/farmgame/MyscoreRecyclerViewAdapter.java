package qcox.tacoma.uw.edu.farmgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import qcox.tacoma.uw.edu.farmgame.HighScoreListFragment.OnListFragmentInteractionListener;
import qcox.tacoma.uw.edu.farmgame.highscore.HighScore;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HighScore} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 * use adapter to handle single list view display
 * @author james
 * @version 1.0
 * @since 2016-5-4
 */
public class MyscoreRecyclerViewAdapter extends RecyclerView.Adapter<MyscoreRecyclerViewAdapter.ViewHolder> {

    private final List<HighScore> mValues;
    private final OnListFragmentInteractionListener mListener;

    /**
     * constructor
     * @param highScores
     * @param listener
     */
    public MyscoreRecyclerViewAdapter(List<HighScore> highScores, OnListFragmentInteractionListener listener) {
        mValues = highScores;
        mListener = listener;
    }

    /**
     * {@inheritDoc}
     * set single list view layout as its parent but not link to its parent(set false)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_score, parent, false);
        return new ViewHolder(view);
    }

    /**
     * put highscore and username to the correct place
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getHighscore());
        holder.mContentView.setText(mValues.get(position).getUsername());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
     * adapter view helper class
     * @author james
     * @version 1.0
     * @since 2016-5-4
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public HighScore mItem;

        /**
         * constructor
         * set single line of the view
         * @param view
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
