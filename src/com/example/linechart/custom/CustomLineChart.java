package com.example.linechart.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomLineChart extends View {

	private Context mContext;

	private Paint paintChart;
	private Paint paintChartLine;
	private Paint paintChartSeparator;
	private Paint paintChartBaseLine;
	private Paint paintChartPoint;
	private Paint paintChartPointCircle;
	private Paint paintChartText;

	private Path pathChart;
	private Path pathChartLine;

	private int colorChart;
	private int colorChartLine;
	private int colorSeparator;
	private int colorBaseLine;
	private int colorChartPoint;
	private int colorChartPointCircle;
	private int colorChartText;

	private int radiusChartPoint = 5;
	private int radiusChartPointCircle = 7;

	private int graphContainerWidth;
	private int graphContainerHeight;

	private float BASE_CHART = 0;
	private float TOP_CHART = 0;
	private float TOTAL_CHART = 0;

	private float FOOTER_HEIGHT = 0; // 10% of view's height
	private float HEADER_HEIGHT = 0; // 10% of view's height

	private float x[];
	private float y[];
	private float maxY = 0; // as 90%
	private float totalY = 0; // as 100%
	private float leftMargin = 0;

	// Get From User
	private float yValue[] ;
	private String xValue[] ;

	private float strokeBaseLine = 1;
	private float strokeChartLine = 2;
	private float strokeSeparatorLine = 1;
	private float strokePointBorder = 2;

	private ValueAnimator mAnimator;

	private Canvas mCanvas;

	private int textSize = 10;

	public CustomLineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		initialize();
		tempValues();

	}

	private void tempValues() {
		Resources mResources = mContext.getResources();

		colorChart = Color.GRAY;
		colorSeparator = Color.BLACK;
		colorChartLine = Color.BLACK;
		colorBaseLine = Color.GRAY;
		colorChartPoint = Color.BLACK;
		colorChartPointCircle = Color.WHITE;

		colorChartText = Color.GREEN;
	}

	private void initialize() {
		paintChart = new Paint();
		paintChartLine = new Paint();
		paintChartSeparator = new Paint();
		paintChartBaseLine = new Paint();
		paintChartPoint = new Paint();
		paintChartPointCircle = new Paint();
		paintChartText = new Paint();
		pathChart = new Path();
		pathChartLine = new Path();

	}

	private void initChartXY() {
		// Global height initialization
		HEADER_HEIGHT = (float) ((0.10) * graphContainerHeight);
		FOOTER_HEIGHT = HEADER_HEIGHT;

		BASE_CHART = (float) ((0.90) * graphContainerHeight);
		TOP_CHART = (float) ((0.10) * graphContainerHeight);
		TOTAL_CHART = (float) ((0.90) * graphContainerHeight);

		initMaxY();
		initY_CO_Ordinate();
		initX_CO_Ordinate();

	}

	private void initMaxY() {
		for (int i = 0; i < yValue.length; i++) {
			if (yValue[i] > maxY)
				maxY = yValue[i];
		}

		totalY = maxY / (float) (0.90);
	}

	private void initX_CO_Ordinate() {

		x = new float[yValue.length + 4];

		int xParts = yValue.length + 1;
		int partWidth = graphContainerWidth / xParts;
		float xbase = leftMargin;

		x[0] = leftMargin;
		x[1] = leftMargin;
		for (int i = 1; i <= xParts; i++) {
			int tillNextPart = partWidth * i;
			x[i + 1] = tillNextPart + xbase;
		}
		x[x.length - 1] = x[x.length - 2];
	}

	private void initY_CO_Ordinate() {
		y = new float[yValue.length + 4];

		// Setting Y value
		y[0] = BASE_CHART;
		y[1] = BASE_CHART - getNewY(yValue[0]);

		for (int i = 0; i < yValue.length; i++) {
			y[i + 2] = BASE_CHART - getNewY(yValue[i]);
		}

		y[y.length - 2] = BASE_CHART - getNewY(yValue[yValue.length - 1]);
		y[y.length - 1] = BASE_CHART;
	}

	private float getNewY(float valueY) {
		return (((valueY / totalY) * 100) / 100) * TOTAL_CHART;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.v("TAG", "LAST");

		drawChart(canvas);
		drawChartSeparator(canvas);
		drawChartPoints(canvas);
		drawBaseLine(canvas);
		// drawTopLine(canvas);
	}

	private void drawChartPoints(Canvas canvas) {

		// Drawing Points and plotting text
		paintChartPoint.setColor(colorChartPoint);
		paintChartPoint.setStyle(Style.FILL);

		paintChartPointCircle.setColor(colorChartPointCircle);
		paintChartPointCircle.setStyle(Style.STROKE);
		paintChartPointCircle.setStrokeWidth(strokePointBorder);

		paintChartText.setColor(colorChartText);
		paintChartText.setTextSize(textSize);
		paintChartText.setStyle(Style.FILL);

		for (int i = 2; i < y.length - 2; i++) {
			// plotting point
			canvas.drawCircle(x[i], y[i], radiusChartPoint, paintChartPoint);
			canvas.drawCircle(x[i], y[i], radiusChartPointCircle,
					paintChartPointCircle);

			// plotting x Value
			float strlength = paintChartText.measureText(xValue[i - 2]) / 2;
			canvas.drawText(xValue[i - 2], x[i] - strlength, BASE_CHART
					+ (TOP_CHART / 2), paintChartText);

			// Plotting y Value
			strlength = paintChartText.measureText(String
					.valueOf(yValue[i - 2])) / 2;

			canvas.drawText(String.valueOf((int) yValue[i - 2]), x[i]
					- strlength, y[i] - (TOP_CHART / 3), paintChartText);
		}
	}

	private void drawBaseLine(Canvas canvas) {
		// Drawing Base Line
		paintChartBaseLine.setColor(colorBaseLine);
		paintChartBaseLine.setStyle(Style.STROKE);
		paintChartBaseLine.setStrokeWidth(strokeBaseLine);
		canvas.drawLine(x[0], BASE_CHART, x[x.length - 1], y[y.length - 1],
				paintChartBaseLine);
	}

	private void drawTopLine(Canvas canvas) {
		// Drawing Base Line
		paintChartBaseLine.setColor(colorBaseLine);
		canvas.drawLine(x[0], TOP_CHART, x[x.length - 1], TOP_CHART,
				paintChartBaseLine);
	}

	private void drawChartSeparator(Canvas canvas) {
		// Drawing Separator
		paintChartSeparator.setColor(colorSeparator);
		paintChartBaseLine.setStyle(Style.STROKE);
		paintChartBaseLine.setStrokeWidth(strokeSeparatorLine);
		for (int i = 0; i < x.length; i++) {
			canvas.drawLine(x[i], BASE_CHART, x[i], y[i], paintChartSeparator);
		}
	}

	private void drawChart(Canvas canvas) {
		// Filling Chart ...
		paintChart.setColor(colorChart); // set the color
		paintChart.setStyle(Style.FILL); // set to STOKE
		pathChart.reset();
		pathChart.moveTo(x[0], y[0]);
		for (int i = 1; i < x.length; i++) {
			pathChart.lineTo(x[i], y[i]);
		}

		// Filling Gradient color
		paintChart.setShader(new LinearGradient(0, 0, 0, BASE_CHART,
				colorChart, Color.WHITE, Shader.TileMode.MIRROR));

		canvas.drawPath(pathChart, paintChart);

		// Drawing Chart Line...
		paintChartLine.setColor(colorChartLine); // set the color
		paintChartLine.setStyle(Style.STROKE); // set to STOKE
		paintChartLine.setStrokeWidth(strokeChartLine);
		pathChartLine.reset();
		pathChartLine.moveTo(x[1], y[1]);
		for (int i = 2; i < x.length - 1; i++) {
			pathChartLine.lineTo(x[i], y[i]);
		}
		canvas.drawPath(pathChartLine, paintChartLine);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.v("TAG", "LAST");
		graphContainerWidth = MeasureSpec.getSize(widthMeasureSpec);
		graphContainerHeight = MeasureSpec.getSize(heightMeasureSpec);

		initChartXY();
	}

	public int getColorChart() {
		return colorChart;
	}

	public void setColorChart(int colorChart) {
		this.colorChart = colorChart;
	}

	public int getColorChartLine() {
		return colorChartLine;
	}

	public void setColorChartLine(int colorChartLine) {
		this.colorChartLine = colorChartLine;
	}

	public int getColorSeparator() {
		return colorSeparator;
	}

	public void setColorSeparator(int colorSeparator) {
		this.colorSeparator = colorSeparator;
	}

	public int getColorBaseLine() {
		return colorBaseLine;
	}

	public void setColorBaseLine(int colorBaseLine) {
		this.colorBaseLine = colorBaseLine;
	}

	public int getColorChartPoint() {
		return colorChartPoint;
	}

	public void setColorChartPoint(int colorChartPoint) {
		this.colorChartPoint = colorChartPoint;
	}

	public int getColorChartPointCircle() {
		return colorChartPointCircle;
	}

	public void setColorChartPointCircle(int colorChartPointCircle) {
		this.colorChartPointCircle = colorChartPointCircle;
	}

	public int getColorChartText() {
		return colorChartText;
	}

	public void setColorChartText(int colorChartText) {
		this.colorChartText = colorChartText;
	}

	public float[] getyValue() {
		return yValue;
	}

	public void setyValue(float[] yValue) {
		this.yValue = yValue;
	}

	public String[] getxValue() {
		return xValue;
	}

	public void setxValue(String[] xValue) {
		this.xValue = xValue;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
}
