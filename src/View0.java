import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import com.sun.javafx.css.CalculatedValue;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;


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
        String equacio = "2x+4=0";

        String solucio = calculateResult(equacio);//solucio
        if (!solucio.equals("error")) viewSolution.setText(equacio + "  =>  " + solucio);
    }

    public String calculateResult(String input) {

        //aqui escribim el input corresponent.

        WAEngine engine = new WAEngine();   //WAEngine es fa servir per a fer les queries.

        // Afegim les propietats al nostre engine que mantindrem por a la nostre query.
        engine.setAppID("W2UJUH-L93UXQK3EP");
        engine.addFormat("plaintext");  //tipus de entrada.

        // Create the query.
        WAQuery query = engine.createQuery();   //a partir del nostre engine creem la query.

        // Set properties of the query.
        query.setInput(input);  //afegim el input a la query

        try {
            WAQueryResult queryResult = engine.performQuery(query); //aquesta falla

            if (queryResult.isError()) {
                System.out.println("Query error");
            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");
            } else {
                String result = "";
                for (WAPod pod : queryResult.getPods()) {   //cada pod sera un apartat
                    if (!pod.isError() && (pod.getTitle().equals("Solution"))) {
                        //recollim el nostre resultat
                        for (WASubpod subpod : pod.getSubpods()) {  //cada subpod es una part del apartat
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                    result = result + (((WAPlainText) element).getText());
                                }
                            }
                        }
                        return result;
                    }
                }

            }

        } catch (WAException e) {
            e.printStackTrace();
        }
        return "error";
    }

}
