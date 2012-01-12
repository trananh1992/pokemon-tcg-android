package fr.codlab.cartes.adaptaters;

import java.util.ArrayList;

import fr.codlab.cartes.Principal;
import fr.codlab.cartes.R;
import fr.codlab.cartes.util.Extension;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class PrincipalExtensionAdapter extends BaseAdapter {
	private Context context;
	private Principal _principal;
	private ArrayList<Extension> _item;

	public PrincipalExtensionAdapter(Principal a,ArrayList<Extension> item) {
		_principal=a;
		context=a;
		_item=item;
	}

	public View getView(int pos, View inView, ViewGroup parent) {
		final int position=pos;
		View v = inView;
		if(v == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_extension, null);
		}
		TextView bTitle = (TextView) v.findViewById(R.id.extension_nom);
		bTitle.setText(_item.get(position).getNom());

		v.setOnClickListener(new OnClickListener(){
			//@Override
			public void onClick(View v) {
				_principal.onClick(_item.get(position).getNom(),
						_item.get(position).getId(),
						_item.get(position).getIntitule());
			}
		});

		TextView bInfos = (TextView) v.findViewById(R.id.extcollection);
		bInfos.setText(" "+_item.get(position).getProgression()+"/"+_item.get(position).getCount());
		bInfos = (TextView) v.findViewById(R.id.extpossedees);
		bInfos.setText(" "+_item.get(position).getPossedees());

		return(v);
	}

	//@Override
	public int getCount() {
		return _item.size();
	}

	//@Override
	public Object getItem(int pos) {
		return _item.get(pos);//_items[arg0];
	}

	//@Override
	public long getItemId(int pos) {
		return pos;//_items[arg0];
	}
}
