package com.tucker.nat20.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tucker.nat20.R;
import com.tucker.nat20.utils.SpellListUtils;

/**
 * Created by eli on 2/7/2018.
 */

public class SpellListAdapter extends RecyclerView.Adapter<SpellListAdapter.RestResultViewHolder>
{

    private SpellListUtils.SpellResponse spellResponse;

    private OnSpellClickListener onSpellClickListener;

    public interface OnSpellClickListener
    {
        void onSpellClick(String url);
    }

    public SpellListAdapter(OnSpellClickListener onSpellClickListener)
    {
        this.onSpellClickListener = onSpellClickListener;
    }

    public void updateSearchResults(SpellListUtils.SpellResponse newSpellResponse)
    {
        spellResponse = newSpellResponse;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount()
    {
        if(spellResponse != null)
            return spellResponse.spellResults.size();
        else
            return 0;
    }


    @Override
    public RestResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.spell_list_item, parent, false);
        return new RestResultViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RestResultViewHolder holder, int position)
    {
        holder.bind(spellResponse.spellResults.get(position).name);
    }

    class RestResultViewHolder extends RecyclerView.ViewHolder
    {
        private TextView result;


        public RestResultViewHolder(View itemView)
        {
            super(itemView);
            result = (TextView)itemView.findViewById(R.id.tv_spell_list_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = spellResponse.spellResults.get(getAdapterPosition()).url;
                    onSpellClickListener.onSpellClick(url);
                }
            });
        }


        public void bind(String item)
        {
            result.setText(item);
        }
    }
}
