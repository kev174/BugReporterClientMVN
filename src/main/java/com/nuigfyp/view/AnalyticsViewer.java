package com.nuigfyp.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

// **********************************************************************
// Go to JFreeChart class and paste code in to see in Windows Builder Pro
// **********************************************************************

public class AnalyticsViewer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public void viewAnalytics() {

		setTitle("Analytics Viewer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 744, 527);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(175, 238, 238));
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JPanel chartDisplay = new JPanel();
		chartDisplay.setBounds(10, 11, 470, 250);
		contentPane.add(chartDisplay);

		final JPanel pieChartPanel = new JPanel();
		pieChartPanel.setBounds(492, 11, 230, 200);
		contentPane.add(pieChartPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(167, 426, 414, 59);
		contentPane.add(buttonPanel);

		JButton btnView = new JButton("View");
		btnView.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				dataset.setValue(18, "", "Text Error");
				dataset.setValue(25, "", "Graphics");
				dataset.setValue(30, "", "Localisation");
				dataset.setValue(35, "", "On Click Error");
				dataset.setValue(25, "", "Code Error");

				JFreeChart chart = ChartFactory.createBarChart("Statistics", "Classification", "Days for Completion",
						dataset, PlotOrientation.VERTICAL, false, true, false);
				CategoryPlot catPlot = chart.getCategoryPlot();

				// IF comparing three charts then this will assign different colors
				catPlot.getRenderer().setSeriesPaint(0, new Color(0, 230, 255));
				catPlot.getRenderer().setSeriesPaint(1, new Color(0, 0, 255));
				catPlot.getRenderer().setSeriesPaint(2, new Color(128, 0, 0));

				ChartPanel chartPanel = new ChartPanel(chart);
				chartPanel.setPreferredSize(new java.awt.Dimension(470, 250));
				chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				chartPanel.setBackground(Color.LIGHT_GRAY);

				chartDisplay.removeAll();
				chartDisplay.add(chartPanel);
				chartDisplay.validate();

				// Pie Chart
				PieDataset dataset2 = createDataset();
				JFreeChart pieChart = createChart(dataset2, "kevin");
				ChartPanel chartPanel2 = new ChartPanel(pieChart);
				chartPanel2.setPreferredSize(new Dimension(230, 190));
				pieChartPanel.add(chartPanel2);
				pieChartPanel.validate();

			}
		});

		buttonPanel.add(btnView);
		this.setVisible(true);

	}

	private PieDataset createDataset() {

		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Text Error", 7);
		result.setValue("Graphics", 8);
		result.setValue("Localisation", 2);
		result.setValue("On Click Error", 0);
		result.setValue("Code Error", 4);
		return result;

	}

	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, // chart title
				dataset, // data
				true, // include legend
				true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}
}
