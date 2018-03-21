package com.tucker.nat20.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tucker.nat20.R;
import com.tucker.nat20.utils.SpellListUtils;

import java.util.ArrayList;

/**
 * Created by eli on 3/20/2018.
 */

public class SavedSpellAdapter extends RecyclerView.Adapter<SavedSpellAdapter.SavedSpellViewHolder> {
    private ArrayList<SpellListUtils.SpellResult> spellArrayList;
    private OnSpellItemClickListener spellClickListener;

    public SavedSpellAdapter(OnSpellItemClickListener newSpellClickListener){
        spellClickListener = newSpellClickListener;
    }


    public interface OnSpellItemClickListener {
        void onSpellItemClick(String url);
    }


    public void updateResults(ArrayList<SpellListUtils.SpellResult> newSpellArrayList)
    {
        spellArrayList = newSpellArrayList;
        notifyDataSetChanged();
    }


    @Override
    public SavedSpellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.spell_list_item, parent, false);
        return new SavedSpellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedSpellViewHolder holder, int position) {
        holder.bind(spellArrayList.get(position), spellClickListener);
    }

    @Override
    public int getItemCount() {
        if (spellArrayList != null)
            return spellArrayList.size();
        else
            return 0;
    }


    class SavedSpellViewHolder extends RecyclerView.ViewHolder
    {
        private TextView spellTV;

        public SavedSpellViewHolder(View itemView)
        {
            super(itemView);
            spellTV = (TextView)itemView.findViewById(R.id.tv_spell_list_item);
        }


        public void bind(SpellListUtils.SpellResult spell, final OnSpellItemClickListener listener)
        {
            spellTV.setText(spell.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SpellListUtils.SpellResult spell = spellArrayList.get(getAdapterPosition());
                    listener.onSpellItemClick(spell.url);
                }
            });
        }
    }
}
