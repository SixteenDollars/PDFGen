import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main (String args[]) throws IOException {

        // initializing
        // Specify path and filename for the PDF you're generating, i.e. "/Users/rouge/Downloads/newpdf.pdf"
        PDDocument doc = PDFGen.initialize("specify path here");

        // inserting image
        // Need to specify page number, path to the image, save path, and styling/placement configuration parameters
        PDFGen.insertImage(doc,0, "specify path here",
               "specify path here", 0f,0f, 612f, 792f);

        // inserting text
        // Need to create an ArrayList of strings first to store desired text in
        ArrayList<String> texts = new ArrayList<>();
        texts.add("LSpire NYC");
        texts.add("2019");
        // Need to specify page number, path of the PDF file, and styling/placement configuration parameters
        PDFGen.insertText(doc, 0, "specify path here", "Times regular", 50, 14.5f, 100,100, texts);

        // closing doc
        // need to call this at the end
        PDFGen.docClose(doc);

/*      Object[] rooms = {"1 Bedroom", "2 Bedroom", "3 Bedroom"};
        double[] rents = {2550, 3250, 4000};

        HashMap<String, String> configs = new HashMap<>();
        configs.put("fontFamily", "Arial");
        configs.put("xtitle", "Market Rents");
        configs.put("plotBgColor", "black");
        configs.put("paperBgColor", "red");
        configs.put("fontColor", "black");
        configs.put("title", "Upside Analysis");
        PDFGen.genBarChart(doc, 0, args[0], 100, 100, 500, 282,
                rooms, rents, 24, configs);*/
    }

}
