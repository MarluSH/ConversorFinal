package aplicacion.conversor;

public class librerias {

    public static boolean isNumeric(String str, char period) {
        if (str == null || str.length() == 0)
            return false;
        char[] data = str.toCharArray();

        int i = 0;
        if (data[0] == '-' && data.length > 1) i = 1;

        if (period == 'n') {
            for (; i < data.length ; i++) {
                if (data[i] < '0' || data[i] > '9')
                    return false;
            }
        }
        else {
            for (; i < data.length ; i++) {
                if (((data[i] < '0' || data[i] > '9') && data[i] != period)) {
                    return false;
                }
            }
        }
        return true;
    }

    static public void printTabulated(String[] printables, int padding, int printablesPerLine) {
        for (int i = 0; i < printables.length; i++) {
            System.out.printf("%-" + printablesPerLine + "s", printables[i]);
            if (i % padding == 0)
                System.out.println();
        }

    }

}
