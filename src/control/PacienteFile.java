package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import model.Paciente;

/**
 *
 * @author ALAKAZAM
 */

public class PacienteFile {
    private BufferedWriter salvarArquivo = null;
    private BufferedReader lerArquivo = null;
    private FileSystemView localDesktop = FileSystemView.getFileSystemView();
    private File localArquivo = new File(localDesktop.getHomeDirectory().getPath()+File.separator+"informacoesPacientes.txt");   
    private StringBuilder bufferString = new StringBuilder();
    
    public boolean cadastraPaciente(Paciente paciente){
        try {
            salvarArquivo = new BufferedWriter(new FileWriter(localArquivo,true));
            salvarArquivo.write(String.valueOf(paciente));
            salvarArquivo.newLine();
            salvarArquivo.flush();
            salvarArquivo.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    } 
    public boolean cadastraPacientes(Paciente[] paciente){
        if(localArquivo.exists()){
            localArquivo.delete();
        }
        try {
            salvarArquivo = new BufferedWriter(new FileWriter(localArquivo));
            
            for (Paciente pacientes : paciente) {
                salvarArquivo.write(String.valueOf(pacientes));
                salvarArquivo.newLine();
                salvarArquivo.flush();
            }
            
            salvarArquivo.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    } 
    
    public Paciente consultarPaciente2(JFrame frame,String nomePaciente){
        Paciente paciente = new Paciente();
        String informacaoPaciente = "";
        int posicaoAtual = -1,fim,palavra,auxiliar;
        try {
            lerArquivo = new BufferedReader(new FileReader(localArquivo));
            try {
                while((informacaoPaciente = lerArquivo.readLine()) != null){
                    bufferString.append(informacaoPaciente);
                    bufferString.append("\n");
                }
                
                posicaoAtual = bufferString.indexOf(nomePaciente);
                if(posicaoAtual != 1){
                    fim =  bufferString.indexOf("\t",posicaoAtual);
                    String nomeConsulta = bufferString.substring(posicaoAtual, fim).trim();
                    if(nomeConsulta.equals(nomePaciente)){
                        paciente.setNomePaciente(nomeConsulta);
                        palavra = bufferString.indexOf("Tipo sanguineo do paciente",fim);
                        auxiliar = bufferString.indexOf(": ",palavra);
                        fim = bufferString.indexOf("\t",auxiliar);
                        paciente.setTipoSanguineo(dadosPaciente(auxiliar, fim));
                        palavra = bufferString.indexOf("CPF do paciente",fim);
                        auxiliar = bufferString.indexOf(": ",palavra);
                        fim = bufferString.indexOf("\t",auxiliar);
                        paciente.setCpf(dadosPaciente(auxiliar, fim));
                        palavra = bufferString.indexOf("Data de nascimento do paciente",fim);
                        auxiliar = bufferString.indexOf(": ",palavra);
                        fim = bufferString.indexOf("\t",auxiliar);
                        paciente.setIdade(dadosPaciente(auxiliar, fim));
                        palavra = bufferString.indexOf("Alergico",fim);
                        auxiliar = bufferString.indexOf(": ",palavra);
                        fim = bufferString.indexOf("\n",auxiliar);
                        paciente.setCheckAlergia(dadosPaciente(auxiliar, fim).charAt(0));
                        return paciente;
                    }else{
                        System.out.println("não encontrado");
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Não foi possível encontrar este paciente");
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Arquivo não encontrado");
        }
        return null;
    }
    
    public Paciente consultarPaciente(JFrame frame,String nomePaciente,String caminhoArquivo){
        localArquivo = caminhoArquivo == null ? localArquivo : new File(caminhoArquivo); 
        Paciente paciente = new Paciente();
        boolean valida = false;
        String informacaoPaciente = "";
        int posicaoAtual = -1,fim,aux = 0,auxiliar;
        try {
            lerArquivo = new BufferedReader(new FileReader(localArquivo));
            try {
                while((informacaoPaciente = lerArquivo.readLine()) != null){
                    bufferString.append(informacaoPaciente);
                    bufferString.append("\n");
                    aux++;
                }
                
                posicaoAtual = bufferString.indexOf(nomePaciente);
                fim = bufferString.indexOf("\t",posicaoAtual);
                String nome = bufferString.substring(posicaoAtual,fim).trim();
               
                if(posicaoAtual != -1){
                    
                    while(aux > 0 && valida == false){
                        if(nomePaciente.equals(nome)){
                            valida = true;
                        }else{
                            fim = bufferString.indexOf("\n",fim);
                            posicaoAtual = bufferString.indexOf(nomePaciente,fim);
                            fim = bufferString.indexOf("\t",posicaoAtual);
                            nome = bufferString.substring(posicaoAtual,fim).trim();
                        }
                        aux--;
                    }
                }
               
                if(valida){
                    fim =  bufferString.indexOf("\t",posicaoAtual);
                    paciente.setNomePaciente(bufferString.substring(posicaoAtual, fim).trim());
                    auxiliar = fim + 1;
                    fim = bufferString.indexOf("\t",auxiliar);
                    paciente.setCpf(bufferString.substring(auxiliar, fim));
                    auxiliar = fim + 1;
                    fim = bufferString.indexOf("\t",auxiliar);
                    paciente.setIdade(bufferString.substring(auxiliar, fim));
                    auxiliar = fim + 1;

                    fim = bufferString.indexOf("\t",auxiliar);
                    paciente.setTipoSanguineo(bufferString.substring(auxiliar, fim));
                    auxiliar = fim + 1;

                    fim = bufferString.indexOf("\t",auxiliar);
                    paciente.setCheckAlergia(bufferString.substring(auxiliar, fim).charAt(0));
                    return paciente;
                }
                lerArquivo.close();
            }catch (IOException | StringIndexOutOfBoundsException | NullPointerException ex) {
                return null;
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Arquivo não encontrado");
        }
        return null;
    }
    
    public String dadosPaciente(int inicio, int fim){
        String dados = "";
        dados = bufferString.substring(++inicio, fim);
        return dados.trim();
    }
 
    public Paciente[] consultarTodosPaciente(JFrame jf,String caminhoArquivo){
        localArquivo = caminhoArquivo == null ? localArquivo : new File(caminhoArquivo); 
        Paciente[] pacientes;
        Paciente paciente;
        String informacaoPaciente = "",palavra = "";
        
        int posicaoAtual = -1,fim,auxiliar = 0,i = 0;
        try {
            lerArquivo = new BufferedReader(new FileReader(localArquivo));
          
            try {
                while((informacaoPaciente = lerArquivo.readLine()) != null){
                    bufferString.append(informacaoPaciente);
                    bufferString.append("\n");
                    i++;
                }
                pacientes = new Paciente[i];
                String[] vetPalavra;
                
                posicaoAtual = bufferString.indexOf("");
                while(i > 0){
                    fim = bufferString.indexOf("\n",posicaoAtual);
                    palavra = bufferString.substring(posicaoAtual, fim);
                    vetPalavra = palavra.split("\t");
                    paciente = new Paciente();
                    paciente.setNomePaciente(vetPalavra[0].trim());
                    paciente.setCpf(vetPalavra[1].trim());
                    paciente.setIdade(vetPalavra[2].trim());
                    paciente.setTipoSanguineo(vetPalavra[3].trim());
                    paciente.setCheckAlergia(vetPalavra[4].charAt(0));
                    paciente.setCodigo(Integer.parseInt(vetPalavra[5]));
                    pacientes[auxiliar] = paciente;
                    
                    posicaoAtual = fim + 1;
                    auxiliar++;
                    i--;
                }
                           
                lerArquivo.close();
                return pacientes;
            } catch (IOException | StringIndexOutOfBoundsException | NullPointerException ex) {
                return null;
            }
           
        } catch (FileNotFoundException  ex) {
            JOptionPane.showMessageDialog(jf, "Arquivo não encontrado");
        }
        return null;
    }
    
    public Paciente[] consultaEspecifica(JFrame jf,String tipo, String caminhoArquivo){
        localArquivo = caminhoArquivo == null ? localArquivo : new File(caminhoArquivo); 
        Paciente[] pacientes;
        Paciente paciente;
        String informacaoPaciente = "",palavra = "";
        int posicaoAtual = -1,fim,auxiliar = 0,i = 0;

        try {
            lerArquivo = new BufferedReader(new FileReader(localArquivo));
            try {
                while((informacaoPaciente = lerArquivo.readLine()) != null){
                    bufferString.append(informacaoPaciente);
                    bufferString.append("\n");
                    i++;
                }
                pacientes = new Paciente[i];
                String[] vetPalavra;
                
                posicaoAtual = bufferString.indexOf("");
                while(i > 0){
                    fim = bufferString.indexOf("\n",posicaoAtual);
                    palavra = bufferString.substring(posicaoAtual, fim);
                    vetPalavra = palavra.split("\t");
                    paciente = new Paciente();
                    paciente.setNomePaciente(vetPalavra[0].trim());
                    paciente.setCpf(vetPalavra[1].trim());
                    paciente.setIdade(vetPalavra[2].trim());
                    paciente.setTipoSanguineo(vetPalavra[3].trim());
                    paciente.setCheckAlergia(vetPalavra[4].trim().charAt(0));
                    paciente.setCodigo(Integer.parseInt(vetPalavra[5]));
                    pacientes[auxiliar] = paciente;
                    
                    posicaoAtual = fim + 1;
                    auxiliar++;
                    i--;
                }
                lerArquivo.close();
                return pacientes;
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(jf, "Usuário não encontrado");
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(jf, "Arquivo não encontrado");
        }
        return null;
    }
    
    public boolean excluirPaciente(JFrame frame, String nomePaciente){
        Paciente[] pacientes;
        Paciente paciente;
        String[] vetPalavra = null;
        String informacaoPaciente = "",palavra;
        int posicaoAtual = -1,fim,auxiliar = 0,i = 0,aux = 0;
        boolean valida = false;
        try {
            lerArquivo = new BufferedReader(new FileReader(localArquivo));
            try {
                while((informacaoPaciente = lerArquivo.readLine()) != null){
                    bufferString.append(informacaoPaciente);
                    bufferString.append("\n");
                    i++;
                    aux++;
                }
                pacientes = new Paciente[i-1];
                posicaoAtual = bufferString.indexOf(nomePaciente);
                fim = bufferString.indexOf("\t",posicaoAtual);
                String nome = bufferString.substring(posicaoAtual,fim).trim();
                if(posicaoAtual != -1){
                    while(aux > 0 && valida == false){
                        if(nomePaciente.equals(nome)){
                            valida = true;
                        }else{
                            fim = bufferString.indexOf("\n",fim);
                            posicaoAtual = bufferString.indexOf(nomePaciente,fim);
                            fim = bufferString.indexOf("\t",posicaoAtual);
                            nome = bufferString.substring(posicaoAtual,fim).trim();
                        }
                        aux--;
                    }
                }
                if(valida){
                    fim =  bufferString.indexOf("\n",posicaoAtual);
                    bufferString.delete(posicaoAtual, fim);
                    posicaoAtual = bufferString.indexOf("");
                    while(i > 0){
                        fim = bufferString.indexOf("\n",posicaoAtual);
                        palavra = bufferString.substring(posicaoAtual, fim);
                        vetPalavra = palavra.split("\t");
                        if(!vetPalavra[0].equals("")){
                            paciente = new Paciente();
                            paciente.setNomePaciente(vetPalavra[0]);
                            paciente.setCpf(vetPalavra[1]);
                            paciente.setIdade(vetPalavra[2]);
                            paciente.setTipoSanguineo(vetPalavra[3]);
                            paciente.setCheckAlergia(vetPalavra[4].charAt(0));
                            paciente.setCodigo(Integer.parseInt(vetPalavra[5]));
                            pacientes[auxiliar] = paciente;
                            auxiliar++;
                        }
                        posicaoAtual = fim + 1;
                        i--;                       
                    }
                    cadastraPacientes(pacientes);
                    return true;
                }
                lerArquivo.close();
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Não foi possível encontrar este paciente");
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Arquivo não encontrado");
        }
        return false;
    }
    
    public boolean alterarDadosPaciente(JFrame frame, Paciente pacienteModificado,String nome,String caminhoArquivo){
        localArquivo = caminhoArquivo == null ? localArquivo : new File(caminhoArquivo); 
        Paciente[] pacientes;
        Paciente paciente;
        String[] vetPalavra = null;
        String informacaoPaciente = "",palavra;
        int posicaoAtual = -1,fim,auxiliar = 0,i = 0, aux = 0;
        boolean valida = false;
        
        try {
            lerArquivo = new BufferedReader(new FileReader(localArquivo));
            try {
                while((informacaoPaciente = lerArquivo.readLine()) != null){
                    bufferString.append(informacaoPaciente);
                    bufferString.append("\n");
                    i++;
                    aux++;
                }
                pacientes = new Paciente[i];
                
                posicaoAtual = bufferString.indexOf(nome);
                fim = bufferString.indexOf("\t",posicaoAtual);
                String nomeP = bufferString.substring(posicaoAtual,fim).trim();
                
                if(posicaoAtual != -1){
                    while(aux > 0 && valida == false){
                        if(nomeP.equals(nome)){
                            valida = true;
                        }else{
                            fim = bufferString.indexOf("\n",fim);
                            posicaoAtual = bufferString.indexOf(nome,fim);
                            fim = bufferString.indexOf("\t",posicaoAtual);
                            nome = bufferString.substring(posicaoAtual,fim).trim();
                        }
                        aux--;
                    }
                }
                if(valida){
                    fim = bufferString.indexOf("\n",posicaoAtual);
                    palavra = bufferString.substring(posicaoAtual, fim);
                    vetPalavra = palavra.split("\t");

                    bufferString.replace(posicaoAtual, fim,
                        pacienteModificado.getNomePaciente() + "\t" +
                        pacienteModificado.getCpf()+ "\t" +
                        pacienteModificado.getIdade()+ "\t" +
                        pacienteModificado.getTipoSanguineo()+ "\t" +
                        pacienteModificado.getCheckAlergia()+ "\t"+
                        vetPalavra[5]
                     );

                    posicaoAtual = bufferString.indexOf("");
                    while(i > 0){
                        fim = bufferString.indexOf("\n",posicaoAtual);
                        palavra = bufferString.substring(posicaoAtual, fim);
                        vetPalavra = palavra.split("\t");
                        paciente = new Paciente();
                        paciente.setNomePaciente(vetPalavra[0]);
                        paciente.setCpf(vetPalavra[1]);
                        paciente.setIdade(vetPalavra[2]);
                        paciente.setTipoSanguineo(vetPalavra[3]);
                        paciente.setCheckAlergia(vetPalavra[4].charAt(0));
                        paciente.setCodigo(Integer.parseInt(vetPalavra[5]));
                        pacientes[auxiliar] = paciente;
                        auxiliar++;
                        posicaoAtual = fim + 1;
                        i--;      

                        cadastraPacientes(pacientes);
                    }
                    lerArquivo.close();
                    return true;
                }
            }catch (IOException | StringIndexOutOfBoundsException | NullPointerException ex) {
                return false;
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, "Arquivo não encontrado");
        }
        return false;
    }
}