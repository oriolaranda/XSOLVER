import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class View0 extends JPanel {
    private JPanel bottom;
    private JTextArea viewSolution;
    private JButton solve;
    private JLabel viewImg;

    public View0(){
        add(bottom);
        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resoldre();
            }
        });

    }

    public JLabel getViewImg(){
        return viewImg;
    }

    private void resoldre() {
        String solucio ="Hola";//solucio
        viewSolution.setText(solucio);
    }

}
