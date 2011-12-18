package fr.codlab.cartes.util;

import android.app.Activity;
import android.app.ProgressDialog;

public class Downloader implements IDownloadFile {
	private Activity _parent;
	private static ProgressDialog _download_progress;
	private static DownloadFile downloadFile;
	private String _url;
	private Boolean _finish;

	Downloader(Activity parent, String url){
		_parent = parent;
		_url = url;
		_finish = false;

		downloadImages();
	}

	public void downloadCreate(){
		if(!_finish){
			//Log.d("create","show");
			
			_download_progress = new ProgressDialog(_parent);
			_download_progress.setMessage("Downloading");
			_download_progress.setIndeterminate(false);
			_download_progress.setCancelable(false);
			_download_progress.setMax(100000);
			_download_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			_download_progress.show();
		}
	}

	public void downloadLoad(){
		if(downloadFile != null){
			this.downloadCreate();
		}
	}

	public void downloadQuit(){
		if(_download_progress != null)
			_download_progress.dismiss();
	}

	private void downloadImages(){
		if(_download_progress == null && !_finish){
			downloadCreate();
			downloadFile = new DownloadFile(this, "card_images.zip");
			downloadFile.execute(_url);
		}
	}

	public void receiveProgress(String msg, Double args){
		String str = args.toString();
		if(_download_progress != null && !_finish){
			_download_progress.setMessage(str.subSequence(0, str.length()-1)+msg);
			_download_progress.setProgress((int)(args*1000));
		}
	}

	public void onPost(Long result){
		_download_progress.dismiss();
		_download_progress = null;
		downloadFile = null;
		_finish = true;
	}
}
