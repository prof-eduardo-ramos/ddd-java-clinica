package br.com.fiap.clinica.domain.model;

/** Objeto de valor composto: o CRM só faz sentido com número E estado (UF). */
public record Crm(String numero, String uf) {

    public Crm {
        if (numero == null || !numero.matches("\\d{4,6}")) {
            throw new RegraDeNegocioException("CRM deve ter de 4 a 6 dígitos");
        }
        if (uf == null || !uf.trim().matches("[A-Za-z]{2}")) {
            throw new RegraDeNegocioException("UF do CRM deve ter 2 letras");
        }
        uf = uf.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "CRM/" + uf + " " + numero;
    }
}
