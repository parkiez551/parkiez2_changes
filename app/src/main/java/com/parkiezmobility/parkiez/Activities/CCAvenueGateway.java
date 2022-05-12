package com.parkiezmobility.parkiez.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.parkiezmobility.parkiez.Database.MyOpenHelper;
import com.parkiezmobility.parkiez.Entities.UserEntity;
import com.parkiezmobility.parkiez.R;
import com.parkiezmobility.parkiez.utility.AvenuesParams;
import com.parkiezmobility.parkiez.utility.Constants;
import com.parkiezmobility.parkiez.utility.RSAUtility;
import com.parkiezmobility.parkiez.utility.ServiceHandler;
import com.parkiezmobility.parkiez.utility.ServiceUtility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CCAvenueGateway extends Activity {
    private ProgressDialog dialog;
    final Activity activity = this;
    private ProgressDialog progressDialog;
    UserEntity user;
    Intent mainIntent;
    String html, encVal;

    final private String ACCESS_CODE = "AVDW71EF51BT16WDTB";
    final private String MERCHANT_ID = "136838";
    final private String REDIRECT_URL = "http://www.clearcredit.store/ccavResponseHandler.jsp";
    final private String CANCEL_URL = "http://www.clearcredit.store/ccavResponseHandler.jsp";
    final private String RSA_KEY_URL = "http://www.clearcredit.store/GetRSA.jsp";
    final private String CURRENCY = "INR";

    private String ORDER_ID;
    String getCustName, getMobileNo, getEmailID, getPaymentAmt, getPaymentName, getVehicleType, getBookingDate;
    private String getBookingTime, getParkingName;
    int getUserID, getParkingAddrID;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);

        mainIntent = getIntent();
        ORDER_ID = getOrderID();

        getCustName = mainIntent.getExtras().getString("CUSTOMER_NAME");
        getMobileNo = mainIntent.getExtras().getString("MOBILE_NUMBER");
        getEmailID = mainIntent.getExtras().getString("EMAIL_ID");
        getPaymentAmt = mainIntent.getExtras().getString("PAYMENT_AMT");
        getPaymentName = mainIntent.getExtras().getString("PAYMENT_NAME");
        getUserID = mainIntent.getExtras().getInt("USER_ID");
        getParkingAddrID = mainIntent.getExtras().getInt("PARKING_ADD_ID");
        getVehicleType = mainIntent.getExtras().getString("VEHICLE_TYPE");
        getBookingDate = mainIntent.getExtras().getString("BOOKING_DATE");
        getBookingTime = mainIntent.getExtras().getString("BOOKING_TIME");
        getParkingName = mainIntent.getExtras().getString("PARKING_NAME");

        MyOpenHelper db = new MyOpenHelper(this);
        user = db.getUser();

        new RenderView().execute();
    }

    private String getOrderID() {
        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        return hashCal("SHA-256", rndm).substring(0, 20);
    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(CCAvenueGateway.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, ACCESS_CODE));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, ORDER_ID));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_NAME, "Prashant Mhetre"));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_ADDRESS, "Bibewadi"));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_CITY, "Pune"));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_STATE, "Maharashrtra"));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_ZIP, "411052"));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_COUNTRY, "India"));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_TEL,"8087423008"));
//			params.add(new BasicNameValuePair(AvenuesParams.BILLING_EMAIL, "Prashantmhetre@gmail.com"));

            String vResponse = sh.makeServiceCall(RSA_KEY_URL, ServiceHandler.POST, params);
            System.out.println(vResponse);
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                try {
                    StringBuffer vEncVal = new StringBuffer("");
//                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, getPaymentAmt));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, "1"));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, CURRENCY));
                    encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
//			if (dialog.isShowing())
//				dialog.dismiss();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    Intent intent = new Intent(getApplicationContext(), PaymentComplete.class);
                    intent.putExtra("TRANSEID", ORDER_ID);
                    intent.putExtra("PAIDAMT", getPaymentAmt);
                    intent.putExtra("PAYMENT_NAME", getPaymentName);
                    intent.putExtra("USER_ID", getUserID);
                    intent.putExtra("PARKING_ADD_ID", getParkingAddrID);
                    intent.putExtra("VEHICLE_TYPE", getVehicleType);
                    intent.putExtra("BOOKING_DATE", getBookingDate);
                    intent.putExtra("BOOKING_TIME", getBookingTime);
                    intent.putExtra("PARKING_NAME", getParkingName);
                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        intent.putExtra("DONE", false);
                        intent.putExtra("OLD", true);
                        intent.putExtra("STATUS", "Failure");
                        intent.putExtra("BOOK_STATUS", "Failure");
                    } else if (html.indexOf("Success") != -1) {
                        intent.putExtra("DONE", true);
                        intent.putExtra("OLD", true);
                        intent.putExtra("STATUS", "Success");
                        intent.putExtra("BOOK_STATUS", "Booked");
                    } else if (html.indexOf("Aborted") != -1) {
                        intent.putExtra("DONE", false);
                        intent.putExtra("OLD", true);
                        intent.putExtra("STATUS", "Cancle");
                        intent.putExtra("BOOK_STATUS", "Cancle");
                    } else {
                        intent.putExtra("DONE", false);
                        intent.putExtra("OLD", true);
                        intent.putExtra("STATUS", "Cancle");
                        intent.putExtra("BOOK_STATUS", "Cancle");
                    }
                    startActivity(intent);
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            //final WebView webview = new WebView(activity);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                        webview.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }

//				@Override
//				public void onPageStarted(WebView view, String url, Bitmap favicon) {
//					//make sure dialog is showing
//					if (!progressDialog.isShowing()) {
//						progressDialog.show();
//					}
//				}
            });

			/* An instance of this class will be registered as a JavaScript interface */
            StringBuffer params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, ACCESS_CODE));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, MERCHANT_ID));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, ORDER_ID));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, REDIRECT_URL));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, CANCEL_URL));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));
            if (user != null) {
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, user.getName()));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS, ""));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, user.getAddress()));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, "Maharashtra"));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, ""));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, "India"));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, user.getMobileNo()));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, user.getEmail()));
            }
            String vPostParams = params.substring(0, params.length() - 1);
            try {
                if (dialog.isShowing())
                    dialog.dismiss();
                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));

            } catch (Exception e) {
                showToast("Exception occured while opening webview.");
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();
    }
} 