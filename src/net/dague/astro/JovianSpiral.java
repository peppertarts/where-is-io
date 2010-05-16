package net.dague.astro;

import net.dague.astro.data.SimData;
import net.dague.astro.util.JovianCalculator;
import net.dague.astro.util.JovianPoints;
import net.dague.astro.util.SolarCalc;
import net.dague.astro.util.SolarSim;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class JovianSpiral extends Activity implements OnClickListener {
	
	AsyncTask<SolarCalc,View,Void> jc;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JovianGraphView jgv = new JovianGraphView(this);

        SolarSim sim = new SolarSim(this);
        
        new Thread(new JovianCalculator(sim, jgv, JovianGraphView.startTime(), 120)).start();
        
    	setContentView(jgv);
        
        jgv.requestFocus();
    
    }
//    protected void onPause() {
//    	super.onPause();
//    }
	@Override
	public void onClick(DialogInterface dialog, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			startActivity(new Intent(this, About.class));
		    return true;
		}
		return false;
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {
	   super.onCreateOptionsMenu(menu);
	   MenuInflater inflater = getMenuInflater();
	   inflater.inflate(R.menu.menu, menu);
	   return true;
	}

    
//    protected void onSaveInstanceState(Bundle  outState) {
//    	if (this.jc != null && this.jc.getStatus() != AsyncTask.Status.FINISHED) {
//     		outState.putBoolean("inProgress", true);
//    	}
//    }
}