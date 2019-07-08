import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import tech.tablesaw.plotly.*;
import tech.tablesaw.plotly.components.*;
import tech.tablesaw.plotly.components.Font;
import tech.tablesaw.plotly.traces.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class PDFGen {

    // For storing specific fonts
    static HashMap<String, PDType1Font> fonts = new HashMap<>();
    static HashMap<String, Font.Family> fontf = new HashMap<>();

    /* initialize method creates first page and populates font-storing arrays
    Accepts the path + file name for the PDF you're generating
    Needs to be called first before anything
    Returns the PDDocument instance
    */
    public static PDDocument initialize(String path) throws IOException {
        fonts.put("Times regular", PDType1Font.TIMES_ROMAN);
        fontf.put("Arial", Font.Family.ARIAL);
        PDDocument doc = new PDDocument();
        return genPage(path, doc);
    }

    // creates a page on the PDF document and returns the PDDocument instance
    public static PDDocument genPage(String path, PDDocument document) throws IOException {
        document.addPage(new PDPage());
        document.save(path);
        return document;
    }

    // retrieves a page from the PDF document based on page number and returns the PDPage instance
    public static PDPage retrievePage(PDDocument doc, int pageNum) {
        PDPage page = doc.getPage(pageNum);
        return page;
    }

    // inserts a piece of text on a PDDocument page (based on page number)
    // specific configuration params to determine stylization and placement of text
    public static void insertText(PDDocument doc, int pageNum, String path, String font, int fontsize, float lead,
                                  int offx, int offy, ArrayList<String> texts) throws IOException {
        PDPage page = retrievePage(doc, pageNum);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);
        contentStream.beginText();

        contentStream.setFont((fonts.get(font)), fontsize);
        contentStream.setLeading(lead);
        contentStream.newLineAtOffset(offx, offy);
        for (int i = 0; i < texts.size(); i++) {
            contentStream.showText(texts.get(i));
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.close();
        doc.save(path);
    }

    // inserts an image on a PDDocument page (based on page number)
    // need to specify an image path as well as a save path
    public static void insertImage(PDDocument doc, int pageNum, String imgpath, String savepath,
                                   float x, float y, float w, float h) throws IOException {
        PDPage page = retrievePage(doc, pageNum);
        PDImageXObject pdImage = PDImageXObject.createFromFile(imgpath, doc);
        PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);
        contents.drawImage(pdImage, x, y, w, h);
        contents.close();
        doc.save(savepath);
    }

    // unfinished method, don't use for now
    public static void genBarChart(PDDocument doc, int pageNum, String docsavepath,
                                   float x, float y,
                                   int w, int h,
                                   Object[] xData, double[] yData,
                                   int fontsize, HashMap<String, String> configs) throws IOException {
        Font f = Font.builder().family(fontf.get(configs.get("fontFamily")))
                .size(fontsize)
                .color(configs.get("fontColor"))
                .build();

        Axis xAxis = Axis.builder()
                .title(configs.get("xtitle"))
                .build();

        Layout layout = Layout.builder()
                .title(configs.get("title"))
                .height(h)
                .width(w)
                .plotBgColor(configs.get("plotBgColor"))
                .paperBgColor(configs.get("paperBgColor"))
                .titleFont(f)
                .xAxis(xAxis)
                .build();

        BarTrace trace = BarTrace.builder(xData, yData).build();
        Figure figure = new Figure(layout, trace);
        File outputFile = Paths.get("testoutput/output.html").toFile();
        Plot.show(figure, "target", outputFile);

        // open HTML page
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        URL urlToPage = new File("testoutput/output.html").toURI().toURL();
        editorPane.setPage(urlToPage);
        editorPane.setSize(2000, 2000);

        // render the page
        BufferedImage renderedImage = new BufferedImage(2000, 2080, BufferedImage.TYPE_INT_RGB);
        editorPane.print(renderedImage.getGraphics());

        // write result to file
        ImageIO.write(renderedImage, "PNG", new File("output.png"));

/*        String uri = new File("testoutput/output.html").toURI().toString();
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        imageGenerator.loadUrl(uri);
        imageGenerator.getBufferedImage();
        imageGenerator.saveAsImage("output.png");
        imageGenerator.saveAsHtmlWithMap("hello-world.html", "output.png");*/

/*        final Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(document.html());
        renderer.layout();

        try (OutputStream os = Files.newOutputStream(Paths.get(outputPdf))) {
            renderer.createPDF(os);
        }*/
/*
        insertImage(doc, pageNum, "testoutput/output.png", docsavepath,
                x, y, w, h);*/
    }

    // need to call this at the end to close the document
    public static void docClose (PDDocument doc) throws IOException {
        doc.close();
    }



}
