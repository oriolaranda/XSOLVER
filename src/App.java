import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;


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


    private ImageIcon icon;
    private Webcam webcam;

    public App() {
        add(contentPane);
        setTitle("XSOLVER");
        setSize(700, 500);
        tab1();
        tab2();

    }


    private void tab1() {
        uploadText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent a) {
                uploadAction();
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadAction();
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

        photo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                webcam.open();
                BufferedImage image = webcam.getImage();
                // save image to PNG file
                try {
                    ImageIO.write(image, "PNG", new File("test.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
    private void uploadAction() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos *.png, *.jpg, *.jpeg", "png","PNG","jpg","JPG","jpeg","JPEG"));
        chooser.showOpenDialog(null);
        final File f = chooser.getSelectedFile();
        String filename = f.getAbsolutePath();
        if (f == null) {
            return;
        }
        uploadText.setText(filename);

        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                Thread.sleep(5000);//simulate large image takes long to load

                return null;
            }

            @Override
            protected void done() {
                super.done();
                view01.getViewImg().setIcon(icon);
            }
        };
        sw.execute();

    }
}
