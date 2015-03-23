package com.meme.moi.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Skyro on 10/02/2015.
 */
public class LivreAdapter extends BaseAdapter {
    List<Livre> biblio;
    // LayoutInflater aura pour mission de charger notre fichier XML
    LayoutInflater inflater;
    public LivreAdapter(Context context,List<Livre> objects) {
        inflater = LayoutInflater.from(context);
        this.biblio = objects;
    }
    /**
     * Génère la vue pour un objet
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.livre_item, null);
        TextView tvTitre = (TextView) convertView.findViewById(R.id.txtTitre);
        TextView tvAuteur = (TextView) convertView.findViewById(R.id.txtAuteur);
        Livre livre = biblio.get(position);
        tvTitre.setText(livre.getTitre());
        tvAuteur.setText(livre.getAuteur());
        return convertView;
    }
    /**
     * Retourne le nombre d'éléments
     */
    @Override
    public int getCount() {
        return biblio.size();
    }
    /**
     * Retourne l'item à la position
     */
    @Override
    public Livre getItem(int position) {
        return biblio.get(position);
    }
    /**
     * Retourne la position de l'item
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}