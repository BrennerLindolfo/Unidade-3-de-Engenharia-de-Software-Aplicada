package br.ufpb.dcx.esa.medievalbank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufpb.dcx.esa.medievalbank.model.Atendee;
import br.ufpb.dcx.esa.medievalbank.model.Demand;

@Service
public class AgencyService {
	private String name;
	private String manager;
	private int tick;
	
	@Autowired
	private AtendeeService atendeeService;
	
	@Autowired
	private DemandService demandService;

	public void increaseTick() {
		this.tick++;
	}
	public int getTick() {
		return this.tick;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManager() {
		return this.manager;
	}
	public String getStatus() {
		List<Atendee> listOfTheAteendes = atendeeService.getAll();
		List<Demand> listOfTheDemands = demandService.getAll();
		
		return "Atendees: "+ listOfTheAteendes+"\n" + 
		"Queue: "+ listOfTheDemands;
	}
}
