package qcox.tacoma.uw.edu.farmgame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import qcox.tacoma.uw.edu.farmgame.data.GameValues;
import qcox.tacoma.uw.edu.farmgame.data.PlayerValues;
import qcox.tacoma.uw.edu.farmgame.items.PlantItems;


/**
 * This is the fragment that the user will see after they click on an item in the item list.  It shows
 * details about the item.
 *
 * @author James, Quinn
 * @version 1.0
 * @since 2016-5-4
 */
public class ItemDetailFragment extends Fragment {

    public static final String ARG_POSITION = "POSITION" ;

    public ItemDetailFragment() {
        // Required empty public constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateItemView(args.getInt(ARG_POSITION));
        }
    }

    /**
     * This is where the actual views of the fragment are updated with the appropriate details.
     *
     * @param pos this is the position in the recycle viewer that the item is in.
     */
    public void updateItemView(int pos) {
        PlantItems aPlantItem = GameValues.getPlantItem(pos);
        if (aPlantItem.imageResourceIndex != -1) {
            ImageView itemImageView = (ImageView) getActivity().findViewById(R.id.item_detail_image);
            itemImageView.setImageResource(aPlantItem.imageResourceIndex);
            //getActivity().findViewById(R.id.fragment_item_detail).setBackground(itemImageView.getDrawable());
            getActivity().findViewById(R.id.fragment_item_detail).setBackgroundDrawable(itemImageView.getDrawable());
        }
        TextView moneyTextView = (TextView) getActivity().findViewById(R.id.item_detail_money_amount);
        int money = GameValues.getCurrentPlayerValues().getMoney();
        moneyTextView.setText(String.valueOf(money));
        TextView itemNameTextView = (TextView) getActivity().findViewById(R.id.item_detail_name);
        itemNameTextView.setText(aPlantItem.name);
        TextView itemShortDescTextView = (TextView) getActivity().findViewById(R.id.item_detail_short_desc);
        itemShortDescTextView.setText(aPlantItem.description);
        ((TextView) getActivity().findViewById(R.id.item_detail_amount))
                .setText("You currently have: " + GameValues.getCurrentPlayerValues().getItemAmount(aPlantItem.name));
        TextView itemLongDescTextView = (TextView) getActivity().findViewById(R.id.item_detail_long_desc);
        itemLongDescTextView.setText("");
        TextView itemBuyCostTextView = (TextView) getActivity().findViewById(R.id.item_detail_buy_cost);
        itemBuyCostTextView.setText("Cost to Buy: " + aPlantItem.buyCost);
        TextView itemSellCostTextView = (TextView) getActivity().findViewById(R.id.item_detail_sell_cost);
        itemSellCostTextView.setText("Cost to Sell: " + aPlantItem.sellCost);
//        ((TextView) getActivity().findViewById(R.id.item_detail_pos)).setText(pos);

    }


}
