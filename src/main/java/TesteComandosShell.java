import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TesteComandosShell {

    public void executaComandoShell(String comando) throws IOException {
        System.out.println(comando);
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(comando);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        boolean flag = false;
        List<String> listaPlacas = new ArrayList<>();
        while ((s = stdInput.readLine()) != null) {
            if(s.contains("USB")){
                listaPlacas.add(s);
            }
        }

        // Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        System.out.println(listaPlacas);

    }

    public static void main(String[] args) throws IOException {
        TesteComandosShell t = new TesteComandosShell();
        String comando = "sh /home/weslley/Documentos/TADS/TCC/testes-tcl/get_fpga_order.sh";
        t.executaComandoShell(comando);
    }

}
