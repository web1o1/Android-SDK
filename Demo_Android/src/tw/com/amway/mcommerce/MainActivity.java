package tw.com.amway.mcommerce;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import co.viscovery.sdk.*;
import co.viscovery.sdk.KernelDefinition.*;

public class MainActivity extends ScanActivity implements ScanActivity.ScanCallback {

    private Animation animation = null;
    private View scanBar = null;

    void runAnimation() {
        if (animation == null) {
            FrameLayout fl = (FrameLayout)findViewById(android.R.id.content);
            animation = new TranslateAnimation(0, 0, 0, fl.getBottom() - fl.getTop()); 
            animation.setDuration(2000); // All animation time
            animation.setRepeatCount(-1); // Setting repeat count( -1 mean limitless repeat)
            animation.setRepeatMode(Animation.REVERSE); // If animation finished, it will be start from the end
            scanBar.setAnimation(animation);
        }

        if (animation.hasEnded()) {
            animation.reset();
            animation.startNow();
        }
    }

    void showAnimation() {
        if (!scanBar.isShown()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    runAnimation();
                    scanBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    void hideAnimation() {
        runOnUiThread(new Runnable() {
            public void run() {
                scanBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    void initKernel() {
        this.setUser("amway_mcom");
        //this.setUser("demo");
        this.setLogLocation(true); // log user location for analytics
        //this.setDecodeBarCode(true);
        this.setRecognizeMode(KernelMode.km_offline);
        this.setRecognizePrefer(KernelPrefer.kp_speed);

        this.callback = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.loadKernelData();
    }

    @Override
    public void onPause() {
        super.onPause();
        //this.unloadKernelData();
    }

    public void recognizeMatched(String[] names, Bitmap image) {
        String name = "";
        for (int i = 0; i < names.length; i++) {
            name = name + names[i] + "\n";
            Log.i("[Demo]", "Matched " + names[i]);
        }
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Matched");
        builder.setMessage(name);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recognizeContinue();
            }
        });
        builder.create().show();
        hideAnimation();
        //this.recognizeContinue();
    }

    // recognize similar images, return similar images names and query image
    // recognize process will be auto paused, need to call recognizeContinue() to restart the process
    public void recognizeSimilar(String[] names, Bitmap image) {
        String name = "";
        for (int i = 0; i < names.length; i++) {
            name = name + names[i] + "\n";
            Log.i("[Demo]", "Similar " + names[i]);
        }
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Similar");
        builder.setMessage(name);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recognizeContinue();
            }
        });
        builder.create().show();
        hideAnimation();
        //this.recognizeContinue();
    }

    // recognize failed, return with query image, recognize process will be auto continue
    public void recognizeNeedMore(Bitmap image) {
        Log.i("[Demo]", "recognizeNeedMore");
    }

    // recognize failed, return with query image, recognize process will be auto continue
    public void recognizeFailed(Bitmap image) {
        Log.i("[Demo]", "Failed");
    }

    // one recognize process begin, use this function to start scan animation
    public void recognizeBegin() {
        showAnimation();
    }

    // one recognize process ended, use this function to stop scan animation
    public void recognizeEnd() {
        
    }

    public void barcodeDecoded(String[] names, Bitmap image) {
        String name = "";
        for (int i = 0; i < names.length; i++) {
            name = name + names[i] + "\n";
            Log.i("[Demo]", "Barcode " + names[i]);
        }
        Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Barcode");
        builder.setMessage(name);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recognizeContinue();
            }
        });
        builder.create().show();
    }

    public void notifyError(KernelError error) {
    
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout fl = (FrameLayout)findViewById(android.R.id.content);
        //View.inflate(this, net.funwish.demo.R.layout.activity_main, fl);
        scanBar = new View(this);
        scanBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
        scanBar.setVisibility(View.INVISIBLE);
        scanBar.setBackgroundColor(Color.CYAN);
        fl.addView(scanBar);

        initKernel();
        Log.i("[Demo]", "MainActivity onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Log.i("[Demo]", "MainActivity onCreateOptionsMenu");
        return true;
    }
}
