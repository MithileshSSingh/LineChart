package com.example.linechart;

import com.example.linechart.custom.CustomLineChart;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private CustomLineChart chart;
	private float yValue[] = { 100, 150, 170, 250, 270, 250, 300 };
	private String xValue[] = { "May 21", "May 22", "May 23", "May 24",
			"May 25", "May 26", "May 27" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inti();
	}

	private void inti() {
		intiView();
		intiChartData();
	}

	private void intiView() {
		chart = (CustomLineChart) findViewById(R.id.chart);

	}

	private void intiChartData() {
		chart.setxValue(xValue);
		chart.setyValue(yValue);
		chart.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
