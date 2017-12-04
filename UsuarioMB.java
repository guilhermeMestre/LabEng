package team.control;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.PieChartModel;

import team.entity.Candidato;
import team.entity.Voto;
import team.persistence.CandidatoDao;
import team.persistence.CandidatoDaoImp;
import team.persistence.VotoDao;
import team.persistence.VotoDaoImpl;

@ManagedBean
@ViewScoped
public class ApuracaoController {

	private CandidatoDao cdao = new CandidatoDaoImp();
	private VotoDao vdao = new VotoDaoImpl();
	private List<Voto> votos =  vdao.getAllVotos();
	private List<Candidato> candidatos = cdao.readMany();
	private PieChartModel pieModel2;
	

	@PostConstruct
	private void init(){ 
		createPieModel();
		//ordena candidatos por maior voto
		Collections.sort(candidatos, new Comparator<Candidato>(){

			@Override
			public int compare(Candidato o1, Candidato o2) {
				return (int) (o2.getVotos() - o1.getVotos());
			}
			
		});
	}
	
	private void createPieModel() {
		createPieModel2();
	}
	
	 public PieChartModel getPieModel2() {
	        return pieModel2;
	 }

	private void createPieModel2() {
		pieModel2 = new PieChartModel();
		for (Candidato candidato : candidatos) {
			if( candidato != null ){
				String nomeCandidato = candidato.getNome();
				Number numeroVotos = vdao.contaVotos(candidato);
				pieModel2.set(nomeCandidato, numeroVotos);
			}
		}
		pieModel2.setTitle("Gráfico das eleições");
		pieModel2.setLegendPosition("e");
		pieModel2.setFill(false);
		pieModel2.setShowDataLabels(true);
		pieModel2.setDiameter(150);
	}
	
	
	public List<Voto> getVotos() {
		return votos;
	}

	public void setVotos(List<Voto> votos) {
		this.votos = votos;
	}

	public List<Candidato> getCandidatos() {
		return candidatos;
	}

	public void setCandidatos(List<Candidato> candidatos) {
		this.candidatos = candidatos;
	}
	
	public long contaVotos(Candidato candidato){
		long numVotos = vdao.contaVotos(candidato);
		return numVotos;
	}
	
	
	
	
}
