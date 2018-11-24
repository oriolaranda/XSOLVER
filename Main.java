import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;


public class Main {

    public static void main(String[] args) {

        String input = "3+2x = 4x"; //aqui escribim el input corresponent.
        if (args.length > 0)
            input = args[0];

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
                        System.out.println(result);
                    }
                }

            }

        } catch (WAException e) {
            e.printStackTrace();
        }

    }

}