import com.github.sarxos.webcam.Webcam;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;
import org.json.JSONObject;

public class App extends JFrame {
    private JLabel titol;
    private JPanel contentPane;
    private JTabbedPane opcions;
    private JPanel tab1;
    private JPanel tab2;
    private JButton uploadButton;
    private JTextField uploadText;
    private JButton photo;
    private JComboBox<Webcam> camsSelect;
    private View0 view01;
    private View0 view02;



    private ImageIcon icon;
    private Webcam webcam;

    public App() {
        add(contentPane);
        setTitle("XSOLVER");
        setSize(900, 700);
        tab1();
        tab2();

    }


    private void tab1() {
        uploadText.setBounds(720,40,0,0);
        uploadText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent a) {
                try {
                    uploadAction();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    uploadAction();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    private void tab2() {
        selectCam();
        camsSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                webcam = (Webcam) camsSelect.getSelectedItem();
            }
        });

        photo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        photo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                webcam.setViewSize(new Dimension(640, 480)); //640x480
                webcam.open();
                BufferedImage image = webcam.getImage();
                try {
                    ImageIO.write(image, "PNG", new File("pic.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                webcam.close();
                ImageIcon icon = new ImageIcon("pic.png");
                icon.getImage().flush();
                view02.getViewImg().setIcon(icon);
                System.out.println(icon);

            }
        });
    }

    private void selectCam() {
        List<Webcam> cams = Webcam.getWebcams();
        if (!cams.isEmpty()) {
            for(Webcam cam: cams) {
                if (cam != null) {
                    camsSelect.addItem(cam);
                }
            }
            webcam = cams.get(0);
        } else camsSelect.setSelectedItem("No webcams detected");

    }
    private void uploadAction() throws InterruptedException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos *.png, *.jpg, *.jpeg", "png","PNG","jpg","JPG","jpeg","JPEG"));
        chooser.showOpenDialog(null);
        final File f = chooser.getSelectedFile();
        String filename = f.getAbsolutePath();

        if (f == null) return;

        uploadText.setText(filename);

        ImageIcon icon = new ImageIcon(filename);
        icon.getImage().flush();
        view01.getViewImg().setIcon(icon);
        System.out.println(icon);

    }




    public class aux2 {
        // **********************************************
        // *** Update or verify the following values. ***
        // **********************************************

        // Replace <Subscription Key> with your valid subscription key.
        private static final String subscriptionKey = "70ed3faa8baf440e93f867eb9c15501c";

        // You must use the same Azure region in your REST API method as you used to
        // get your subscription keys. For example, if you got your subscription keys
        // from the West US region, replace "westcentralus" in the URL
        // below with "westus".
        //
        // Free trial subscription keys are generated in the "westus" region.
        // If you use a free trial subscription key, you shouldn't need to change
        // this region.
        private static final String uriBase =
                "https://westcentralus.api.cognitive.microsoft.com/vision/v2.0/recognizeText";

        private static final String imageToAnalyze =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/" +
                        "Cursive_Writing_on_Notebook_paper.jpg/800px-Cursive_Writing_on_Notebook_paper.jpg";

        private void imageToString() {
            CloseableHttpClient httpTextClient = HttpClientBuilder.create().build();
            CloseableHttpClient httpResultClient = HttpClientBuilder.create().build();;

            try {
                // This operation requires two REST API calls. One to submit the image
                // for processing, the other to retrieve the text found in the image.

                URIBuilder builder = new URIBuilder(uriBase);

                // Request parameter.
                builder.setParameter("mode", "Handwritten");

                // Prepare the URI for the REST API method.
                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);

                // Request headers.
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

                // Request body.
                StringEntity requestEntity =
                        new StringEntity("{\"url\":\"" + imageToAnalyze + "\"}");
                request.setEntity(requestEntity);

                // Two REST API methods are required to extract handwritten text.
                // One method to submit the image for processing, the other method
                // to retrieve the text found in the image.

                // Call the first REST API method to detect the text.
                HttpResponse response = httpTextClient.execute(request);

                // Check for success.
                if (response.getStatusLine().getStatusCode() != 202) {
                    // Format and display the JSON error message.
                    HttpEntity entity = response.getEntity();
                    String jsonString = EntityUtils.toString(entity);
                    JSONObject json = new JSONObject(jsonString);
                    System.out.println("Error:\n");
                    System.out.println(json.toString(2));
                    return;
                }

                // Store the URI of the second REST API method.
                // This URI is where you can get the results of the first REST API method.
                String operationLocation = null;

                // The 'Operation-Location' response header value contains the URI for
                // the second REST API method.
                Header[] responseHeaders = response.getAllHeaders();
                for (Header header : responseHeaders) {
                    if (header.getName().equals("Operation-Location")) {
                        operationLocation = header.getValue();
                        break;
                    }
                }

                if (operationLocation == null) {
                    System.out.println("\nError retrieving Operation-Location.\nExiting.");
                    System.exit(1);
                }

                // If the first REST API method completes successfully, the second
                // REST API method retrieves the text written in the image.
                //
                // Note: The response may not be immediately available. Handwriting
                // recognition is an asynchronous operation that can take a variable
                // amount of time depending on the length of the handwritten text.
                // You may need to wait or retry this operation.

                System.out.println("\nHandwritten text submitted.\n" +
                        "Waiting 5 seconds to retrieve the recognized text.\n");
                Thread.sleep(5000);

                // Call the second REST API method and get the response.
                HttpGet resultRequest = new HttpGet(operationLocation);
                resultRequest.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

                HttpResponse resultResponse = httpResultClient.execute(resultRequest);
                HttpEntity responseEntity = resultResponse.getEntity();

                if (responseEntity != null) {
                    // Format and display the JSON response.
                    String jsonString = EntityUtils.toString(responseEntity);
                    JSONObject json = new JSONObject(jsonString);
                    System.out.println("Text recognition result response: \n");
                    System.out.println(json.toString(2));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
