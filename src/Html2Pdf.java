import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.FSEntityResolver;

/**
 * @author Manu Raj Hada [manu.raj.hada@gmail.com]
 *
 */
public class Html2Pdf {

	public static void main(String[] args) throws Exception {
		/**
		 * calling html2pdf function, to pass url as parameter.
		 */
		html2pdf("https://m.facebook.com/");
	}

	/**
	 * Function to generate pdf from HTML accessed from given URL parameter.<br/>
	 * It will fetch the HTML from given URL generate the PDF and will save at given file path. You may download the pdf from output stream for web project<br/>
	 * Note: To use this function n generate PDF successfully the HTML must pass w3 validation test, i.e. all html tags must have closed, "&" should be written as "&amp;" etc.<br/>
	 * It may cut the generated pdf horizontally, for that html page must have to shrink on browser to generate pdf successfully.<br/>
	 * I have tried it with best view of html body width:720px
	 * @param urlParam
	 */
	public static void html2pdf(String urlParam) {
		System.out.println("Processing..");
		// will generate pdf with given fileName
		String fileName = "Sample.pdf";
		// getting project's root path
		String path = System.getProperty("user.dir");
		// getting Operating System name
		String OS = System.getProperty("os.name").toLowerCase();
		// setting file path to project root folder based on OS
		String outputFile = OS.indexOf("win") >= 0 ? path + "\\" + fileName
				: path + "/" + fileName;
		File file = new File(outputFile);
		// deleting file if it already exists 
		if (file.exists()) {
			file.delete();
		}
		try {
			OutputStream os = new FileOutputStream(outputFile);
			// initializing iText rendrer
			ITextRenderer renderer = new ITextRenderer();
			// building document factory to render pdf
			DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
			// turning off factory settings to improve generation speed
			fac.setNamespaceAware(false);
			fac.setValidating(false);
			fac.setFeature("http://xml.org/sax/features/namespaces", false);
			fac.setFeature("http://xml.org/sax/features/validation", false);
			fac.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
					false);
			fac.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
			
			DocumentBuilder builder = fac.newDocumentBuilder();
			builder.setEntityResolver(FSEntityResolver.instance());
			String docURI = urlParam;
			// parsing URL HTML document
			org.w3c.dom.Document document = builder.parse(docURI);
			renderer.setDocument(document, docURI);
			// creating pdf layout
			renderer.layout();
			try {
				// generating pdf
				renderer.createPDF(os);
			} catch (com.lowagie.text.DocumentException e) {
				e.printStackTrace();
			}
			os.close();
			System.out.println("done!");
			System.out.println("Find the PDF at location: " + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}