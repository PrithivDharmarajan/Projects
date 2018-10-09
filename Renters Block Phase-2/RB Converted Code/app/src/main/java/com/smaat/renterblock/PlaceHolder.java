package com.smaat.renterblock;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class PlaceHolder extends ArrayAdapter<String> implements Filterable {
	private ArrayList<String> resultList;

	private static final String LOG_TAG = "DemoAddress";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";

	private static final String OUT_JSON = "/json";

	Context context;

	String countryName;

	public PlaceHolder(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();

				if (constraint != null) {

					// Retrieve the auto complete results.

					try {
						resultList = autocomplete(constraint.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Assign the data to filter results

					filterResults.values = resultList;

					filterResults.count = resultList.size();

				}

				return filterResults;

			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {

					notifyDataSetChanged();

				} else {

					notifyDataSetInvalidated();

				}

			}

		};
		return filter;
	}

	@Override
	public String getItem(int position) {
		return resultList.get(position);
	}

	private ArrayList<String> autocomplete(String input) throws Exception {

		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;

		StringBuilder jsonResults = new StringBuilder();

		try {

			StringBuilder sb = new StringBuilder(PLACES_API_BASE

			+ TYPE_AUTOCOMPLETE + OUT_JSON);

			sb.append("?sensor=false&key="

			+ "AIzaSyB04zBN9P9zaXrUeWdwPGKAdwij3bslQPQ");

			// for current country.Get the country code by SIM

			// If you run this in emulator then it will get country name is
			// "us".

			String cName = getCountryCode();

			if (cName != null) {
				countryName = cName;
			} else {
				countryName = "in";
			}
			sb.append("&components=country:" + countryName);

			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());

			conn = (HttpURLConnection) url.openConnection();

			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			doTrustToCertificates();
			// Load the results into a StringBuilder

			int read;

			char[] buff = new char[1024];

			while ((read = in.read(buff)) != -1) {

				jsonResults.append(buff, 0, read);

			}

		} catch (MalformedURLException e) {

			Log.e(LOG_TAG, "Error processing Places API URL", e);

			return resultList;

		} catch (IOException e) {

			Log.e(LOG_TAG, "Error connecting to Places API", e);

			return resultList;

		} finally {

			if (conn != null) {

				conn.disconnect();

			}

		}

		try {

			// Create a JSON object hierarchy from the results

			JSONObject jsonObj = new JSONObject(jsonResults.toString());

			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results

			resultList = new ArrayList<String>(predsJsonArray.length());

			for (int i = 0; i < predsJsonArray.length(); i++) {

				resultList.add(predsJsonArray.getJSONObject(i).getString(

				"description"));

			}

		} catch (JSONException e) {

			Log.e(LOG_TAG, "Cannot process JSON results", e);

		}

		return resultList;

	}

	// function to identify the country code by SIM.

	private String getCountryCode() {

		TelephonyManager tel = (TelephonyManager) context

		.getSystemService(Context.TELEPHONY_SERVICE);

		return tel.getNetworkCountryIso();

	}

	public void doTrustToCertificates() throws Exception {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		URL url = new URL(PLACES_API_BASE);
		URLConnection con = url.openConnection();
		Reader reader = new InputStreamReader(con.getInputStream());
		while (true) {
			int ch = reader.read();
			if (ch == -1) {
				break;
			}
			System.out.print((char) ch);
		}
	}

}
