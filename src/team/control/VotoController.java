package team.control;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import team.entity.Candidato;
import team.entity.Voto;
import team.persistence.CandidatoDao;
import team.persistence.CandidatoDaoImp;
import team.persistence.VotoDao;
import team.persistence.VotoDaoImpl;

@ManagedBean(name="votoMB")
@ViewScoped
public class VotoController {
	
	private CandidatoDao dao = new CandidatoDaoImp();
	private Voto voto = new Voto();
	private List<Candidato> candidatos = dao.readMany();
	private Candidato selectedCandidato = new Candidato();
	
	
	
	@PostConstruct
	private void init(){ 
			
	}
	
	public List<Candidato> getCandidatos() {
		return candidatos;
	}

	public void setCandidatos(List<Candidato> candidatos) {
		this.candidatos = candidatos;
	}
	
	public Candidato getSelectedCandidato(){
		return selectedCandidato;
		
	}
	
	public void setSelectedCandidato(Candidato selectedCandidato){
		this.selectedCandidato = selectedCandidato;
	}

	public Voto getVoto() {
		return voto;
	}

	public void setVoto(Voto voto) {
		this.voto = voto;
	}
	
	public String votar(){
		String nomeCandidato = voto.getCandidatoEscolhido().getNome();
		VotoDao daoVoto = new VotoDaoImpl();
		daoVoto.save(voto);
		voto = new Voto();
		MessagesView mV = new MessagesView();
		mV.info("Voto no candidato " + nomeCandidato + " gravado com sucesso!");
		return "home";
	}
	
}
