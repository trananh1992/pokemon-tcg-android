package fr.codlab.cartes.updater;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class Updater implements IAuthHttp, ICreateAccountHttp, IDownloadHttp, IUploadHttp{
	private IUpdater _parent;
	private String _login;
	private String _password;
	private String _encrypted_password;
	private int _working;

	public Updater(IUpdater parent){
		setParent(parent);
	}

	private boolean isWorking(){
		return _working == 1;
	}

	public void setParent(IUpdater parent){
		_parent = parent;
		if(isWorking())
			_parent.notifyWork();
	}

	public void stop(){
		_working = 0;
	}

	public void create(Context c, String login, String password){
		CreateAccountHttp a = new CreateAccountHttp(c, this, login, password);
		a.compute();
	}

	public void authent(Context c, String login, String password){
		_login = login;
		_password = password;
		AuthHttp a = new AuthHttp(c, this, login, password);
		a.compute();
	}

	@Override
	public void onCreateAccountSuccess(String password) {
		_encrypted_password = password;
		_parent.okcreate();
	}

	@Override
	public void onCreateAccountFailure(String text) {
		_parent.error();	
	}

	@Override
	public void onAuthSuccess(String password) {
		_parent.okauth();
	}

	@Override
	public void onAuthFailure(String text) {
		_parent.error();
	}

	@Override
	public void onUploadSuccess(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUploadFailure(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadSuccess(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadFailure(String text) {
		// TODO Auto-generated method stub

	}

}
