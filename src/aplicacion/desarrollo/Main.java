package aplicacion.desarrollo;

import java.util.Scanner;

import aplicacion.conversor.conversorAPI;
import aplicacion.conversor.librerias;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        conversorAPI.requestCurrencyData();

        var myExchangeRate = new conversorAPI();

        conversorAPI.showSupportedCurrencies();

        System.out.println(" ********** Para utilizar escriba de la siguiente manera: 'cantidad' 'moneda' a 'moneda' (Ejem. Ejem. 95 MXN a USD) ********* ");
        while (true) {
            String[] inputs = scanner.nextLine().split(" ");
            boolean success = false;
            if (!librerias.isNumeric(inputs[0], '.')) {
                System.out.println("La cantidad dada no es un número."); continue;
            }

            if (inputs.length == 1) {
                if (myExchangeRate.lastExchangeRate1!= null)
                    success = myExchangeRate.requestExchange(Double.parseDouble(inputs[0]), "", "");
                else
                    System.out.println("Se ha dado una cantidad pero no hay ninguna 'moneda a moneda' guardada.");
            }
            else if (inputs.length < 4) {
                System.out.println("No se han dado suficientes argumentos."); continue;
            }
            else
                success = myExchangeRate.requestExchange (Double.parseDouble(inputs[0]), inputs[1], inputs[3]);

            if (!success) {
                System.out.println("Error de api, tipo de moneda o la cantidad escrita es demasiada.");
            }
        }

    }

}

