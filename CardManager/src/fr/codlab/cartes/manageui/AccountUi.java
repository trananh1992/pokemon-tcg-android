package fr.codlab.cartes.manageui;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import fr.codlab.cartes.R;
import fr.codlab.cartes.bdd.SGBD;
import fr.codlab.cartes.fragments.InformationScreenFragment;
import fr.codlab.cartes.updater.IUpdater;
import fr.codlab.cartes.updater.Updater;

public class AccountUi implements IUpdater{
	private static Updater _updater = null;
	private Context c = null;
	private InformationScreenFragment _activity_main;
	private static ProgressDialog _progress;

	private AccountUi(){

	}

	public AccountUi(InformationScreenFragment activity_main, View v){
		_activity_main = activity_main;
		c = v.getContext();
		implement(v);
		_progress = null;
		setThis();
	}

	private void setThis(){
		if(_updater == null)
			_updater = new Updater(this);
		_updater.setParent(this);
	}

	public void implement(final View v){
		setThis();
		c = v.getContext();

		Button button = (Button)v.findViewById(R.account.create);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				String login = ((TextView)v.findViewById(R.account.nickname)).getText().toString();
				String password = ((TextView)v.findViewById(R.account.password)).getText().toString();
				_updater.create(c, login, password);
				createWaiter(c, "Creating","please wait");
			}
		});
		
		button = (Button)v.findViewById(R.account.auth);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				String login = ((TextView)v.findViewById(R.account.nickname)).getText().toString();
				String password = ((TextView)v.findViewById(R.account.password)).getText().toString();
				_updater.authent(c, login, password);
				createWaiter(c, "Authent","please wait");
			}
		});
		
		button = (Button)v.findViewById(R.account.down);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				SGBD s = new SGBD(c);
				s.open();
				String encoded_test = s.getEncodedPossessions();
				
				s.createfromEncodedPossessions(encoded_test);
				s.close();
			}
		});
		button = (Button)v.findViewById(R.account.up);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				SGBD s = new SGBD(c);
				s.open();
				Log.d("GET"," "+s.getEncodedPossessions());
				s.close();
			}
		});
		
	}

	String title;
	String text;
	@Override
	public void notifyWork() {
		createWaiter(c,title, text);
	}

	@Override
	public byte[] receiveData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void error() {
		stopWaiter();
		if(_activity_main.getActivity() != null)
			_activity_main.getActivity().runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, "Error !", Toast.LENGTH_LONG).show();
				}
			});
	}

	@Override
	public void createWaiter(Context c, String title, String text) {
		if(_progress == null){
			this.title = title;
			this.text = text;
			_progress = new ProgressDialog(c);
			_progress.setTitle(title);
			_progress.setMessage(text);
			_progress.setCancelable(false);
			_progress.show();
		}
	}

	@Override
	public void stopWaiter() {
		if(_progress != null){
			_progress.dismiss();
			_progress = null;
		}
	}

	@Override
	public void okcreate() {
		stopWaiter();
		if(_activity_main.getActivity() != null)
			_activity_main.getActivity().runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, "Create !", Toast.LENGTH_LONG).show();
				}
			});
	}

	@Override
	public void okauth() {
		stopWaiter();
		if(_activity_main.getActivity() != null)
			_activity_main.getActivity().runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, "Successfully authenticate !", Toast.LENGTH_LONG).show();
				}
			});
	}
}
