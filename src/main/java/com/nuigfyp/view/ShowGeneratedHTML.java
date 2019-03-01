package com.nuigfyp.view;

import java.awt.Desktop;
import java.io.*;
import com.nuigfyp.model.Bug;

public class ShowGeneratedHTML {

	private static final String[] COMPANY = new String[] { "sapicon.jpg", "nuig.jpg", "sonyericsson.jpg",
			"medtronic.png", "hp.png" };
	private File f;

	public ShowGeneratedHTML(Bug bug) {

		BufferedWriter bw = null;
		File screenshotFile = new File(bug.getScreenshot());
		File pdfFile = new File(bug.getDocument());

		try {

			f = new File("source.htm");
			bw = new BufferedWriter(new FileWriter(f));

			bw.write("<html>");
			bw.newLine();
			bw.write("<head>");
			bw.newLine();
			bw.write("<link rel = \"stylesheet\" type = \"text/css\" href = \"css/style.css\">");
			bw.newLine();
			bw.write("<body>");
			bw.newLine();
			bw.newLine();

			bw.write("<div id=\"banner\">");
			bw.newLine();
			bw.write("<div>");
			bw.newLine();
			bw.write("<img src = \"resources/bug.png\" alt = \"Bug Icon\" width=\"70\" height=\"70\" align=\"left\">");
			bw.newLine();
			bw.write("<img src = \"resources/bug.png\" alt = \"Bug Icon\" width=\"70\" height=\"70\" align=\"right\">");
			bw.newLine();
			bw.write("<h1>BugReporter</h1>");
			bw.newLine();
			bw.write("<p>14102730 Final Year Project: 2018-2019</p>");
			bw.newLine();
			bw.write("</div>");
			bw.newLine();
			bw.write("</div>");
			bw.newLine();
			bw.write("<br><br>");
			bw.newLine();
			bw.newLine();

			bw.write("<div>");
			int companyImageIndex = bug.getProject();
			bw.newLine();
			bw.write("<img src = \"resources/" + COMPANY[--companyImageIndex]
					+ "\" alt = \"Company Logo\" width=\"140\" height=\"140\" align=\"left\">");
			bw.newLine();
			bw.write("</div>");
			bw.newLine();
			bw.newLine();

			bw.write("<table align=\"center\">");
			bw.newLine();
			bw.write("<tr>");
			bw.newLine();
			bw.write("<th>Project ID</th>");
			bw.newLine();
			bw.write("<th>Severity</th>");
			bw.newLine();
			bw.write("<th>Recorders Name</th>");
			bw.newLine();
			bw.write("<th>Tester Name</th>");
			bw.newLine();
			bw.write("</tr>");
			bw.newLine();
			bw.write("<br>");
			bw.newLine();
			bw.write("<tr>");
			bw.newLine();
			bw.write("<td>" + bug.getProject() + "</td>");
			bw.newLine();
			bw.write("<td>" + bug.getSeverity() + "</td>");
			bw.newLine();
			bw.write("<td>" + bug.getReporterName() + "</td>");
			bw.newLine();
			bw.write("<td>" + bug.getTesterName() + "</td>");
			bw.newLine();
			bw.write("</tr>");
			bw.newLine();
			bw.write("</table>");
			bw.newLine();
			bw.newLine();

			bw.write("<br><br>");
			bw.newLine();
			bw.write("<h1>Bug Report Description</h1>");
			bw.newLine();
			bw.newLine();
			bw.write("<textarea cols=95 rows=10>");
			bw.write(bug.getDescription());
			bw.write("</textarea>");
			bw.newLine();
			bw.write("<br><br>");
			bw.newLine();
			bw.newLine();

			if (!screenshotFile.getName().equals("No")) {

				bw.write("<div>");
				bw.newLine();
				bw.write("<h1>Bug Screenshot</h1>");
				bw.newLine();
				bw.write("<img src = \"Downloaded_Files/" + screenshotFile.getName()
						+ "\" alt = \"Screenshot Image\" class=\"center\" border=\"5\">");
				bw.newLine();
				bw.write("</div>");
				bw.newLine();
				bw.write("<br><br>");
				bw.newLine();
				bw.newLine();
			}

			if (!pdfFile.getName().equals("No")) {

				bw.write("<div>");
				bw.newLine();
				bw.write("<h1>Bug Document</h1>");
				bw.newLine();
				bw.write("<embed src = \"Downloaded_Files/" + pdfFile.getName()
						+ "\" type=\"application/pdf\" width=\"100%\" height=\"510%\"/>");
				bw.newLine();
				bw.write("</div>");
				bw.newLine();
				bw.newLine();
			}

			bw.write("</body>");
			bw.newLine();
			bw.write("</head>");
			bw.newLine();
			bw.write("</html>");

			bw.close();
			Desktop.getDesktop().browse(f.toURI());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
