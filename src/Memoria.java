import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Memoria {
    private static final Memoria instancia = new Memoria();

    private enum TipoComando {
        ZERAR, NUM, DIV, MULT, SUB, SOMA, IGUAL, VIRGULA, SINAL
    }

    private final List<Consumer<String>> observers = new ArrayList<>();

    private TipoComando ultimaOperacao = null;
    private boolean substituir = false;
    private String textoAtual = "";
    private String textoBuffer = "0";

    private Memoria() { }

    public void adicionarObservador(Consumer<String> observer) {
        observers.add(observer);
    }

    public void notificarObservadores(String s) {
        observers.forEach(e -> e.accept(s));
    }

    public static Memoria getInstancia() {
        return instancia;
    }

    public String getTextoAtual() {
        return textoAtual.isEmpty() ? "0" : textoAtual;
    }

    public void processarComando(String text) {
        TipoComando tipoComando = detectarTipoComando(text);

        if (tipoComando == null) {
            return;
        }

        switch (tipoComando) {
            case ZERAR:
                textoAtual = "";
                textoBuffer = "";
                substituir = false;
                ultimaOperacao = null;
                break;
            case NUM:
            case VIRGULA:
                textoAtual = substituir ? text : textoAtual + text;
                substituir = false;
                break;
            case SINAL:
                if (!"0".equals(textoAtual)) {
                    if (textoAtual.startsWith("-")) {
                        textoAtual = textoAtual.replace("-", "");
                    } else {
                        textoAtual = "-" + textoAtual;
                    }
                }
                break;
            default:
                substituir = true;
                try { textoAtual = obterResultado(tipoComando); } catch (Exception e) {}
                textoBuffer = textoAtual;
                ultimaOperacao = tipoComando;
                break;
        }

        notificarObservadores(getTextoAtual().length() > 12 ? getTextoAtual().substring(0, 12) : getTextoAtual());
    }

    private String obterResultado(TipoComando tipoComando) {
        if (ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
            return textoAtual;
        }

        double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
        double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));
        double resultado = 0;

        switch (ultimaOperacao) {
            case SOMA:
                resultado = numeroBuffer + numeroAtual;
                break;
            case SUB:
                resultado = numeroBuffer - numeroAtual;
                break;
            case DIV:
                resultado = numeroBuffer / numeroAtual;
                break;
            case MULT:
                resultado = numeroBuffer * numeroAtual;
                break;
        }

        String resultadoString = Double.toString(resultado).replace(".", ",");
        boolean inteiro = resultadoString.endsWith(",0");
        return inteiro ? resultadoString.replace(",0" , "") : resultadoString;
    }

    private TipoComando detectarTipoComando(String text) {
        if (textoAtual.isEmpty() && text.equals("0")) {
            return null;
        }

        try {
            Integer.parseInt(text);
            return  TipoComando.NUM;
        } catch (NumberFormatException e) {
            if ("AC".equals(text)) {
                return TipoComando.ZERAR;
            } else if("/".equals(text)) {
                return TipoComando.DIV;
            } else if("*".equals(text)) {
                return TipoComando.MULT;
            } else if("-".equals(text)) {
                return TipoComando.SUB;
            } else if("+".equals(text)) {
                return TipoComando.SOMA;
            } else if("=".equals(text)) {
                return TipoComando.IGUAL;
            } else if(",".equals(text) && !textoAtual .contains(",")) {
                return TipoComando.VIRGULA;
            } else if("±".equals(text)) {
                return TipoComando.SINAL;
            }

            return null;
        }
    }
}
