package Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public  class ConexionInternet
{
	   private Context c;
	   
	    public ConexionInternet(Context c)  //Debe pasar la referencia a la activity activa
	    {
	    	this.c=c;
	    }
	   
	    public  Boolean estaConectado(){
		 return  conectadoWifi()||conectadoRedMovil();
		}
		 
	    private  Boolean conectadoWifi(){
		ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
		NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (info != null) {
		if (info.isConnected()) {
		return true;
		}
		}
		}
		return false;
		}
		 
		private  Boolean conectadoRedMovil(){
		ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
		NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (info != null) {
		if (info.isConnected()) {
		return true;
		}
		}
		}
		return false;
		}


		

}
