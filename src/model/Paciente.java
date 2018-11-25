package model;

/**
 *
 * @author ALAKAZAM
 */
public class Paciente {
    private String nomePaciente,tipoSanguineo,idade,cpf;
    private char checkAlergia;
    int codigo;

    public Paciente() {
    }
    
    public Paciente(String nomePaciente, String tipoSanguineo, String cpf, String idade, char checkAlergia) {
        this.nomePaciente = nomePaciente;
        this.tipoSanguineo = tipoSanguineo;
        this.cpf = cpf;
        this.idade = idade;
        this.checkAlergia = checkAlergia;
        this.codigo = 0;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public char getCheckAlergia() {
        return checkAlergia;
    }

    public void setCheckAlergia(char checkAlergia) {
        this.checkAlergia = checkAlergia;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    @Override
    public String toString() {
        return nomePaciente + "\t" + cpf + "\t" + idade +  "\t" + tipoSanguineo + "\t" + checkAlergia + "\t" + codigo;
    }
    
}
