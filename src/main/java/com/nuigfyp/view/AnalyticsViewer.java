package com.nuigfyp.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.nuigfyp.model.Bug;

// **********************************************************************
// Go to JFreeChart class and paste code in to see in Windows Builder Pro
// **********************************************************************

public class AnalyticsViewer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public void viewAnalytics(final ArrayList<Bug> list) {

		setTitle("Analytics Viewer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 544, 527);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(175, 238, 238));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*final JPanel chartDisplay = new JPanel();
		chartDisplay.setBounds(10, 11, 470, 250);
		contentPane.add(chartDisplay);*/

		final JPanel pieChartPanel = new JPanel();
		pieChartPanel.setBounds(11, 11, 330, 300);
		contentPane.add(pieChartPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(167, 426, 414, 59);
		contentPane.add(buttonPanel);

		JButton btnView = new JButton("View");
		
		PieDataset dataset2 = createLocalisationAverageDataset(list);
		JFreeChart pieChart = createChart(dataset2, "Average Time for Problem Resolution");
		ChartPanel chartPanel2 = new ChartPanel(pieChart);
		chartPanel2.setPreferredSize(new Dimension(330, 290));
		pieChartPanel.add(chartPanel2);
		pieChartPanel.validate();
		
		/*btnView.addActionListener(new ActionListener() {

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
				PieDataset dataset2 = createLocalisationAverageDataset(list);
				JFreeChart pieChart = createChart(dataset2, "Average Time for Problem Resolution");
				ChartPanel chartPanel2 = new ChartPanel(pieChart);
				chartPanel2.setPreferredSize(new Dimension(330, 290));
				pieChartPanel.add(chartPanel2);
				pieChartPanel.validate();

			}
		});*/

		buttonPanel.add(btnView);
		this.setVisible(true);

	}

	private JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, // chart title
				dataset, // data
				true, 	 // include legend
				true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);		
		return chart;

	}
	
	private PieDataset createLocalisationAverageDataset(ArrayList<Bug> list) {

		double[] instancesTotal = new double[6];
		int[] numberOfInstances= new int[6];

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		for(Bug b:list) {			
			if(b.getActive() == 0) {
				
				String startDate = b.getStartDate();
				String endDate = b.getEndDate();
				LocalDateTime fromDateTime= LocalDateTime.parse(startDate, formatter);
				LocalDateTime toDateTime = LocalDateTime.parse(endDate, formatter);				
				long hours = fromDateTime.until(toDateTime, ChronoUnit.HOURS);
				int classificationIndexCode = b.getBugClassification();
				
				switch(classificationIndexCode) {
		         case 1 :	            
		        	numberOfInstances[0]++;
		        	instancesTotal[0] = instancesTotal[0] + hours;
		            break;
		         case 2 :
		        	numberOfInstances[1]++;
		        	instancesTotal[1] = instancesTotal[1] + hours;
		        	break;
		         case 3 :
		            numberOfInstances[2]++;
		        	instancesTotal[2] = instancesTotal[2] + hours;
		            break;
		         case 4 :
		            numberOfInstances[3]++;
		        	instancesTotal[3] = instancesTotal[3] + hours;
		            break;
		         case 5 :
		            numberOfInstances[4]++;
		        	instancesTotal[4] = instancesTotal[4] + hours;
		            break;
		         default :
		        	numberOfInstances[5]++;
			        instancesTotal[5] = instancesTotal[5] + hours;
		            System.out.println("Invalid Value." + hours);
		      }				
			}		
		}

		DefaultPieDataset result = new DefaultPieDataset();

		if(numberOfInstances[0] > 0) {
			result.setValue("Text Error", instancesTotal[0] / numberOfInstances[0]);
		} if (numberOfInstances[1] > 0) {
			result.setValue("Graphics", instancesTotal[1] / numberOfInstances[1]);
		} if (numberOfInstances[2] > 0) {
			result.setValue("Localisation", instancesTotal[2] / numberOfInstances[2]);
		} if (numberOfInstances[3] > 0) {
			result.setValue("On Click Error", instancesTotal[3] / numberOfInstances[3]);
		} if (numberOfInstances[4] > 0) {
			result.setValue("Code Error", instancesTotal[4] / numberOfInstances[4]);
		} if (numberOfInstances[5] > 0) {
			result.setValue("Unknown Error", instancesTotal[5] / numberOfInstances[5]);
		};
		
		return result;
	}
}
