package controllers;
import java.util.*;
import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;

import static java.lang.System.out;



@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);


            // Your Algo Here!!
            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount

            String merchantName = null;
            BigDecimal amount = null;

            if (res.getTextAnnotationsList()!=null && !res.getTextAnnotationsList().isEmpty()){

                String[] ss = res.getTextAnnotationsList().get(0).getDescription().split("\n");
                merchantName = ss[0];

                System.out.println(merchantName);

                String st = "";
                for (int i = ss.length - 1; i >= 0; i--) {
                    if (ss[i].contains(".")) {
                        for (int j = 0; j < ss[i].length(); j++) {
                            if (Character.isDigit(ss[i].charAt(j))) {
                                st = st + ss[i].charAt(j);
                            }
                        }
                        break;
                    }
                }


                System.out.println(st);
                Integer c = Integer.parseInt(st);
                Double b = c * 0.01;
                String a = Double.toString(b);

                int j = a.indexOf('.');
                System.out.println(a);
                System.out.println(a.length());
                System.out.println(j);
                String d = a.substring(0, j + 3);
                amount = new BigDecimal(d);


            /*for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                out.printf("Position : %s\n", annotation.getBoundingPoly());
                out.printf("Text: %s\n", annotation.getDescription());
            }*/

                //TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
                System.out.println(merchantName);
                System.out.println(amount);
                return new ReceiptSuggestionResponse(merchantName, amount);
            }
            else {
                System.out.println("QQ");
                return new ReceiptSuggestionResponse(merchantName, amount);
            }

        }
    }
}
