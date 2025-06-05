package demo.project.api.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import io.restassured.response.Response;

public class EvidenceUtils {

	public static void takeScreenshot(Response response, String scenarioName) throws IOException {
	    // Formatando a data e hora
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmm");
	    String format = formatter.format(LocalDateTime.now());

	    // Sanitizando o nome do cenário
	    String sanitizedName = sanitizeFileName(scenarioName != null ? scenarioName : "unknown_scenario");

	    // Caminho base
	    String baseFolderPath = "Evidences/API/" + sanitizedName;
	    File testFolder = new File(baseFolderPath);
	    if (!testFolder.exists()) {
	        testFolder.mkdirs();
	    }

	    // Caminho do arquivo PDF
	    String filePath = baseFolderPath + "/evidences_" + format + ".pdf";

	    // Criando o PDF
	    PdfWriter writer = new PdfWriter(filePath);
	    PdfDocument pdf = new PdfDocument(writer);
	    Document document = new Document(pdf);

	    document.add(new Paragraph("RESPONSE TAKESCREENSHOT")
	        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
	        .setFontSize(16));

	    document.add(new Paragraph("\n\n"));
	    document.add(new Paragraph("STATUS CODE: " + response.getStatusCode()));
	    document.add(new Paragraph("RESPONSE BODY: " + response.getBody().asString()));
	    document.add(new Paragraph("RESPONSE HEADERS: " + response.getHeaders().toString()));
	    document.add(new Paragraph("TIMESTAMP: " + LocalDateTime.now()));
	    document.close();

	    System.out.println("Evidence generated in PDF format in the folder '" + baseFolderPath + "'!");
	}

	// Sanitiza o nome do cenário para uso como nome de pasta
	private static String sanitizeFileName(String fileName) {
	    return fileName.replaceAll("[<>:\"/\\\\|?*]", "_").replaceAll("\\s+", "_");
	}

}
