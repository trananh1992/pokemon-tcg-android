package fr.codlab.cartes.adaptaters;


import fr.codlab.cartes.IExtensionListener;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.Rarity;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;
import fr.codlab.cartes.views.CardImage;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class ExtensionListeAdapter extends BaseAdapter {
	private ExtensionListAdapterUtil _util;
	
	private Context _context;
	public CardImage _vueClic=null;
	
	public ExtensionListeAdapter(IExtensionListener a, Context context, Extension item) {
		_util = new ExtensionListAdapterUtil(a, item, context);
		_context=context;
	}



	public View getView(final int pos, View inView, ViewGroup parent) {
		if(inView == null){
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inView = inflater.inflate(R.layout.image_list, null);//item_pager_extension
		}
		
		/*ItemExtensionListingCardPagerAdapter adapter = new ItemExtensionListing(_context, _util, pos);
		TitlePageIndicator indicator =
				(TitlePageIndicator)findViewById( R.id.indicator );
		ViewPager pager = 
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);*/
		_util.populate(inView, pos);
		return(inView);
	}
	@Override
	public int getCount() {
		return _util.getCount();
	}

	@Override
	public Object getItem(int pos) {
		return _util.getItem(pos);
	}

	@Override
	public long getItemId(int pos) {
		return _util.getItemId(pos);
	}
}
