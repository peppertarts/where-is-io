package net.dague.astro;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import net.dague.astro.sim.JupiterSim;
import net.dague.astro.sim.RiseCalculator;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.TimeUtil;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.DatePicker;

public class RiseSetTimes extends Activity {

	private SimpleDateFormat df;
	private RiseCalculator rs;
	private Calendar currentDate;
	private int mYear;
    private int mMonth;
    private int mDay;
	private static final int MENU_DATE = 0;
	private static final int MENU_RESET = 1;
	private static final int DATE_DIALOG_ID = 0;
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
		new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				
				Calendar date = Calendar.getInstance();
				date.set(year, monthOfYear, dayOfMonth);
				
				double jdDate = TimeUtil.JDfloor(TimeUtil.mils2JD(date.getTimeInMillis()));
				updateRiseSetTimes(jdDate);
			}
		};
	
	private void fillRiseTime(int id, int body, double now) {
		double rise = rs.riseTime(body, now);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(TimeUtil.JD2mils(rise));
        
        TextView view = (TextView) findViewById(id);
        view.setText("" + df.format(cal.getTime()));
	}
	
	private void fillSetTime(int id, int body, double now) {
		double set = rs.setTime(body, now);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(TimeUtil.JD2mils(set));

        TextView view = (TextView) findViewById(id);
        view.setText("" + df.format(cal.getTime()));
	}
	
	private double[] getGPS() {  

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
		List<String> providers = lm.getProviders(true);
		
		/* Loop over the array backwards, and if you get an accurate location, then break out the loop*/  
		Location l = null;  
		Log.i("GPS", "Numer of providers: " + providers.size());
  
		for (int i=providers.size()-1; i>=0; i--) {  
			Log.i("GPS", "Location provider: " + providers.get(i));
			l = lm.getLastKnownLocation(providers.get(i)); 
			if (l != null) break;
		}  
    
		double[] gps = new double[2];  
		Log.i("GPS", "L: " + l);
		if (l != null) {  
			gps[0] = l.getLatitude();  
			gps[1] = l.getLongitude(); 
		}  
		Log.i("GPS", "Lat: " + gps[0] + ", Lon: " + gps[1]);
		return gps;  
	}

	private void updateRiseSetTimes(double date) {
		fillRiseTime(R.id.sun_rise, SolarSim.SUN, date);
        fillSetTime(R.id.sun_set, SolarSim.SUN, date);
        
        fillRiseTime(R.id.mercury_rise, SolarSim.MERCURY, date);
        fillSetTime(R.id.mercury_set, SolarSim.MERCURY, date);
        
        fillRiseTime(R.id.venus_rise, SolarSim.VENUS, date);
        fillSetTime(R.id.venus_set, SolarSim.VENUS, date);
        
        fillRiseTime(R.id.mars_rise, SolarSim.MARS, date);
        fillSetTime(R.id.mars_set, SolarSim.MARS, date);

        fillRiseTime(R.id.jupiter_rise, SolarSim.JUPITER, date);
        fillSetTime(R.id.jupiter_set, SolarSim.JUPITER, date);
        
        fillRiseTime(R.id.saturn_rise, SolarSim.SATURN, date);
        fillSetTime(R.id.saturn_set, SolarSim.SATURN, date);
        
        fillRiseTime(R.id.uranus_rise, SolarSim.URANUS, date);
        fillSetTime(R.id.uranus_set, SolarSim.URANUS, date);

        fillRiseTime(R.id.neptune_rise, SolarSim.NEPTUNE, date);
        fillSetTime(R.id.neptune_set, SolarSim.NEPTUNE, date);
	}
			
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riseset);
        
        df = new SimpleDateFormat("H:mm");
        
        double gps[] = getGPS();
        rs = new RiseCalculator(gps[0], gps[1]);
        
        double now = TimeUtil.JDfloor(TimeUtil.mils2JD(System.currentTimeMillis()));
		updateRiseSetTimes(now);
		
		currentDate = Calendar.getInstance();
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_DATE, 0, "Set Date");
		menu.add(0, MENU_RESET, 0, "Reset");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DATE:
			showDialog(DATE_DIALOG_ID);
			return true;
		case MENU_RESET:
			mYear = currentDate.get(Calendar.YEAR);
			mMonth = currentDate.get(Calendar.MONTH);
			mDay = currentDate.get(Calendar.DAY_OF_MONTH);
		
			double now = TimeUtil.JDfloor(TimeUtil.mils2JD(System.currentTimeMillis()));
			updateRiseSetTimes(now);
			return true;
		}
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
						mDateSetListener,
						mYear, mMonth, mDay);
		}
		return null;
	}
}
