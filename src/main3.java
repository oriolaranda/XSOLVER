
import com.github.sarxos.webcam.Webcam;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class main3 {
        public static void main(String[] args) throws IOException {
            List<Webcam> cams = Webcam.getWebcams();
            for (Webcam webcam: cams) {
                if (webcam != null) {
                    System.out.println("Webcam: " + webcam.getName());
                } else {
                    System.out.println("No webcam detected");
                }
            }
            Webcam webcam = cams.get(1);
            webcam.open();
            BufferedImage image = webcam.getImage();
            // save image to PNG file
            ImageIO.write(image, "PNG", new File("test.png"));
        }
}
