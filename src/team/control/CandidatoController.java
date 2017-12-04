package team.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import team.entity.Candidato;
import team.persistence.CandidatoDao;
import team.persistence.CandidatoDaoImp;
import team.persistence.VotoDao;
import team.persistence.VotoDaoImpl;

@ManagedBean
@SessionScoped
public class CandidatoController {

	private Candidato candidato = new Candidato();
	private UploadedFile uFile = null;
	private StreamedContent content = null;
	private boolean botaoDesativado = true;

	public boolean isBotaoDesativado() {
		return botaoDesativado;
	}

	public void setBotaoDesativado(boolean botaoDesativado) {
		this.botaoDesativado = botaoDesativado;
	}
	
	//manipulador da imagem que aparece na graphicImage da view
	public StreamedContent getSc() throws IOException {
		return content;
	}

	public void setSc(StreamedContent sc) {
		this.content = sc;
	}

	public UploadedFile getuFile() {
		return uFile;
	}

	public void setuFile(UploadedFile uFile) {
		this.uFile = uFile;
	}

	public Candidato getCandidato() {
		return candidato;
	}

	public void setCandidato(Candidato candidato) {
		this.candidato = candidato;
	}

	public String excluirCandidato() {
		Candidato candidato = getCandidato();
		CandidatoDao dao = new CandidatoDaoImp();
		VotoDao vdao = new VotoDaoImpl();
		vdao.removeVotos(candidato.getId());
		dao.remove(candidato);
		
		candidato = new Candidato();
		setCandidato(candidato);
		setBotaoDesativado(true);
		
		MessagesView mV = new MessagesView();
		mV.info("Candidato excluido com sucesso!");
		
		return "Candidato";
	}

	public String adicionarCandidato() {
		CandidatoDao dao = new CandidatoDaoImp();

		dao.save(candidato);
		Candidato c = new Candidato();
		setCandidato(c);

		MessagesView mV = new MessagesView();
		mV.info("Cadastro realizado com sucesso!");
		return "Candidato";
	}

	public void alterarCandidato() {
		CandidatoDao dao = new CandidatoDaoImp();
		dao.update(candidato);
		
		Candidato c = new Candidato();
		setCandidato(c);

		MessagesView mV = new MessagesView();
		mV.info("Candidato alterado com sucesso!");
	}

	public String buscarCandidato() {
		CandidatoDao dao = new CandidatoDaoImp();
		Candidato c = new Candidato();
		
		try {
			c = dao.read(candidato.getNome());
		} catch (Exception e) {
			MessagesView mV = new MessagesView();
			mV.error("Candidato não encontrado");
		}
		setCandidato(c);

		if (c.getNome() != null) {
				setBotaoDesativado(false);
				if(c.getFoto() != null){
					setSc(getStreamedContent(c.getFoto()));
				}
		} 
		
		return "Candidato";
	}
	
	public StreamedContent getStreamedContent(byte[] imagemByte){
		StreamedContent imagemSc = new DefaultStreamedContent();
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			byte[] iByte = imagemByte;
			ByteArrayInputStream bai = new ByteArrayInputStream(iByte);
			imagemSc = new DefaultStreamedContent(bai);
			bao.close();
			bai.close();
		} catch (IOException e) {
			MessagesView mV = new MessagesView();
			mV.fatal("Erro na conversão do arquivo");
			e.printStackTrace();
		}
		return imagemSc;
	}
	
	public void handleFileUpload(FileUploadEvent event) throws IOException {
		Candidato c = getCandidato();
		// guarda a imagem selecionanda em memória
		setuFile(event.getFile());
		ByteArrayInputStream bai = new ByteArrayInputStream(event.getFile().getContents());
		// Faz com que a image apareca na view
		StreamedContent dsc = new DefaultStreamedContent(bai);
		setSc(dsc);

		bai.close();

		c.setFoto(getuFile().getContents());
		setCandidato(c);
	}
}
