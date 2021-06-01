package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	
	//definire chi sono gli eventi
	//coda prioritaria di oggetti di tipo Evento, da creare 
	private PriorityQueue<Event> queue ;
	
	//parametri ingresso di simulazione  (costanti )
	private int NC ;
	private Duration T_IN ; //intervallo tra clienti 
	private LocalTime oraApertura = LocalTime.of(8, 0);
	private LocalTime oraChiusura = LocalTime.of(20, 0);
	
	//stato mondo
	private int nAuto; //auto attualmente presenti
	
	//misure in uscita 
	private int nClienti;
	private int NClientiInsoddisfatti; 
	
	//impostazioni par iniziali
	
	public void setNumCars(int NC) {
		this.NC = NC ;
	}
	
	public void setClientFrequency (Duration d){
		this.T_IN = d;
	}

	//simulazione
	public void run () {
		this.queue = new PriorityQueue<Event>() ;
		
		//stato iniziale 
		this.nAuto = NC;
		this.nClienti = 0;
		this.NClientiInsoddisfatti = 0 ;
		
		
		//eventi iniziali
		LocalTime ora = this.oraApertura ;
		while(ora.isBefore(this.oraChiusura)) {
			this.queue.add(new Event(ora, EventType.NUOVO_CLIENTE));
			ora = ora.plus(this.T_IN);
		}
		
		//ciclo di simulazione
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
		
	}




private void processEvent(Event e) {
	switch (e.getType()) {
	
	case NUOVO_CLIENTE:
		this.nClienti++;
		if(this.nAuto > 0) {
			//noleggia
			this.nAuto--;
			//devo prevedere che l'auto torni in un intervalli di 1, 2 o 3 ore
			double num = Math.random()*3; // [0,1)
			if(num <1.0) {
				this.queue.add(new Event(
						             e.getTime().plus(Duration.of(1, ChronoUnit.HOURS)),
						             EventType.RITORNO_AUTO) );
			//e.getTime() = adesso
				
			} else if (num <2.0) {
				this.queue.add(new Event(
			             e.getTime().plus(Duration.of(2, ChronoUnit.HOURS)),
			             EventType.RITORNO_AUTO) );
			} else if (num <3.0){
				this.queue.add(new Event(
			             e.getTime().plus(Duration.of(3, ChronoUnit.HOURS)),
			             EventType.RITORNO_AUTO));
				}
			
		} else {
			//insoddisfatto
			this.NClientiInsoddisfatti++;
		}
		break;
	case RITORNO_AUTO:
		this.nAuto++;
		break;
	}
	
}

public int getTotClients() {
	return this.nClienti;
	
}

public int getDissatisfied () {
	return this.NClientiInsoddisfatti;
}
}